/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.messagebus;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * A simple message bus for a local publish/subscribe facility.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface MessageBus
  {
    /*******************************************************************************************************************
     *
     * A listener to receive notifications from a {@link MessageBus}.
     * 
     * @stereotype  Listener
     * 
     ******************************************************************************************************************/
    public static interface Listener<Topic>
      {
        /***************************************************************************************************************
         *
         * Notifies the reception of the given message.
         * 
         * @param  message  the event
         *
         **************************************************************************************************************/
        public void notify (@Nonnull Topic message);  
      }
    
    /*******************************************************************************************************************
     *
     * Publishes the given event. The topic is the class of the event.
     * 
     * @param  message  the event
     *
     ******************************************************************************************************************/
    public <Topic> void publish (@Nonnull Topic message);
    
    /*******************************************************************************************************************
     *
     * Publishes the given event and topic. Passing an explicit topic can be useful when dealing with a hierarchy of
     * events (so, perhaps a subclass is passed but the topic is the root of the hierarchy).
     * 
     * @param  topic  the topic
     * @param  message  the event
     *
     ******************************************************************************************************************/
    public <Topic> void publish (@Nonnull Class<Topic> topic, @Nonnull Topic message);
    
    /*******************************************************************************************************************
     *
     * Subscribes a {@link Listener} to a topic.
     * 
     * @param  topic     the topic
     * @param  listener  the listener
     *
     ******************************************************************************************************************/
    public <Topic> void subscribe (@Nonnull Class<Topic> topic, @Nonnull Listener<Topic> listener);
    
    /*******************************************************************************************************************
     *
     * Unsubscribes a {@link Listener}.
     * 
     * @param  listener  the listener
     *
     ******************************************************************************************************************/
    public void unsubscribe (@Nonnull Listener<?> listener);
  }
