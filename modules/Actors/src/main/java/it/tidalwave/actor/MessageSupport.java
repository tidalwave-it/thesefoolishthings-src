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
package it.tidalwave.actor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.actor.impl.Locator;
import it.tidalwave.actor.impl.DefaultCollaboration;
import it.tidalwave.actor.spi.CollaborationAwareMessageBus;
import lombok.Delegate;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @stereotype Message
 * 
 * A support class for implementing messages.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j @EqualsAndHashCode(of="collaboration")
public abstract class MessageSupport implements Collaboration.Provider, As, Serializable
  {
    // TODO: @Inject
    private final Provider<CollaborationAwareMessageBus> messageBus = Locator.createProviderFor(CollaborationAwareMessageBus.class);

    @Nonnull
    protected final DefaultCollaboration collaboration;
    
    @Delegate
    private AsDelegate asDelegate = AsDelegateProvider.Locator.find().createAsDelegate(this);

    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    protected MessageSupport()
      {
        this.collaboration = DefaultCollaboration.getOrCreateCollaboration(this);
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    protected MessageSupport (final @Nonnull Collaboration collaboration)
      {
        this.collaboration = (DefaultCollaboration)collaboration;
      }
    
    /*******************************************************************************************************************
     * 
     * Returns the {@link Collaboration} that this message is part of.
     * 
     * @return  the {@code Collaboration}
     * 
     ******************************************************************************************************************/
    @Override @Nonnull
    public Collaboration getCollaboration() 
      {
        return collaboration;
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    public Collaboration send()
      {
        log.debug("send() - {}", this);
        collaboration.registerDeliveringMessage(this);
        messageBus.get().publish(this); 
        return collaboration;
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    public Collaboration sendLater (final @Nonnegative int delay, final @Nonnull TimeUnit timeUnit)
      {
        log.debug("sendLater({}, {}) - {}", new Object[] { delay, timeUnit, this });
        collaboration.registerDeliveringMessage(this);
          
        new Timer().schedule(new TimerTask() 
          {
            @Override
            public void run() 
              {
                messageBus.get().publish(MessageSupport.this); 
              }
          }, TimeUnit.MILLISECONDS.convert(delay, timeUnit));
        
        return collaboration;
      }

    /*******************************************************************************************************************
     * 
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (final @Nonnull Class<T> type) 
      {
        return as(type, new As.NotFoundBehaviour<T>() 
          {
            @Nonnull
            public T run (final @Nonnull Throwable t) 
              {
                throw new AsException(type, t);
              }
          });
      }
  } 
