/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class MultiQueue 
  {
    @RequiredArgsConstructor @Getter @ToString
    static class TopicAndMessage<T>
      {
        @Nonnull
        private final Class<T> topic;

        @Nonnull
        private final T message;
      }
     
    private final ConcurrentNavigableMap<Class<?>, Queue<?>> queueMapByTopic =
            new ConcurrentSkipListMap<>(Comparator.comparing(Class::getName));
    
    private Class<?> latestSentTopic = null;
    
    /*******************************************************************************************************************
     *
     * Adds a message of the given topic to this queue and issues a notification.
     *
     * @param   <T>   the static type of the message
     * @param   topic     the dynamic type of the message
     * @param   message   the message
     *
     ******************************************************************************************************************/
    public synchronized <T> void add (@Nonnull final Class<T> topic, @Nonnull final T message)
      {
        getQueue(topic).add(message);
        notifyAll();
      }
    
    /*******************************************************************************************************************
     *
     * Removes and returns the next pair (topic, message) from the queue. Blocks until one is available.
     *
     * @param   <T>                 the static type of the topic
     * @return                          the topic and message
     * @throws  InterruptedException    if interrupted while waiting
     *
     ******************************************************************************************************************/
    @Nonnull
    public synchronized <T> TopicAndMessage<T> remove()
      throws InterruptedException
      {
        for (;;)
          {
            for (final var topic : reorderedTopics())
              {
                final var queue = queueMapByTopic.get(topic);
                final var message = queue.poll();
                
                if (message != null)
                  {
                    latestSentTopic = topic;
                    
                    if (log.isTraceEnabled())
                      {
                        log.trace("stats {}", stats());
                      }
                    
                    return new TopicAndMessage<>((Class<T>)topic, (T)message);
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
        final var keySet = queueMapByTopic.navigableKeySet();
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
        final var b = new StringBuilder();
        var separator = "";
        
        for (final var e : queueMapByTopic.entrySet())
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
    private synchronized <T> Queue<T> getQueue (@Nonnull final Class<T> topic)
      {
        // TODO Java 8 would make this easier
        var queue = (Queue<T>)queueMapByTopic.get(topic);
        
        if (queue == null)
          {
            queue = new LinkedBlockingQueue<>();
            queueMapByTopic.put(topic, queue);
          }
        
        return queue;
      }
  }
