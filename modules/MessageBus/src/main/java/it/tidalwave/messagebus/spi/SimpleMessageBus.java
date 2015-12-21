/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
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
package it.tidalwave.messagebus.spi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBus.Listener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A partial implementation of {@link MessageBus}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ThreadSafe @Slf4j
public class SimpleMessageBus implements MessageBus
  {
    private final Map<Class<?>, List<WeakReference<Listener<?>>>> listenersMapByTopic = new HashMap<>();

    private final MessageDelivery messageDelivery;
    
    @Getter
    private final Executor executor;
    
    /*******************************************************************************************************************
     *
     * 
     *
     ******************************************************************************************************************/
    public SimpleMessageBus() 
      {
        this(Executors.newFixedThreadPool(10));
      }

    /*******************************************************************************************************************
     *
     * 
     *
     ******************************************************************************************************************/
    public SimpleMessageBus (final @Nonnull Executor executor)
      {
        this(executor, new SimpleAsyncMessageDelivery());
      }
    
    /*******************************************************************************************************************
     *
     * 
     *
     ******************************************************************************************************************/
    public SimpleMessageBus (final @Nonnull Executor executor, final @Nonnull MessageDelivery messageDelivery)
      {
        this.executor = executor;
        this.messageDelivery = messageDelivery;
        this.messageDelivery.initialize(this);
        log.info("MessageBusSupport configured with {}", messageDelivery);
      }
     
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Topic message)
      {
        publish((Class<Topic>)message.getClass(), message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Class<Topic> topic, final @Nonnull Topic message)
      {
        log.trace("publish({}, {})", topic, message);
        messageDelivery.deliverMessage(topic, message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void subscribe (final @Nonnull Class<Topic> topic, final @Nonnull Listener<Topic> listener)
      {
        log.debug("subscribe({}, {})", topic, listener);
        findListenersByTopic(topic).add(new WeakReference<>(listener));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void unsubscribe (final @Nonnull Listener<?> listener)
      {
        log.debug("unsubscribe({})", listener);

        for (final List<WeakReference<Listener<?>>> list : listenersMapByTopic.values())
          {
            for (final Iterator<WeakReference<Listener<?>>> i = list.iterator(); i.hasNext(); )
              {
                final WeakReference<?> ref = i.next();

                if ((ref.get() == null) || (ref.get() == listener))
                  {
                    i.remove();
                  }
              }
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    protected <TOPIC> void dispatchMessage (final @Nonnull Class<TOPIC> topic, final @Nonnull TOPIC message)
      {
        final HashSet<Map.Entry<Class<?>, List<WeakReference<MessageBus.Listener<?>>>>> clone =
                new HashSet<>(listenersMapByTopic.entrySet());

        for (final Map.Entry<Class<?>, List<WeakReference<MessageBus.Listener<?>>>> e : clone)
          {
            if (e.getKey().isAssignableFrom(topic))
              {
                final List<WeakReference<MessageBus.Listener<TOPIC>>> listeners = (List)e.getValue();

                for (final WeakReference<MessageBus.Listener<TOPIC>> listenerReference : listeners)
                  {
                    final MessageBus.Listener<TOPIC> listener = listenerReference.get();

                    if (listener != null)
                      {
                        try
                          {
                            listener.notify(message);
                          }
                        catch (Throwable t)
                          {
                            log.warn("deliverMessage()", t);
                          }
                      }
                  }
              }
          }
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private <Topic> List<WeakReference<Listener<Topic>>> findListenersByTopic (final @Nonnull Class<Topic> topic)
      {
        final List tmp = listenersMapByTopic.get(topic);
        List<WeakReference<Listener<Topic>>> listeners = tmp;

        if (listeners == null)
          {
            listeners = new ArrayList<>();
            listenersMapByTopic.put(topic, (List)listeners);
          }

        return listeners;
      }
  }
