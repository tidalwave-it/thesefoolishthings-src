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
package it.tidalwave.messagebus.netbeans.platformx.eventbus;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.MessageBusHelper.MethodAdapter;
import org.netbeans.platformx.eventbus.api.EventBus;
import org.netbeans.platformx.eventbus.api.EventBusListener;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE) @Slf4j
public class PlatformXEventBusAdapter implements MessageBusHelper.Adapter
  {
    public static final MessageBusHelper.Adapter INSTANCE = new PlatformXEventBusAdapter();
    public static final MessageBusHelper.Adapter PLATFORMX_EVENTBUS_ADAPTER = INSTANCE;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    class EventBusListenerAdapter<Topic> implements MethodAdapter<Topic>, EventBusListener<Topic>
      {
        @Nonnull
        private final Object owner;

        @Nonnull
        private final Method method;

        @Nonnull
        private final Class<Topic> topic;

        public EventBusListenerAdapter (final @Nonnull Object owner,
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
            getEventBus().subscribe(topic, this);
          }

        @Override
        public void unsubscribe()
          {
            getEventBus().unsubscribe(topic, this);
          }
      }

    private EventBus eventBus;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <Topic> MethodAdapter<Topic> createMethodAdapter (final @Nonnull Object object,
                                                             final @Nonnull Method method,
                                                             final @Nonnull Class<Topic> topic)
      {
        return new EventBusListenerAdapter<Topic>(object, method, topic);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void publish (final @Nonnull Object message)
      {
        getEventBus().publish(message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Class<Topic> topic, final @Nonnull Topic message)
      {
        if (!message.getClass().equals(topic))
          {
            final String m = "EventBus doesn't support publishing with a different class than the topic";
            throw new IllegalArgumentException(m);
          }

        getEventBus().publish(message);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private synchronized EventBus getEventBus() // cannot use OpenBlueSky Locator because of circular dependencies
      {
        if (eventBus == null)
          {
            eventBus = EventBus.getDefault();
          }

        return eventBus;
      }
  }
