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
package it.tidalwave.role.impl;

import javax.annotation.Nonnull;
import java.util.ServiceLoader;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.tidalwave.util.LazySupplier;
import it.tidalwave.util.PreferencesHandler;
import it.tidalwave.role.spi.annotation.DefaultProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// PreferencesHandler can be used to programmatically set the log folder, so don't inject logger
public class ServiceLoaderLocator
  {
    public static final String PROPS_BASE_NAME = PreferencesHandler.class.getPackage().getName();

    /** Suppress any console output. @since 3.2-ALPHA-21 */
    public static final String PROP_SUPPRESS_CONSOLE = PROPS_BASE_NAME + ".suppressConsoleOutput";

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> T findService (@Nonnull final Class<? extends T> serviceClass)
      {
        final var serviceClassName = serviceClass.getSimpleName();
        final var classLoader = Thread.currentThread().getContextClassLoader();
        final var iterator = ServiceLoader.load(serviceClass, classLoader).iterator();
        final var spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        var providers = StreamSupport.stream(spliterator, false).collect(toList());

        if (providers.isEmpty())
          {
            throw new RuntimeException("No ServiceProvider for " + serviceClassName);
          }

        if (providers.size() > 1) // filter out the default one
          {
            getLogger().info("Too many instances of {}, ignoring default providers ...", serviceClassName);
            providers = providers.stream()
                                 .filter(p -> p.getClass().getAnnotation(DefaultProvider.class) == null)
                                 .collect(toList());
          }

        if (providers.size() > 1)
          {
            throw new RuntimeException("Too many instances of " + serviceClassName + " : " + providers);
          }

        final var provider = providers.get(0);
        
        // PreferencesHandler can be used to programmatically set the log folder, so don't log yet
        if (!serviceClass.equals(PreferencesHandler.class))
          {
            getLogger().info("{} instantiated from META-INF/services: {}", serviceClassName, provider);
          }
        else if (!Boolean.getBoolean(PROP_SUPPRESS_CONSOLE))
          {
            System.out.printf("%s instantiated from META-INF/services: %s\n", serviceClassName, provider);
          }

        return provider;
      }

    @Nonnull
    public static <T> LazySupplier<T> lazySupplierOf (@Nonnull final Class<? extends T> clazz)
      {
        return LazySupplier.of(() -> ServiceLoaderLocator.findService(clazz));
      }

    @Nonnull
    private static Logger getLogger()
      {
        return LoggerFactory.getLogger(ServiceLoaderLocator.class);
      }
  }
