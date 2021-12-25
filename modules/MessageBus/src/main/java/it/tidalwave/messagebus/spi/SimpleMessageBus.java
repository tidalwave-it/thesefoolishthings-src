/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.messagebus.spi;

import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import it.tidalwave.messagebus.MessageBus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A partial implementation of {@link MessageBus}.
 *
 * @author  Fabrizio Giudici
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
     * Creates a new instance with a {@link SimpleAsyncMessageDelivery} strategy for delivery. It will use its own
     * thread pool.
     *
     ******************************************************************************************************************/
    public SimpleMessageBus() 
      {
        this(Executors.newFixedThreadPool(10));
      }

    /*******************************************************************************************************************
     *
     * Creates a new instance given an executor and a {@link SimpleAsyncMessageDelivery} strategy for delivery.
     *
     * @param   executor          the {@link Executor}
     *
     ******************************************************************************************************************/
    public SimpleMessageBus (@Nonnull final Executor executor)
      {
        this(executor, new SimpleAsyncMessageDelivery());
      }
    
    /*******************************************************************************************************************
     *
     * Creates a new instance given an executor and a strategy for delivery.
     *
     * @param   executor          the {@link Executor}
     * @param   messageDelivery   the strategy for delivery
     *
     ******************************************************************************************************************/
    public SimpleMessageBus (@Nonnull final Executor executor, @Nonnull final MessageDelivery messageDelivery)
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
    public <TOPIC> void publish (@Nonnull final TOPIC message)
      {
        publish((Class<TOPIC>)message.getClass(), message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <TOPIC> void publish (@Nonnull final Class<TOPIC> topic, @Nonnull final TOPIC message)
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
    public <TOPIC> void subscribe (@Nonnull final Class<TOPIC> topic, @Nonnull final Listener<TOPIC> listener)
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
    public void unsubscribe (@Nonnull final Listener<?> listener)
      {
        log.debug("unsubscribe({})", listener);

        for (final List<WeakReference<Listener<?>>> list : listenersMapByTopic.values())
          {
            list.removeIf(ref -> (ref.get() == null) || (ref.get() == listener));
          }
      }

    /*******************************************************************************************************************
     *
     * Dispatches a message.
     *
     * @param   <TOPIC>   the static type of the topic
     * @param   topic     the dynamic type of the topic
     * @param   message   the message
     *
     ******************************************************************************************************************/
    protected <TOPIC> void dispatchMessage (@Nonnull final Class<TOPIC> topic, @Nonnull final TOPIC message)
      {
        final HashSet<Map.Entry<Class<?>, List<WeakReference<MessageBus.Listener<?>>>>> clone =
                new HashSet<>(listenersMapByTopic.entrySet()); // FIXME: marked as dubious by SpotBugs

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
    private <TOPIC> List<WeakReference<Listener<TOPIC>>> findListenersByTopic (@Nonnull final Class<TOPIC> topic)
      {
        List<WeakReference<Listener<TOPIC>>> listeners = (List)listenersMapByTopic.get(topic);

        if (listeners == null)
          {
            listeners = new ArrayList<>();
            listenersMapByTopic.put(topic, (List)listeners);
          }

        return listeners;
      }
  }
