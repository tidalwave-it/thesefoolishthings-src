/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.role.spi.RoleManager;
import it.tidalwave.role.ContextManager;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * FIXME: drop dependencies on Spring, move to Utilities
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class AnnotationRoleManagerSupport implements RoleManager
  {
    private final ContextManager contextManager = ContextManager.Locator.find();

    private final static Comparator<Class<?>> CLASS_COMPARATOR = new Comparator<Class<?>>()
      {
        @Override
        public int compare (final @Nonnull Class<?> class1, final @Nonnull Class<?> class2)
          {
            return class1.getName().compareTo(class2.getName());
          }
      };

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor @Getter @ToString @EqualsAndHashCode @Slf4j
    static class ClassAndRole
      {
        @Nonnull
        private final Class<?> ownerClass;

        @Nonnull
        private final Class<?> roleClass;

        @Nonnull
        public List<ClassAndRole> getSuper()
          {
            final List<ClassAndRole> result = new ArrayList<ClassAndRole>();
            result.add(this);

            if (ownerClass.getSuperclass() != null)
              {
                result.addAll(new ClassAndRole(ownerClass.getSuperclass(), roleClass).getSuper());
              }

            for (final Class<?> interfaceClass : ownerClass.getInterfaces())
              {
                result.addAll(new ClassAndRole(interfaceClass, roleClass).getSuper());
              }

            return result;
          }
      }

    private MultiValueMap<ClassAndRole, Class<?>> roleMapByOwnerClass =
            new LinkedMultiValueMap<ClassAndRole, Class<?>>();

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <RoleType> List<? extends RoleType> findRoles (final @Nonnull Object owner,
                                                          final @Nonnull Class<RoleType> roleClass)
      {
        log.trace("findRoles({}, {})", owner, roleClass);
        final Class<?> ownerClass = findClass(owner);
        final List<RoleType> roles = new ArrayList<RoleType>();
        final List<Class<? extends RoleType>> roleImplementations = findRoleImplementationsFor(ownerClass, roleClass);

outer:  for (final Class<? extends RoleType> roleImplementationClass : roleImplementations)
          {
            // FIXME: why enumerating all constructors?
            for (final Constructor<?> constructor : roleImplementationClass.getDeclaredConstructors())
              {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                final Class<?> contextClass = roleImplementationClass.getAnnotation(DciRole.class).context();

                Object context = null;

                if (contextClass != DciRole.NoContext.class)
                  {
                    log.trace(">>>> contexts: {}", contextManager.getContexts());

                    try
                      {
                        context = contextManager.findContext(contextClass);
                      }
                    catch (NotFoundException e)
                      {
                        log.trace(">>>> role {} discarded, can't find context: {}", roleImplementationClass, contextClass);
                        continue outer;
                      }
                  }

                if (parameterTypes.length > 0)
                  {
                    try
                      {
                        final List<Object> parameters = new ArrayList<Object>();

                        for (Class<?> parameterType : parameterTypes)
                          {
                            if (parameterType.isAssignableFrom(ownerClass))
                              {
                                parameters.add(owner);
                              }
                            // TODO: strict equals or isAssignableFrom?
                            else if (parameterType.equals(contextClass))
                              {
                                parameters.add(context);
                              }
                            else // standard injection
                              {
                                parameters.add(getBean(parameterType));
                              }
                          }

                        roles.add(roleClass.cast(constructor.newInstance(parameters.toArray())));
                      }
                    catch (Exception e)
                      {
                        log.error("", e);
                      }
                  }
              }
          }

        log.trace(">>>> returning: {}", roles);

        return roles;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private synchronized <RT> List<Class<? extends RT>> findRoleImplementationsFor (final Class<?> ownerClass,
                                                                                    final Class<RT> roleClass)
      {
        final ClassAndRole classAndRole = new ClassAndRole(ownerClass, roleClass);
        final List<Class<?>> implementations = roleMapByOwnerClass.get(classAndRole);

        if (implementations != null)
          {
            return (List)implementations;
          }

        for (final ClassAndRole classAndRole1 : classAndRole.getSuper())
          {
            final List<Class<?>> implementations2 = roleMapByOwnerClass.get(classAndRole1);

            if (implementations2 != null)
              {
                for (final Class<?> implementation : implementations2)
                  {
                    roleMapByOwnerClass.add(classAndRole, implementation);
                  }

                return (List)implementations2;
              }
          }

        return Collections.emptyList();
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    protected void scan (final @Nonnull Collection<Class<?>> roleImplementationClasses)
      {
        for (final Class<?> roleImplementationClass : roleImplementationClasses)
          {
            final DciRole role = roleImplementationClass.getAnnotation(DciRole.class);

            for (final Class<?> datumClass : role.datumType())
              {
                for (final Class<?> roleClass : findAllImplemetedInterfacesOf(roleImplementationClass))
                  {
                    roleMapByOwnerClass.add(new ClassAndRole(datumClass, roleClass), roleImplementationClass);
                  }
              }
          }

        logRoles();
      }

    /*******************************************************************************************************************
     *
     * Finds all the interfaces implemented by a given class, including those eventually implemented by superclasses.
     *
     * @param  clazz    the class to inspect
     * @return          the implemented interfaces
     *
     ******************************************************************************************************************/
    @Nonnull
    private static SortedSet<Class<?>> findAllImplemetedInterfacesOf (final @Nonnull Class<?> clazz)
      {
        final SortedSet<Class<?>> interfaces = new TreeSet<Class<?>>(CLASS_COMPARATOR);
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));

        if (clazz.getSuperclass() != null)
          {
            interfaces.addAll(findAllImplemetedInterfacesOf(clazz.getSuperclass()));
          }

        return interfaces;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract <T> T getBean (final @Nonnull Class<T> beanType);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void logRoles()
      {
        log.debug("Configured roles:");

        for (final Entry<ClassAndRole, List<Class<?>>> entry : roleMapByOwnerClass.entrySet())
          {
            log.debug(">>>> {} -> {}", entry.getKey(), entry.getValue());
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
    private Class<?> findClass (final @Nonnull Object owner)
      {
        Class<?> ownerClass = owner.getClass();

        if (ownerClass.toString().contains("EnhancerByMockito"))
          {
            log.trace(">>>> owner is a mock {} implementing {}", ownerClass, ownerClass.getInterfaces());
            ownerClass = ownerClass.getInterfaces()[0]; // 1st is the original class, 2nd is CGLIB proxy
            log.trace(">>>> owner class replaced with {}", ownerClass);
          }

        return ownerClass;
      }
  }
