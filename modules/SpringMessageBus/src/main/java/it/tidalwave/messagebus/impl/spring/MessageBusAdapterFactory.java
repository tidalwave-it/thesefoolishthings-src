/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus.impl.spring;

import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.MessageBusHelper.MethodAdapter;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class MessageBusAdapterFactory implements MessageBusHelper.Adapter
  {
    @Nonnull
    private final MessageBus messageBus;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Getter /* visible for testing */ @ToString(of = "method")
    class MessageBusListenerAdapter<Topic> implements MethodAdapter<Topic>, MessageBus.Listener<Topic>
      {
        @Nonnull
        private final Object owner;

        @Nonnull
        private final Method method;

        @Nonnull
        private final Class<Topic> topic;

        public MessageBusListenerAdapter (final @Nonnull Object owner,
                                          final @Nonnull Method method,
                                          final @Nonnull Class<Topic> topic)
          {
            this.owner  = owner;
            this.method = method;
            this.topic  = topic;
            method.setAccessible(true);
          }

        @Override
        public void notify (final @Nonnull Topic message)
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

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <Topic> MethodAdapter createMethodAdapter (final @Nonnull Object owner,
                                                      final @Nonnull Method method,
                                                      final @Nonnull Class<Topic> topic)
      {
        return new MessageBusListenerAdapter(owner, method, topic);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void publish (final @Nonnull Object message)
      {
        messageBus.publish(message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Class<Topic> topic, final @Nonnull Topic message)
      {
        messageBus.publish(topic, message);
      }
  }
