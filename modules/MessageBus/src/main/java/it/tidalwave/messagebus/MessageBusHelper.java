/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus;

import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.messagebus.spi.ReflectionUtils.*;
import static it.tidalwave.messagebus.spi.ReflectionUtils.MethodProcessor.FilterResult.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class MessageBusHelper
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static interface Adapter
      {
        @Nonnull
        public <TOPIC> MethodAdapter<TOPIC> createMethodAdapter (@Nonnull Object object,
                                                                 @Nonnull Method method,
                                                                 @Nonnull Class<TOPIC> topic);

        public void publish (@Nonnull Object message);

        public <TOPIC> void publish (Class<TOPIC> topic, @Nonnull TOPIC message);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static interface MethodAdapter<TOPIC>
      {
        public void subscribe();

        public void unsubscribe();
      }

    @Nonnull
    private final Object owner;

    @Nonnull
    private final Adapter methodAdapterFactory;

    private final List<MethodAdapter<?>> methodAdapters = new ArrayList<>();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void subscribeAll()
      {
        forEachMethodInTopDownHierarchy(owner, new MethodProcessor()
          {
            @Override @Nonnull
            public FilterResult filter (@Nonnull final Class<?> clazz)
              {
                return clazz.getAnnotation(SimpleMessageSubscriber.class) != null ? ACCEPT : IGNORE;
              }

            @Override
            public void process (@Nonnull final Method method)
              {
                final var parameterAnnotations = method.getParameterAnnotations();

                if ((parameterAnnotations.length == 1) && containsAnnotation(parameterAnnotations[0], ListensTo.class))
                  {
                    registerMessageListener(method);
                  }
              }
          });
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void unsubscribeAll()
      {
        for (final var methodAdapter : methodAdapters)
          {
            methodAdapter.unsubscribe();
          }
      }

    /*******************************************************************************************************************
     *
     * Publishes a message.
     *
     * @param message     the message to deliver
     *
     ******************************************************************************************************************/
    public void publish (@Nonnull final Object message)
      {
        methodAdapterFactory.publish(message);
      }

    /*******************************************************************************************************************
     *
     * Publishes a message.
     *
     * @param <TOPIC>       the static type of the topic
     * @param topicType     the dynamic type of the topic
     * @param topic         the topic
     *
     ******************************************************************************************************************/
    public <TOPIC> void publish (@Nonnull final Class<TOPIC> topicType, @Nonnull final TOPIC topic)
      {
        methodAdapterFactory.publish(topicType, topic);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private <TOPIC> void registerMessageListener (@Nonnull final Method method)
      {
        log.trace("registerMessageListener({})", method);

        final var topic = (Class<TOPIC>)method.getParameterTypes()[0];
        final var methodAdapter = methodAdapterFactory.createMethodAdapter(owner, method, topic);
        methodAdapters.add(methodAdapter);
        methodAdapter.subscribe();
      }
  }
