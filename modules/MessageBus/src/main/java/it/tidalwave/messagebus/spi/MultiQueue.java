/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.messagebus.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
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
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class MultiQueue 
  {
    @RequiredArgsConstructor @Getter @ToString
    static class TopicAndMessage<TOPIC>
      {
        @Nonnull
        private final Class<TOPIC> topic;
        @Nonnull
        private final TOPIC message;
      }
     
    private final Comparator<Class<?>> comparator = new Comparator<Class<?>>()
      {
        @Override
        public int compare (final @Nonnull Class<?> c1, final @Nonnull Class<?> c2) 
          {
            return c1.getName().compareTo(c2.getName());
          }
      };
    
    private final ConcurrentNavigableMap<Class<?>, Queue<?>> queueMapByTopic = new ConcurrentSkipListMap<>(comparator);
    
    private Class<?> latestSentTopic = null;
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public synchronized <TOPIC> void add (final @Nonnull Class<TOPIC> topic, final @Nonnull TOPIC message) 
      {
        getQueue(topic).add(message);
        notifyAll();
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public synchronized <TOPIC> TopicAndMessage<TOPIC> remove() 
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
        
        for (final Map.Entry<Class<?>, Queue<?>> e : queueMapByTopic.entrySet())
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
