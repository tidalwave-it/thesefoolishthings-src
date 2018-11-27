/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.actor;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import it.tidalwave.actor.annotation.Message;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

/***********************************************************************************************************************
 *
 * This message notifies that a new {@link Collaboration} has been started.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Message @Immutable
@EqualsAndHashCode(callSuper = true) @ToString(callSuper = false)
public class CollaborationStartedMessage extends MessageSupport
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private CollaborationStartedMessage (final @Nonnull Collaboration collaboration)
      {
        super(collaboration);
      }

    /*******************************************************************************************************************
     *
     * Creates a new instance for the given {@link Collaboration}.
     *
     * @param  collaboration    the {@code Collaboration}
     * @return                  the new instance
     *
     ******************************************************************************************************************/
    @Nonnull
    public static CollaborationStartedMessage forCollaboration (final @Nonnull Collaboration collaboration)
      {
        return new CollaborationStartedMessage(collaboration);
      }

    /*******************************************************************************************************************
     *
     * Returns the time when the {@link Collaboration} has been started.
     *
     * @return    the start time
     *
     ******************************************************************************************************************/
    @Nonnull
    public DateTime getStartTime()
      {
        return collaboration.getStartTime();
      }
  }
