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
package it.tidalwave.util.test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A facility that provides some common tasks for testing, such as creating a Spring context and manipulating files.
 *
 * @author  Fabrizio Giudici
 * @since 3.2-ALPHA-18
 *
 **********************************************************************************************************************/
@Slf4j
public class SpringTestHelper extends BaseTestHelper
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    public SpringTestHelper (@Nonnull final Object test)
      {
        super(test);
      }

    /*******************************************************************************************************************
     *
     * Creates a Spring context configured with the given files. A further configuration file is appended, named
     * {@code test-class-simple-name/TestBeans.xml}.
     *
     * @param   configurationFiles  the configuration files
     * @return                      the Spring {@link ApplicationContext}
     *
     ******************************************************************************************************************/
    @Nonnull
    public ApplicationContext createSpringContext (@Nonnull final String ... configurationFiles)
      {
        return createSpringContext(Collections.emptyMap(), configurationFiles);
      }

    /*******************************************************************************************************************
     *
     * Creates a Spring context configured with the given files. A further configuration file is appended, named
     * {@code test-class-simple-name/TestBeans.xml}.
     *
     * @param   properties          the properties
     * @param   configurationFiles  the configuration files
     * @return                      the Spring {@link ApplicationContext}
     *
     ******************************************************************************************************************/
    @Nonnull
    public ApplicationContext createSpringContext (@Nonnull final Map<String, Object> properties,
                                                   @Nonnull final String ... configurationFiles)
      {
        return createSpringContext(properties, context -> {}, new ArrayList<>(List.of(configurationFiles)));
      }

    /*******************************************************************************************************************
     *
     * Creates a Spring context configured with the given files. A further configuration file is appended, named
     * {@code test-class-simple-name/TestBeans.xml}.
     *
     * @param   configurationFiles  the configuration files
     * @param   modifier            a processor to modify the contents of the context
     * @return                      the Spring {@link ApplicationContext}
     *
     ******************************************************************************************************************/
    @Nonnull
    public ApplicationContext createSpringContext (@Nonnull final Consumer<? super GenericApplicationContext> modifier,
                                                   @Nonnull final String ... configurationFiles)
      {
        return createSpringContext(Collections.emptyMap(), modifier, configurationFiles);
      }

    /*******************************************************************************************************************
     *
     * Creates a Spring context configured with the given files. A further configuration file is appended, named
     * {@code test-class-simple-name/TestBeans.xml}.
     *
     * @param   properties          the properties
     * @param   modifier            a processor to modify the contents of the context
     * @param   configurationFiles  the configuration files
     * @return                      the Spring {@link ApplicationContext}
     *
     ******************************************************************************************************************/
    @Nonnull
    public ApplicationContext createSpringContext (@Nonnull final Map<String, Object> properties,
                                                   @Nonnull final Consumer<? super GenericApplicationContext> modifier,
                                                   @Nonnull final String ... configurationFiles)
      {
        return createSpringContext(properties, modifier, new ArrayList<>(List.of(configurationFiles)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private ApplicationContext createSpringContext (@Nonnull final Map<String, Object> properties,
                                                    @Nonnull final Consumer<? super GenericApplicationContext> modifier,
                                                    @Nonnull final Collection<? super String> configurationFiles)
      {
        configurationFiles.add(test.getClass().getSimpleName() + "/TestBeans.xml");

        final var environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", properties));
        final var context = new GenericXmlApplicationContext();
        context.setEnvironment(environment);
        context.load(configurationFiles.toArray(new String[0]));
        modifier.accept(context);
        context.refresh();
        log.info("Beans: {}", List.of(context.getBeanFactory().getBeanDefinitionNames()));

        return context;
      }
  }
