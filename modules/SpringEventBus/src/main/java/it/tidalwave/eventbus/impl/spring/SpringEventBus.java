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
package it.tidalwave.eventbus.impl.spring;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.task.TaskExecutor;
import it.tidalwave.eventbus.EventBus;
import it.tidalwave.eventbus.EventBusListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A simple implementation of {@link EventBus} based on Spring.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ThreadSafe @Slf4j
public class SpringEventBus implements EventBus
  {
    private final Map<Class<?>, List<WeakReference<EventBusListener<?>>>> listenersMapByTopic =
            new HashMap<Class<?>, List<WeakReference<EventBusListener<?>>>>();
    
    @Getter @Setter
    private TaskExecutor taskExecutor;
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Topic event) 
      {
        publish((Class<Topic>)event.getClass(), event);
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Class<Topic> topic, final @Nonnull Topic event) 
      {
        log.debug("publish({}, {})", topic, event);
        taskExecutor.execute(new Runnable() 
          {
            @Override
            public void run() 
              {
                publishImpl(topic, event);
              }
        });
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <Topic> void subscribe (final @Nonnull Class<Topic> topic, final @Nonnull EventBusListener<Topic> listener) 
      {
        log.info("subscribe({}, {})", topic, listener);
        findListenersByTopic(topic).add(new WeakReference<EventBusListener<Topic>>(listener));
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private <Topic> void publishImpl (final @Nonnull Class<Topic> topic, final @Nonnull Topic event) 
      {
        final List<WeakReference<EventBusListener<Topic>>> listeners = new ArrayList<WeakReference<EventBusListener<Topic>>>(findListenersByTopic(topic));
        
        for (final WeakReference<EventBusListener<Topic>> listenerReference : listeners)
          {
            final EventBusListener<Topic> listener = listenerReference.get();

            if (listener != null)
              {
                try
                  {
                    listener.notify(event);   
                  }
                catch (Throwable t)
                  {
                    log.warn("publish()", t); 
                  }
              }
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private <Topic> List<WeakReference<EventBusListener<Topic>>> findListenersByTopic (final @Nonnull Class<Topic> topic)
      {
        final List tmp = listenersMapByTopic.get(topic);
        List<WeakReference<EventBusListener<Topic>>> listeners = tmp;
        
        if (listeners == null)
          {
            listeners = new ArrayList<WeakReference<EventBusListener<Topic>>>();
            listenersMapByTopic.put(topic, (List)listeners);
          } 
        
        return listeners;
      }
  }
