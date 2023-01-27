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
package it.tidalwave.role.impl;

import javax.annotation.Nonnull;
import java.util.ServiceLoader;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import it.tidalwave.util.LazySupplier;
import it.tidalwave.role.spi.annotation.DefaultProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceLoaderLocator
  {

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
            log.info("Too many instances of {}, ignoring default providers ...", serviceClassName);
            providers = providers.stream()
                                 .filter(p -> p.getClass().getAnnotation(DefaultProvider.class) == null)
                                 .collect(toList());
          }

        if (providers.size() > 1)
          {
            throw new RuntimeException("Too many instances of " + serviceClassName + " : " + providers);
          }

        final var provider = providers.get(0);
        log.info("{} instantiated from META-INF/services: {}", serviceClassName, provider);
        return provider;
      }

    @Nonnull
    public static <T> LazySupplier<T> lazySupplierOf (@Nonnull final Class<? extends T> clazz)
      {
        return LazySupplier.of(() -> ServiceLoaderLocator.findService(clazz));
      }
  }
