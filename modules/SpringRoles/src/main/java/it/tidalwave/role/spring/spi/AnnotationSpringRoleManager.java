/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role.spring.spi;

import it.tidalwave.role.ContextRunner;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.spring.ClassScanner;
import it.tidalwave.role.annotation.RoleFor;
import it.tidalwave.role.spi.RoleManager;
import it.tidalwave.util.NotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class AnnotationSpringRoleManager implements RoleManager
  {
    @Inject @Nonnull
    private BeanFactory beanFactory;
    
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
    
    private MultiValueMap<ClassAndRole, Class<?>> roleMapByOwnerClass = new LinkedMultiValueMap<ClassAndRole, Class<?>>();
   
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <RoleType> List<? extends RoleType> findRoles (final @Nonnull Object owner, final @Nonnull Class<RoleType> roleClass)
      {
        log.trace("findRoles({}, {})", owner, roleClass);
        
        final Class<?> ownerClass = owner.getClass();
        final List<RoleType> roles = new ArrayList<RoleType>();
        
outer:  for (final Class<? extends RoleType> roleImplementationClass : findRoleImplementationsFor(ownerClass, roleClass))
          {
            for (final Constructor<?> constructor : roleImplementationClass.getDeclaredConstructors())
              {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                final Class<?> contextClass = roleImplementationClass.getAnnotation(RoleFor.class).context();
                
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
                            else if (parameterType.equals(contextClass))
                              {
                                try
                                  {
                                    parameters.add(ContextRunner.findContext(parameterType));
                                  }
                                catch (NotFoundException e)
                                  {
                                    try
                                      {
                                        parameters.add(beanFactory.getBean(parameterType));
                                      }
                                    catch (BeanCreationException e2) // couldn't satisfy the context
                                      {
                                        log.debug("Role discarded", e2);
                                        continue outer;
                                      }
                                  }
                              }
                            else
                              {
                                parameters.add(beanFactory.getBean(parameterType));
                              }
                          }
                        
                        roles.add((RoleType)constructor.newInstance(parameters.toArray()));
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
    private synchronized <RoleType> List<Class<? extends RoleType>> findRoleImplementationsFor (final Class<?> ownerClass, final Class<RoleType> roleClass)
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
    @PostConstruct
    /* package */ void initialize()
      {
        final ClassScanner classScanner = new ClassScanner().withAnnotationFilter(RoleFor.class);
        
        for (final Class<?> roleImplementationClass : classScanner.findClasses())
          {
            final RoleFor role = roleImplementationClass.getAnnotation(RoleFor.class);
            final Class<?> datumClass = role.datum();
            final Class<?> contextClass = role.context();
            
            for (final Class<?> roleClass : roleImplementationClass.getInterfaces())
              {
                roleMapByOwnerClass.add(new ClassAndRole(datumClass, roleClass), roleImplementationClass);
              }
          }
        
        logRoles();
      }
        
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
  }