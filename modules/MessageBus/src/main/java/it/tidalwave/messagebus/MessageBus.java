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
package it.tidalwave.messagebus;

import jakarta.annotation.Nonnull;

/***************************************************************************************************************************************************************
 *
 * A simple message bus for a local publish/subscribe facility.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public interface MessageBus
  {
    /***********************************************************************************************************************************************************
     * A listener to receive notifications from a {@link MessageBus}.
     *
     * @stereotype  Listener
     **********************************************************************************************************************************************************/
    public static interface Listener<T>
      {
        /***************************************************************************************************************
         *
         * Notifies the reception of the given message.
         *
         * @param  message  the event
         *
         **************************************************************************************************************/
        public void notify (@Nonnull T message);
      }

    /***********************************************************************************************************************************************************
     * Publishes the given event. The topic is the class of the event.
     *
     * @param  <T>      the static type of the topic
     * @param  message  the event
     **********************************************************************************************************************************************************/
    public <T> void publish (@Nonnull T message);

    /***********************************************************************************************************************************************************
     * Publishes the given message and topic. Passing an explicit topic can be useful when dealing with a hierarchy of
     * events (so, perhaps a subclass is passed but the topic is the root of the hierarchy).
     *
     * @param  <T>      the static type of the topic
     * @param  topic    the topic
     * @param  message  the message
     **********************************************************************************************************************************************************/
    public <T> void publish (@Nonnull Class<T> topic, @Nonnull T message);

    /***********************************************************************************************************************************************************
     * Subscribes a {@link Listener} to a topic.
     *
     * @param  <T>       the static type of the topic
     * @param  topic     the topic
     * @param  listener  the listener
     **********************************************************************************************************************************************************/
    public <T> void subscribe (@Nonnull Class<T> topic, @Nonnull Listener<T> listener);

    /***********************************************************************************************************************************************************
     * Unsubscribes a {@link Listener}.
     *
     * @param  listener  the listener
     **********************************************************************************************************************************************************/
    public void unsubscribe (@Nonnull Listener<?> listener);
  }
