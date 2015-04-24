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
package it.tidalwave.messagebus.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An implementation of {@link MessageDelivery} that dispatches messages in a round-robin fashion, topic by topic.
 * Each delivery is performed in a separated thread.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @since   2.2
 *
 **********************************************************************************************************************/
@Slf4j
public class RoundRobinAsyncMessageDelivery implements MessageDelivery
  {
    @RequiredArgsConstructor @Getter @ToString
    static class TopicAndMessage<TOPIC>
      {
        @Nonnull
        private final Class<TOPIC> topic;
        @Nonnull
        private final TOPIC message;
      }
     
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private final Comparator<Class<?>> comparator = new Comparator<Class<?>>()
      {
        @Override
        public int compare (final @Nonnull Class<?> c1, final @Nonnull Class<?> c2) 
          {
            return c1.getName().compareTo(c2.getName());
          }
      };
    
    @Nonnull
    private SimpleMessageBus messageBusSupport;
    
    private final ConcurrentNavigableMap<Class<?>, Queue<?>> queueMapByTopic = new ConcurrentSkipListMap<>(comparator);
    
    private Class<?> latestSentTopic = null;
    
    @Getter @Setter
    private int workers = 10;
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private final Runnable dispatcher = new Runnable()
      {
        @Override
        public void run() 
          {
            for (;;)
              {
                try 
                  {
                    dispatchMessage(pollMessageFromNextQueue());
                  } 
                catch (InterruptedException e) 
                  {
                    break;
                  }
              }
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize (final @Nonnull SimpleMessageBus messageBusSupport) 
      {
        this.messageBusSupport = messageBusSupport;
        final Executor executor = this.messageBusSupport.getExecutor();
        
        for (int i = 0; i < workers; i++)
          {
            executor.execute(dispatcher);
          }
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <TOPIC> void deliverMessage (final @Nonnull Class<TOPIC> topic, @Nonnull TOPIC message)
      {
        getQueue(topic).add(message);
        
        synchronized (this)
          {
            notifyAll();
          }
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private <TOPIC> void dispatchMessage (final @Nonnull TopicAndMessage<TOPIC> tam)
      {
        messageBusSupport.dispatchMessage(tam.getTopic(), tam.getMessage());
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private synchronized <TOPIC> TopicAndMessage<TOPIC> pollMessageFromNextQueue() 
      throws InterruptedException
      {
        while (queueMapByTopic.isEmpty()) // now is probably redundamt, reorderedTopic would return empty
          {
            wait();  
          }
        
        for (;;)
          {
            for (final Class<?> topic : reorderedTopics())
              {
                final Queue<?> queue = queueMapByTopic.get(topic);
                final Object message = queue.poll();
                
                if (message != null)
                  {
                    latestSentTopic = topic;
                    
                    if (log.isTraceEnabled())
                      {
                        log.trace("stats {}", stats());
                      }
                    
                    return new TopicAndMessage<>((Class<TOPIC>)topic, (TOPIC)message);  
                  }
              }

            if (log.isTraceEnabled())
              {
                log.trace("all queues empty; stats {}", stats());
              }
    
            wait();  
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the list of topics reordered, so it starts just after latestSentTopic and wraps around.
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Class<?>> reorderedTopics() 
      {
        final NavigableSet<Class<?>> keySet = queueMapByTopic.navigableKeySet();
        final List<Class<?>> scanSet = new ArrayList<>();

        if (latestSentTopic == null)
          {
            scanSet.addAll(keySet);
          }
        else
          {
            scanSet.addAll(keySet.subSet(latestSentTopic, false, keySet.last(), true));
            scanSet.addAll(keySet.subSet(keySet.first(), true, latestSentTopic, true));
          }
        
        return scanSet;
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private synchronized String stats()
      {
        final StringBuilder b = new StringBuilder();
        String separator = "";
        
        for (final Entry<Class<?>, Queue<?>> e : queueMapByTopic.entrySet())
          {
            b.append(separator).append(String.format("%s[%s]: %d", 
                    e.getKey().getSimpleName(), e.getKey().equals(latestSentTopic) ? "X" : " ", e.getValue().size()));
            separator = ", ";
          }

        return b.toString();
      }
    
    /*******************************************************************************************************************
     *
     * Returns the queue associated to a given topic. The queue is created if the topic is new.
     * 
     * @param   topic       the topic
     * @return              the queue
     *
     ******************************************************************************************************************/
    @Nonnull
    private synchronized <TOPIC> Queue<TOPIC> getQueue (final @Nonnull Class<TOPIC> topic)
      {
        // Java 8 would make this easier
        Queue<TOPIC> queue = (Queue<TOPIC>)queueMapByTopic.get(topic);
        
        if (queue == null)
          {
            queue = new LinkedBlockingQueue<>();
            queueMapByTopic.put(topic, queue);
            notifyAll();
          }
        
        return queue;
      }
  }
