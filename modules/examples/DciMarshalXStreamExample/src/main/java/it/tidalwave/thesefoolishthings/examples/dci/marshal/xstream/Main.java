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
package it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream;

import javax.annotation.Nonnull;

import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.RoleManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Main
  {
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

    @Bean
    public DciMarshalXStreamExample example()
      {
        return new DciMarshalXStreamExample();
      }

    public static void main (@Nonnull final String ... args)
      throws Exception
      {
        final BeanFactory context = new AnnotationConfigApplicationContext(Main.class);
        context.getBean(DciMarshalXStreamExample.class).run();
      }
  }
