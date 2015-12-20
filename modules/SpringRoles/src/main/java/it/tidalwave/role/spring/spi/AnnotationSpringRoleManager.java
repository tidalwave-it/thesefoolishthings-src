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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.beans.factory.BeanFactory;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spring.ClassScanner;
import it.tidalwave.role.spi.RoleManagerSupport;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.dci.annotation.DciRole;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A specialization of {@link RoleManagerSupport} for a Spring context that uses annotations ({@link DciRole} and
 * {@link DciContext}) for retrieving role metadata and is capable to inject Spring beans into created roles.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class AnnotationSpringRoleManager extends RoleManagerSupport
  {
    @Inject @Nonnull
    private BeanFactory beanFactory;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @PostConstruct
    /* package */ void initialize()
      {
        scan(new ClassScanner().withAnnotationFilter(DciRole.class).findClasses());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected <T> T getBean (final @Nonnull Class<T> beanType)
      {
        return beanFactory.getBean(beanType);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected Class<?> findContextForRole (final @Nonnull Class<?> roleImplementationClass)
      throws NotFoundException
      {
        final Class<?> contextClass = roleImplementationClass.getAnnotation(DciRole.class).context();

        if (contextClass == DciRole.NoContext.class)
          {
            throw new NotFoundException("No context");
          }

        return contextClass;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected Class<?>[] findDatumTypesForRole (final @Nonnull Class<?> roleImplementationClass)
      {
        return roleImplementationClass.getAnnotation(DciRole.class).datumType();
      }
  }
