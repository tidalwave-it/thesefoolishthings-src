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
package it.tidalwave.messagebus.impl.spring;

import java.lang.reflect.Method;
import jakarta.annotation.Nonnull;
import it.tidalwave.util.annotation.VisibleForTesting;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.MessageBusHelper.MethodAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class MessageBusAdapterFactory implements MessageBusHelper.Adapter
  {
    @Nonnull
    private final MessageBus messageBus;

    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    @Getter @VisibleForTesting @ToString(of = "method")
    class MessageBusListenerAdapter<T> implements MethodAdapter<T>, MessageBus.Listener<T>
      {
        @Nonnull
        private final Object owner;

        @Nonnull
        private final Method method;

        @Nonnull
        private final Class<T> topic;

        public MessageBusListenerAdapter (@Nonnull final Object owner,
                                          @Nonnull final Method method,
                                          @Nonnull final Class<T> topic)
          {
            this.owner  = owner;
            this.method = method;
            this.topic  = topic;
            method.setAccessible(true);
          }

        @Override
        public void notify (@Nonnull final T message)
          {
            log.trace("notify({})", message);

            try
              {
                method.invoke(owner, message);
              }
            catch (Throwable t)
              {
                log.error("Error calling {} with {}", method, message.getClass());
                log.error("", t);
              }
          }

        @Override
        public void subscribe()
          {
            messageBus.subscribe(topic, this);
          }

        @Override
        public void unsubscribe()
          {
            messageBus.unsubscribe(this);
          }
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public <T> MethodAdapter<T> createMethodAdapter (@Nonnull final Object owner,
                                                     @Nonnull final Method method,
                                                     @Nonnull final Class<T> topic)
      {
        return new MessageBusListenerAdapter<>(owner, method, topic);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void publish (@Nonnull final Object message)
      {
        messageBus.publish(message);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public <T> void publish (@Nonnull final Class<T> topic, @Nonnull final T message)
      {
        messageBus.publish(topic, message);
      }
  }
