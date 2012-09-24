/***********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://solidblue.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/solidblue-src
 *
 **********************************************************************************************************************/
package it.tidalwave.actor;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import it.tidalwave.actor.annotation.Message;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.DateTime;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Message @Immutable 
@EqualsAndHashCode(callSuper=true) @ToString(callSuper=false)
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
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static CollaborationStartedMessage forCollaboration (final @Nonnull Collaboration collaboration)
      {
        return new CollaborationStartedMessage(collaboration);
      }
    
    /*******************************************************************************************************************
     * 
     * Returns the time when this unit of work has been created.
     * 
     ******************************************************************************************************************/
    @Nonnull
    public DateTime getStartTime()
      {
        return collaboration.getStartTime();
      }
  }