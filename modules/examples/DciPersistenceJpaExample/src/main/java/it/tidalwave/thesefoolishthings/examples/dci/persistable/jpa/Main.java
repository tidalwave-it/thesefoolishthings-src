/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa;

import javax.annotation.Nonnull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import it.tidalwave.role.spring.RoleSpringConfiguration;
import it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.role.impl.JpaPersistenceContext;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@SpringBootApplication
@EntityScan(basePackages = "it.tidalwave")
public class Main
  {
    @Bean
    public JpaPersistenceContext jpaPersistenceContext()
      {
        return new JpaPersistenceContext();
      }

    @Bean
    public DciPersistenceJpaExample example()
      {
        return new DciPersistenceJpaExample();
      }

    @Bean
    public TransactionalProcessor transactionalProcessor()
      {
        return new TransactionalProcessor();
      }

    public static void main (@Nonnull final String ... args)
      {
        // See https://github.com/spring-projects/spring-boot/issues/12649#issuecomment-1269568055
        System.setProperty("org.springframework.boot.logging.LoggingSystem", "none");
        SpringApplication.run(new Class[] { RoleSpringConfiguration.class, Main.class }, args);
      }
  }
