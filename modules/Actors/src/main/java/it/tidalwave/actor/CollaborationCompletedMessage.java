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
import org.joda.time.Duration;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Message @Immutable 
@EqualsAndHashCode @ToString(callSuper=false)
public class CollaborationCompletedMessage extends MessageSupport
  {
    private final long endTime = System.currentTimeMillis();
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    private CollaborationCompletedMessage (final @Nonnull Collaboration collaboration)
      {
        super(collaboration);
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static CollaborationCompletedMessage forCollaboration (final @Nonnull Collaboration collaboration)
      {
        return new CollaborationCompletedMessage(collaboration);
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
    
    /*******************************************************************************************************************
     * 
     * Returns the time when this unit of work has been completed.
     * 
     ******************************************************************************************************************/
    @Nonnull
    public DateTime getEndTime()
      {
        return new DateTime(endTime);
      }
    
    /*******************************************************************************************************************
     * 
     * Returns the time this unit of work took to complete.
     * 
     ******************************************************************************************************************/
    @Nonnull
    public Duration getDuration()
      {
        return new Duration(getStartTime().getMillis(), endTime);
      }
  }
