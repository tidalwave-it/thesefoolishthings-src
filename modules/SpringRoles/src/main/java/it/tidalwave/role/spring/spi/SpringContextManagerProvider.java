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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.ContextManagerProvider;
import org.springframework.beans.factory.annotation.Configurable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * Since the whole library must be independent of Spring or any other DI framework, the {@link ContextManager} is always
 * retrieved by means of {@link ContextManagerProvider} that, in turn, searches for a {@code META-INF/services}
 * registered class. When using Spring this is the registered class, that returns a Spring bean injected by means of
 * Aspect-J.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// Registered in META-INF/services
@Configurable(preConstruction = true) @Slf4j
public class SpringContextManagerProvider implements ContextManagerProvider
  {
    @Getter @Inject @Nonnull
    private ContextManager contextManager;

    public SpringContextManagerProvider()
      {
        Objects.requireNonNull(contextManager, "ContextManager not injected");
        log.info("Initialized - contextManager: {}", contextManager);
      }
  }
