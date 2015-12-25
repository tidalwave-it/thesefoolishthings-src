/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.impl.DatumAndRole;
import it.tidalwave.role.spi.impl.MultiMap;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.spi.LogUtil.*;

/***********************************************************************************************************************
 *
 * A basic implementation of a {@link RoleManager}. This class must be specialized to:
 *
 * <ol>
 * <li>discover roles (see {@link #scan(java.util.Collection)}</li>
 * <li>associate roles to a datum (see {@link #findDatumTypesForRole(java.lang.Class)}</li>
 * <li>associate roles to contexts (see {@link #findContextTypeForRole(java.lang.Class)}</li>
 * <li>eventually retrieve beans to inject in created roles (see {@link #getBean(java.lang.Class)}</li>
 * </ol>
 *
 * Specializations might use annotations or configuration files to accomplish these tasks.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class RoleManagerSupport implements RoleManager
  {
    private final static Comparator<Class<?>> CLASS_COMPARATOR = new Comparator<Class<?>>()
      {
        @Override
        public int compare (final @Nonnull Class<?> class1, final @Nonnull Class<?> class2)
          {
            return class1.getName().compareTo(class2.getName());
          }
      };

    private final ContextManager contextManager = ContextManager.Locator.find();

    /* VisibleForTesting */ final MultiMap<DatumAndRole, Class<?>> roleMapByDatumAndRole = new MultiMap<>();

    // FIXME: use ConcurrentHashMap// FIXME: use ConcurrentHashMap
    /* VisibleForTesting */ final Set<DatumAndRole> alreadyScanned = new HashSet<>();

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <ROLE_TYPE> List<? extends ROLE_TYPE> findRoles (final @Nonnull Object datum,
                                                            final @Nonnull Class<ROLE_TYPE> roleType)
      {
        log.trace("findRoles({}, {})", shortId(datum), shortName(roleType));
        final Class<?> datumType = findClass(datum);
        final List<ROLE_TYPE> roles = new ArrayList<>();
        final Set<Class<? extends ROLE_TYPE>> roleImplementationTypes = findRoleImplementationsFor(datumType, roleType);

outer:  for (final Class<? extends ROLE_TYPE> roleImplementationType : roleImplementationTypes)
          {
            for (final Constructor<?> constructor : roleImplementationType.getDeclaredConstructors())
              {
                log.trace(">>>> trying constructor {}", constructor);
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                Class<?> contextType = null;
                Object context = null;

                try
                  {
                    contextType = findContextTypeForRole(roleImplementationType);
                    log.trace(">>>> contexts: {}", shortIds(contextManager.getContexts()));

                    try
                      {
                        context = contextManager.findContextOfType(contextType);
                      }
                    catch (NotFoundException e)
                      {
                        log.trace(">>>> role {} discarded, can't find context: {}",
                                  shortName(roleImplementationType), shortName(contextType));
                        continue outer;
                      }
                  }
                catch (NotFoundException e)
                  {
                    // ok, no context
                  }

                try
                  {
                    final Object[] params = getParameterValues(parameterTypes, datumType, datum, contextType, context);
                    roles.add(roleType.cast(constructor.newInstance(params)));
                    break;
                  }
                catch (InstantiationException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException e)
                  {
                    log.error("Could not instantiate role of type " + roleImplementationType, e);
                  }
              }
          }

        if (log.isTraceEnabled())
          {
            log.trace(">>>> findRoles() returning: {}", shortIds((Collection)roles));
          }

        return roles;
      }

    /*******************************************************************************************************************
     *
     * Prepare the constructor parameters out of the given expected types. Parameters will be eventually made of the
     * given datum, context, and other objects returned by {@link #getBean(java.lang.Class)}.
     *
     * @param   parameterTypes      the expected types
     * @param   datumClass          the type of the datum
     * @param   datum               the datum
     * @param   contextClass        the type of the context
     * @param   context             the context
     *
     ******************************************************************************************************************/
    @Nonnull
    private Object[] getParameterValues (final @Nonnull Class<?>[] parameterTypes,
                                         final @Nonnull Class<?> datumClass,
                                         final @Nonnull Object datum,
                                         final @Nullable Class<?> contextClass,
                                         final @Nullable Object context)
      {
        final List<Object> values = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes)
          {
            if (parameterType.isAssignableFrom(datumClass))
              {
                values.add(datum);
              }
            else if ((contextClass != null) && parameterType.isAssignableFrom(contextClass))
              {
                values.add(context);
              }
            else // generic injection
              {
                values.add(getBean(parameterType));
              }
          }

        log.trace(">>>> constructor parameters: {}", values);
        return values.toArray();
      }

    /*******************************************************************************************************************
     *
     * Finds the role implementations for the given owner type and role type. This method might discover new
     * implementations that weren't found during the initial scan, since the initial scan can't go down in a
     * hierarchy; that is, given a Base class or interface with some associated roles, it can't associate those roles
     * to subclasses (or implementations) of Base. Now we can navigate up the hierarchy and complete the picture.
     * Each new discovered role is added into the map, so the next time scanning will be faster.
     *
     * @param   datumType       the type of the datum
     * @param   roleType        the type of the role to find
     * @param                   the types of role implementations
     *
     ******************************************************************************************************************/
    @Nonnull
    /* VisibleForTesting */ synchronized <RT> Set<Class<? extends RT>> findRoleImplementationsFor (
            final @Nonnull Class<?> datumType,
            final @Nonnull Class<RT> roleType)
      {
        final DatumAndRole datumAndRole = new DatumAndRole(datumType, roleType);

        if (!alreadyScanned.contains(datumAndRole))
          {
            alreadyScanned.add(datumAndRole);
            final Set<Class<?>> before = new HashSet<>(roleMapByDatumAndRole.getValues(datumAndRole));

            for (final DatumAndRole superDatumAndRole : datumAndRole.getSuper())
              {
                roleMapByDatumAndRole.addAll(datumAndRole, roleMapByDatumAndRole.getValues(superDatumAndRole));
              }

            final Set<Class<?>> after = new HashSet<>(roleMapByDatumAndRole.getValues(datumAndRole));
            logChanges(datumAndRole, before, after);
          }

        return (Set<Class<? extends RT>>)(Set)roleMapByDatumAndRole.getValues(datumAndRole);
      }

    /*******************************************************************************************************************
     *
     * Scans all the given role implementation classes and build a map of roles by owner class.
     *
     * @param   roleImplementationTypes     the types of role implementations to scan
     *
     ******************************************************************************************************************/
    protected void scan (final @Nonnull Collection<Class<?>> roleImplementationTypes)
      {
        log.debug("scan({})", shortNames(roleImplementationTypes));

        for (final Class<?> roleImplementationType : roleImplementationTypes)
          {
            for (final Class<?> datumType : findDatumTypesForRole(roleImplementationType))
              {
                for (final Class<?> roleType : findAllImplementedInterfacesOf(roleImplementationType))
                  {
                    if (!roleType.getName().equals("org.springframework.beans.factory.aspectj.ConfigurableObject"))
                      {
                        roleMapByDatumAndRole.add(new DatumAndRole(datumType, roleType), roleImplementationType);
                      }
                  }
              }
          }

        logRoles();
      }

    /*******************************************************************************************************************
     *
     * Finds all the interfaces implemented by a given class, including those eventually implemented by superclasses
     * and interfaces that are indirectly implemented (e.g. C implements I1, I1 extends I2).
     *
     * @param  clazz    the class to inspect
     * @return          the implemented interfaces
     *
     ******************************************************************************************************************/
    @Nonnull
    /* VisibleForTesting */ static SortedSet<Class<?>> findAllImplementedInterfacesOf (final @Nonnull Class<?> clazz)
      {
        final SortedSet<Class<?>> interfaces = new TreeSet<>(CLASS_COMPARATOR);
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));

        for (final Class<?> interface_ : interfaces)
          {
            interfaces.addAll(findAllImplementedInterfacesOf(interface_));
          }

        if (clazz.getSuperclass() != null)
          {
            interfaces.addAll(findAllImplementedInterfacesOf(clazz.getSuperclass()));
          }

        return interfaces;
      }

    /*******************************************************************************************************************
     *
     * Retrieves an extra bean.
     *
     * @param <T>           the static type of the bean
     * @param beanType      the dynamic type of the bean
     * @return              the bean
     *
     ******************************************************************************************************************/
    @Nullable
    protected abstract <T> T getBean (@Nonnull Class<T> beanType);

    /*******************************************************************************************************************
     *
     * Returns the type of the context associated to the given role implementation type.
     *
     * @param   roleImplementationType      the role type
     * @return                              the context type
     * @throws NotFoundException            if no context is found
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract Class<?> findContextTypeForRole (@Nonnull Class<?> roleImplementationType)
      throws NotFoundException;

    /*******************************************************************************************************************
     *
     * Returns the valid datum types for the given role implementation type.
     *
     * @param   roleImplementationType      the role type
     * @return                              the datum types
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract Class<?>[] findDatumTypesForRole (@Nonnull Class<?> roleImplementationType);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void logChanges (final @Nonnull DatumAndRole datumAndRole,
                             final @Nonnull Set<Class<?>> before,
                             final @Nonnull Set<Class<?>> after)
      {
        after.removeAll(before);

        if (!after.isEmpty())
          {
            log.debug(">>>>>>> added implementations: {} -> {}", datumAndRole, after);

            if (log.isTraceEnabled()) // yes, trace
              {
                logRoles();
              }
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void logRoles()
      {
        log.debug("Configured roles:");

        final List<Entry<DatumAndRole, Set<Class<?>>>> entries = new ArrayList<>(roleMapByDatumAndRole.entrySet());
        Collections.sort(entries, new Comparator<Entry<DatumAndRole, Set<Class<?>>>>()
          {
            @Override
            public int compare (final @Nonnull Entry<DatumAndRole, Set<Class<?>>> e1,
                                final @Nonnull Entry<DatumAndRole, Set<Class<?>>> e2)
              {
                final int s1 = e1.getKey().getDatumClass().getName().compareTo(
                               e2.getKey().getDatumClass().getName());

                if (s1 != 0)
                  {
                    return s1;
                  }

                return e1.getKey().getRoleClass().getName().compareTo(
                       e2.getKey().getRoleClass().getName());
              }
          });

        for (final Entry<DatumAndRole, Set<Class<?>>> entry : entries)
          {
            log.debug(">>>> {}: {} -> {}",
                    new Object[] { shortName(entry.getKey().getDatumClass()),
                                   shortName(entry.getKey().getRoleClass()),
                                   shortNames(entry.getValue())});
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the class of an object, taking care of mocks created by Mockito, for which the original class is
     * returned.
     *
     * @param  owner    the owner
     * @return          the owner class
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Class<?> findClass (final @Nonnull Object owner)
      {
        Class<?> ownerClass = owner.getClass();

        if (ownerClass.toString().contains("EnhancerByMockito"))
          {
            ownerClass = ownerClass.getInterfaces()[0]; // 1st is the original class, 2nd is CGLIB proxy

            if (log.isTraceEnabled())
              {
                log.trace(">>>> owner is a mock {} implementing {}",
                        shortName(ownerClass), shortNames(Arrays.asList(ownerClass.getInterfaces())));
                log.trace(">>>> owner class replaced with {}", shortName(ownerClass));
              }
          }

        return ownerClass;
      }
  }
