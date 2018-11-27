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
package it.tidalwave.actor.impl;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.spi.ReflectionUtils;
import it.tidalwave.actor.annotation.OriginatedBy;
import it.tidalwave.actor.CollaborationCompletedMessage;
import it.tidalwave.actor.CollaborationStartedMessage;
import it.tidalwave.actor.spi.ActorActivatorStats;
import it.tidalwave.actor.spi.CollaborationAwareMessageBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.messagebus.spi.ReflectionUtils.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class CollaborationAwareMessageBusAdapter implements ReflectionUtils.MethodProcessor
  {
    @Nonnull
    private final Object owner;

    @Nonnull
    private final ExecutorWithPriority executor;

    @Nonnull
    private final ActorActivatorStats stats;

    private final List<MessageBus.Listener<?>> messageBusListeners = new ArrayList<MessageBus.Listener<?>>();

    private final Provider<CollaborationAwareMessageBus> messageBus =
            Locator.createProviderFor(CollaborationAwareMessageBus.class);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public FilterResult filter (final @Nonnull Class<?> clazz)
      {
        return FilterResult.ACCEPT; // TODO: should filter with @Actor?
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void process (final @Nonnull Method method)
      {
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        if ((parameterAnnotations.length == 1) && containsAnnotation(parameterAnnotations[0], ListensTo.class))
          {
            registerMessageListener(method);
          }
        else if ((parameterAnnotations.length == 2) && containsAnnotation(parameterAnnotations[0], ListensTo.class)
                                                    && containsAnnotation(parameterAnnotations[1], OriginatedBy.class))
          {
            registerCollaborationListener(method);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void unsubscribe()
      {
        for (final MessageBus.Listener<?> listener : messageBusListeners)
          {
            messageBus.get().unsubscribe(listener);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private <Topic> void registerMessageListener (final @Nonnull Method method)
      {
        log.info("registerMessageListener({})", method);

        final Class<Topic> topic = (Class<Topic>)method.getParameterTypes()[0];
        addListener(method, new MessageListenerAdapter<Topic>(owner, method, executor, stats), topic);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private <Topic> void registerCollaborationListener (final @Nonnull Method method)
      {
        log.info("registerCollaborationListener({})", method);
        final Class<?> collaborationMessageType = method.getParameterTypes()[0];
        final Class<?> messageType = method.getParameterTypes()[1];

        final MessageBus.Listener messageListener = collaborationMessageType.equals(CollaborationStartedMessage.class)
            ? new CollaborationMessageListenerAdapter<CollaborationStartedMessage>(owner, method, executor, messageType, stats)
            : new CollaborationMessageListenerAdapter<CollaborationCompletedMessage>(owner, method, executor, messageType, stats);
        addListener(method, messageListener, collaborationMessageType);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private <Topic> void addListener (final @Nonnull Method method,
                                      final @Nonnull MessageBus.Listener<Topic> messageListener,
                                      final @Nonnull Class<Topic> topic) throws SecurityException
      {
        method.setAccessible(true);
        messageBusListeners.add(messageListener);
        messageBus.get().subscribe(topic, messageListener);
      }
  }
