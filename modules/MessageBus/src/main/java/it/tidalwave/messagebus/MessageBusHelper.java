/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - MessageBus
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
 * @version $Id$
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
        public <Topic> MethodAdapter<Topic> createMethodAdapter (@Nonnull Object object,
                                                                 @Nonnull Method method,
                                                                 @Nonnull Class<Topic> topic);

        public void publish (@Nonnull Object message);

        public <Topic> void publish (Class<Topic> topic, @Nonnull Topic message);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static interface MethodAdapter<Topic>
      {
        public void subscribe();

        public void unsubscribe();
      }

    @Nonnull
    private final Object owner;

    @Nonnull
    private final Adapter methodAdapterFactory;

    private final List<MethodAdapter<?>> methodAdapters = new ArrayList<MethodAdapter<?>>();

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
            public FilterResult filter (final @Nonnull Class<?> clazz)
              {
                return clazz.getAnnotation(SimpleMessageSubscriber.class) != null ? ACCEPT : IGNORE;
              }

            @Override
            public void process (final @Nonnull Method method)
              {
                final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

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
        for (final MethodAdapter<?> methodAdapter : methodAdapters)
          {
            methodAdapter.unsubscribe();
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void publish (final @Nonnull Object message)
      {
        methodAdapterFactory.publish(message);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public <Topic> void publish (final @Nonnull Class<Topic> topicType, final @Nonnull Topic topic)
      {
        methodAdapterFactory.publish(topicType, topic);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private <Topic> void registerMessageListener (final @Nonnull Method method)
      {
        log.trace("registerMessageListener({})", method);

        final Class<Topic> topic = (Class<Topic>)method.getParameterTypes()[0];
        final MethodAdapter<Topic> methodAdapter = methodAdapterFactory.createMethodAdapter(owner, method, topic);
        methodAdapters.add(methodAdapter);
        methodAdapter.subscribe();
      }
  }
