/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.messagebus.spi;

import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

/***************************************************************************************************************************************************************
 *
 * An implementation of {@link MessageDelivery} that dispatches messages as they are delivered, each one in a separated
 * thread.
 * 
 * @author  Fabrizio Giudici
 * @since   2.2
 *
 **************************************************************************************************************************************************************/
@Slf4j
public class SimpleAsyncMessageDelivery implements MessageDelivery
  {
    @Nonnull
    private SimpleMessageBus messageBusSupport;
    
    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    @Override
    public void initialize (@Nonnull final SimpleMessageBus messageBusSupport)
      {
        this.messageBusSupport = messageBusSupport;
      }
    
    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    @Override
    public <T> void deliverMessage (@Nonnull final Class<T> topic, @Nonnull final T message)
      {
        messageBusSupport.getExecutor().execute(() -> messageBusSupport.dispatchMessage(topic, message));
      }
  }
