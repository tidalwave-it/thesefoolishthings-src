/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.RoleManager;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Configuration
public class RoleSpringConfiguration
  {
    /** The path of the Spring configuration supporting roles to pass e.g. to a
        @code ClassPathXmlApplicationContext}. */
    public static final String BEANS = "classpath*:/META-INF/SpringRoleAutoBeans.xml";

    @Bean
    public RoleManager roleManager()
      {
        return RoleManager.Locator.find();
      }

    @Bean
    public ContextManager contextManager()
      {
        return ContextManager.Locator.find();
      }
  }
