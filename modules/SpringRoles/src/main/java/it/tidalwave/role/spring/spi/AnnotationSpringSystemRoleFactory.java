/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.spring.ClassScanner;
import it.tidalwave.role.spi.SystemRoleFactorySupport;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.dci.annotation.DciRole;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A specialization of {@link SystemRoleFactorySupport} for a Spring context that uses annotations ({@link DciRole} and
 * {@link DciContext}) for retrieving role metadata and is capable to inject Spring beans into created roles.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j @Configurable
public class AnnotationSpringSystemRoleFactory extends SystemRoleFactorySupport
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
        log.debug("scanning classes with {} annotation...", DciRole.class);
        scan(new ClassScanner().withAnnotationFilter(DciRole.class).findClasses());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @SuppressWarnings("BoundedWildcard")
    @Override @Nonnull
    protected <T> Optional<T> getBean (@Nonnull final Class<T> beanType)
      {
        return Optional.ofNullable(beanFactory.getBean(beanType));
      }
  }
