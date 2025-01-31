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
package it.tidalwave.actor.spi;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Slf4j
public class ActorGroupActivator
  {
    private final List<ActorActivator> actorActivators = new ArrayList<>();

    public void add (@Nonnull final ActorActivator actorActivator)
      {
        actorActivators.add(actorActivator);
      }

    public void activate()
      {
        for (final var actorActivator : actorActivators)
          {
            actorActivator.initialize();
            log.info(">>>> activated {}", actorActivator.getActorObject());
          }
      }

    public void deactivate()
      {
        for (final var actorActivator : actorActivators)
          {
            actorActivator.dispose();
            log.info(">>>> deactivated {}", actorActivator.getActorObject());
          }
      }
  }
