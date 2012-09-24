/***********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://solidblue.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/solidblue-src
 *
 **********************************************************************************************************************/
package it.tidalwave.messagebus.impl.netbeans;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.lang.reflect.Method;
import it.tidalwave.actor.impl.Locator;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.MessageBusHelper.MethodAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * FIXME: this is not related to NetBeans, but to MessageBus - move into a specific adapter module.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access=AccessLevel.PRIVATE) @Slf4j
public class MessageBusAdapterFactory implements MessageBusHelper.Adapter
  {
    public static final MessageBusHelper.Adapter INSTANCE = new MessageBusAdapterFactory();
    
    private final Provider<MessageBus> messageBus = Locator.createProviderFor(MessageBus.class);
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    class MessageBusListenerAdapter <Topic> implements MethodAdapter<Topic>, MessageBus.Listener<Topic>
      {
        @Nonnull
        private final Object owner;

        @Nonnull
        private final Method method;
        
        @Nonnull
        private final Class<Topic> topic;
        
        public MessageBusListenerAdapter (final @Nonnull Object owner, 
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
            messageBus.get().subscribe(topic, this);
          }

        @Override
        public void unsubscribe() 
          {
            messageBus.get().unsubscribe(this);  
          }
      }
    
    /*******************************************************************************************************************
     * 
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Override @Nonnull
    public <Topic> MethodAdapter createMethodAdapter (final @Nonnull Object owner,
                                                      final @Nonnull Method method,
                                                      final @Nonnull Class<Topic> topic)
      {
        return new MessageBusListenerAdapter(owner, method, topic);
      }
    
    /*******************************************************************************************************************
     * 
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Override
    public void publish (final @Nonnull Object message) 
      {
        messageBus.get().publish(message);
      }

    /*******************************************************************************************************************
     * 
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Override
    public <Topic> void publish (final @Nonnull Class<Topic> topic, final @Nonnull Topic message) 
      {
        messageBus.get().publish(topic, message);
      }
  }
