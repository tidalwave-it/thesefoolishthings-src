/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.role.spi;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import it.tidalwave.util.ContextManager;
import it.tidalwave.util.annotation.VisibleForTesting;
import it.tidalwave.role.impl.OwnerAndRole;
import it.tidalwave.role.impl.MultiMap;
import it.tidalwave.dci.annotation.DciRole;
import lombok.extern.slf4j.Slf4j;
import static java.util.Comparator.*;
import static it.tidalwave.util.ShortNames.*;

/***********************************************************************************************************************
 *
 * A basic implementation of a {@link SystemRoleFactory}. This class must be specialized to:
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
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class SystemRoleFactorySupport implements SystemRoleFactory
  {
    @VisibleForTesting final MultiMap<OwnerAndRole, Class<?>> roleMapByOwnerAndRole = new MultiMap<>();

    // FIXME: use ConcurrentHashMap
    @VisibleForTesting final Set<OwnerAndRole> alreadyScanned = new HashSet<>();

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public synchronized <T> List<T> findRoles (@Nonnull final Object datum, @Nonnull final Class<? extends T> roleType)
      {
        log.trace("findRoles({}, {})", shortId(datum), shortName(roleType));
        final Class<?> datumType = findTypeOf(datum);
        final List<T> roles = new ArrayList<>();
        final var roleImplementationTypes = findRoleImplementationsFor(datumType, roleType);

        outer:  for (final var roleImplementationType : roleImplementationTypes)
          {
            for (final var constructor : roleImplementationType.getDeclaredConstructors())
              {
                log.trace(">>>> trying constructor {}", constructor);
                final var parameterTypes = constructor.getParameterTypes();
                Optional<?> context = Optional.empty();
                final var contextType = findContextTypeForRole(roleImplementationType);

                if (contextType.isPresent())
                  {
                    // With DI frameworks such as Spring it's better to avoid eager initializations of references
                    final var contextManager = ContextManager.getInstance();
                    log.trace(">>>> contexts: {}", shortIds(contextManager.getContexts()));
                    context = contextManager.findContextOfType(contextType.get());

                    if (context.isEmpty())
                      {
                        log.trace(">>>> role {} discarded, can't find context: {}",
                                  shortName(roleImplementationType), shortName(contextType.get()));
                        continue outer;
                      }
                  }

                try
                  {
                    final var params = getParameterValues(parameterTypes, datumType, datum, contextType, context);
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
            log.trace(">>>> findRoles() returning: {}", shortIds(roles));
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
    private Object[] getParameterValues (@Nonnull final Class<?>[] parameterTypes,
                                         @Nonnull final Class<?> datumClass,
                                         @Nonnull final Object datum,
                                         @Nonnull final Optional<Class<?>> contextClass,
                                         @Nonnull final Optional<?> context)
      {
        final var values = new ArrayList<>();

        for (final var parameterType : parameterTypes)
          {
            if (parameterType.isAssignableFrom(datumClass))
              {
                values.add(datum);
              }
            else if (contextClass.isPresent() && parameterType.isAssignableFrom(contextClass.get()))
              {
                values.add(context.orElse(null));
              }
            else // generic injection
              {
                // FIXME: it's injecting null, but perhaps should it throw exception?
                values.add(getBean(parameterType).orElse(null));
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
     * @return                  the types of role implementations
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting synchronized <T> Set<Class<? extends T>> findRoleImplementationsFor (
            @Nonnull final Class<?> datumType,
            @Nonnull final Class<T> roleType)
      {
        final var datumAndRole = new OwnerAndRole(datumType, roleType);

        if (!alreadyScanned.contains(datumAndRole))
          {
            alreadyScanned.add(datumAndRole);
            final var before = new HashSet<>(roleMapByOwnerAndRole.getValues(datumAndRole));

            for (final var superDatumAndRole : datumAndRole.getSuper())
              {
                roleMapByOwnerAndRole.addAll(datumAndRole, roleMapByOwnerAndRole.getValues(superDatumAndRole));
              }

            final var after = new HashSet<>(roleMapByOwnerAndRole.getValues(datumAndRole));
            logChanges(datumAndRole, before, after);
          }

        return (Set<Class<? extends T>>)(Set)roleMapByOwnerAndRole.getValues(datumAndRole);
      }

    /*******************************************************************************************************************
     *
     * Scans all the given role implementation classes and build a map of roles by owner class.
     *
     * @param   roleImplementationTypes     the types of role implementations to scan
     *
     ******************************************************************************************************************/
    protected synchronized void scan (@Nonnull final Collection<Class<?>> roleImplementationTypes)
      {
        log.debug("scan({})", shortNames(roleImplementationTypes));

        for (final var roleImplementationType : roleImplementationTypes)
          {
            for (final var datumType : findDatumTypesForRole(roleImplementationType))
              {
                for (final var roleType : findAllImplementedInterfacesOf(roleImplementationType))
                  {
                    if (!"org.springframework.beans.factory.aspectj.ConfigurableObject".equals(roleType.getName()))
                      {
                        roleMapByOwnerAndRole.add(new OwnerAndRole(datumType, roleType), roleImplementationType);
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
    @VisibleForTesting static SortedSet<Class<?>> findAllImplementedInterfacesOf (@Nonnull final Class<?> clazz)
      {
        final SortedSet<Class<?>> interfaces = new TreeSet<>(comparing(Class::getName));
        interfaces.addAll(List.of(clazz.getInterfaces()));

        for (final var interface_ : interfaces)
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
    @Nonnull
    protected <T> Optional<T> getBean (@Nonnull final Class<T> beanType)
      {
        return Optional.empty();
      }

    /*******************************************************************************************************************
     *
     * Returns the type of the context associated to the given role implementation type.
     *
     * @param   roleImplementationType      the role type
     * @return                              the context type
     *
     ******************************************************************************************************************/
    @Nonnull
    protected Optional<Class<?>> findContextTypeForRole (@Nonnull final Class<?> roleImplementationType)
      {
        final var contextClass = roleImplementationType.getAnnotation(DciRole.class).context();
        return (contextClass == DciRole.NoContext.class) ? Optional.empty() : Optional.of(contextClass);
      }

    /*******************************************************************************************************************
     *
     * Returns the valid datum types for the given role implementation type.
     *
     * @param   roleImplementationType      the role type
     * @return                              the datum types
     *
     ******************************************************************************************************************/
    @Nonnull
    protected Class<?>[] findDatumTypesForRole (@Nonnull final Class<?> roleImplementationType)
      {
        return roleImplementationType.getAnnotation(DciRole.class).datumType();
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void logChanges (@Nonnull final OwnerAndRole ownerAndRole,
                             @Nonnull final Set<Class<?>> before,
                             @Nonnull final Set<Class<?>> after)
      {
        after.removeAll(before);

        if (!after.isEmpty())
          {
            log.debug(">>>>>>> added implementations: {} -> {}", ownerAndRole, shortNames(after));

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

        final var entries = new ArrayList<>(roleMapByOwnerAndRole.entrySet());
        entries.sort(comparing((Map.Entry<OwnerAndRole, Set<Class<?>>> e) -> e.getKey().getOwnerClass().getName())
                               .thenComparing(e -> e.getKey().getRoleClass().getName()));

        for (final var entry : entries)
          {
            log.debug(">>>> {}: {} -> {}",
                      shortName(entry.getKey().getOwnerClass()),
                      shortName(entry.getKey().getRoleClass()),
                      shortNames(entry.getValue()));
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the type of an object, taking care of mocks created by Mockito, for which the implemented interface is
     * returned.
     *
     * @param  object   the object
     * @return          the object type
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting static <T> Class<T> findTypeOf (@Nonnull final T object)
      {
        var ownerClass = object.getClass();

        if (ownerClass.toString().contains("MockitoMock"))
          {
            ownerClass = ownerClass.getInterfaces()[0]; // 1st is the original class, 2nd is CGLIB proxy

            if (log.isTraceEnabled())
              {
                log.trace(">>>> owner is a mock {} implementing {}",
                          shortName(ownerClass), shortNames(List.of(ownerClass.getInterfaces())));
                log.trace(">>>> owner class replaced with {}", shortName(ownerClass));
              }
          }

        return (Class<T>)ownerClass;
      }
  }
