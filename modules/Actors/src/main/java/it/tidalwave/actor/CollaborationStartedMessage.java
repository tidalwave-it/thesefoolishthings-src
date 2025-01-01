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
package it.tidalwave.actor;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.time.ZonedDateTime;
import it.tidalwave.actor.annotation.Message;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/***************************************************************************************************************************************************************
 *
 * This message notifies that a new {@link Collaboration} has been started.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Message @Immutable @EqualsAndHashCode(callSuper = true) @ToString
public class CollaborationStartedMessage extends MessageSupport
  {
    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    private CollaborationStartedMessage (@Nonnull final Collaboration collaboration)
      {
        super(collaboration);
      }

    /***********************************************************************************************************************************************************
     * Creates a new instance for the given {@link Collaboration}.
     *
     * @param  collaboration    the {@code Collaboration}
     * @return                  the new instance
     **********************************************************************************************************************************************************/
    @Nonnull
    public static CollaborationStartedMessage forCollaboration (@Nonnull final Collaboration collaboration)
      {
        return new CollaborationStartedMessage(collaboration);
      }

    /***********************************************************************************************************************************************************
     * Returns the time when the {@link Collaboration} has been started.
     *
     * @return    the start time
     **********************************************************************************************************************************************************/
    @Nonnull
    public ZonedDateTime getStartTime()
      {
        return collaboration.getStartTime();
      }
  }
