/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus;

import javax.annotation.Nonnull;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class EventBusAdapterExample
  {
    private final MessageBusHelper messageBusHelper;

    public EventBusAdapterExample (final @Nonnull MessageBusHelper.Adapter adapter)
      {
        messageBusHelper = new MessageBusHelper(this, adapter);
      }

    public void start()
      {
        messageBusHelper.subscribeAll();
        // set up your stuff
      }

    public void stop()
      {
        messageBusHelper.unsubscribeAll();
        // dispose your stuff
      }

    private void onMessage1 (final @ListensTo Message1 message)
      {
        log.info("Received {}", message);
        messageBusHelper.publish(new Message2());
      }

    private void onMessage2 (final @ListensTo Message2 message)
      {
        log.info("Received {}", message);
      }
  }
