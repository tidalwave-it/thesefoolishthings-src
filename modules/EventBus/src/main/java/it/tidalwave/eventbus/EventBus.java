/***********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://northernwind.java.net
 * SCM: http://java.net/hg/northernwind~src
 *
 **********************************************************************************************************************/
package it.tidalwave.eventbus;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * A simple event bus for a local publish/subscribe facility.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface EventBus
  {
    /*******************************************************************************************************************
     *
     * Publishes the given event. The topic is the class of the event.
     * 
     * @param  event  the event
     *
     ******************************************************************************************************************/
    public <Topic> void publish (@Nonnull Topic event);
    
    /*******************************************************************************************************************
     *
     * Publishes the given event and topic. Passing an explicit topic can be useful when dealing with a hierarchy of
     * events (so, perhaps a subclass is passed but the topic is the root of the hierarchy).
     * 
     * @param  topic  the topic
     * @param  event  the event
     *
     ******************************************************************************************************************/
    public <Topic> void publish (@Nonnull Class<Topic> topic, @Nonnull Topic event);
    
    /*******************************************************************************************************************
     *
     * Subscribes an {@link EventBusListener} to a topic.
     * 
     * @param  topic     the topic
     * @param  listener  the listener
     *
     ******************************************************************************************************************/
    public <Topic> void subscribe (@Nonnull Class<Topic> topic, @Nonnull EventBusListener<Topic> listener);
  }
