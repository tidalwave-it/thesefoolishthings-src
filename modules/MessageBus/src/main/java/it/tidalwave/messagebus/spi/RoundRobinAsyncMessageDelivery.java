/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.concurrent.Executor;
import it.tidalwave.messagebus.spi.MultiQueue.TopicAndMessage;
import lombok.Getter;
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
@Slf4j @ToString(of = "workers")
public class RoundRobinAsyncMessageDelivery implements MessageDelivery
  {
    @Nonnull
    private SimpleMessageBus messageBusSupport;
    
    @Getter @Setter
    private int workers = 10;
    
    private final MultiQueue multiQueue = new MultiQueue();
    
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
                    dispatchMessage(multiQueue.remove());
                  } 
                catch (InterruptedException e) 
                  {
                    break;
                  }
              }
          }
        
        private <TOPIC> void dispatchMessage (final @Nonnull TopicAndMessage<TOPIC> tam)
          {
            messageBusSupport.dispatchMessage(tam.getTopic(), tam.getMessage());
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
        multiQueue.add(topic, message);
      }
  }
