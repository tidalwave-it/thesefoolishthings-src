/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus.impl.spring;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Configurable(preConstruction = true)
public class SpringSimpleMessageSubscriberSupport
  {
    @Inject @Nonnull
    private BeanFactory beanFactory;

    private final MessageBusHelper busHelper;

    public SpringSimpleMessageSubscriberSupport (@Nonnull final Object bean)
      {
        final String source = bean.getClass().getAnnotation(SimpleMessageSubscriber.class).source();
        final MessageBus messageBus = beanFactory.getBean(source, MessageBus.class);
        busHelper = new MessageBusHelper(bean, new MessageBusAdapterFactory(messageBus));
      }

    public void subscribeAll()
      {
        busHelper.subscribeAll();
      }

    public void unsubscribeAll()
      {
        busHelper.unsubscribeAll();
      }
  }
