/**
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;

/***********************************************************************************************************************
 *
 * This aspect implements the semantics of @SimpleMessageSubscriber in a Spring 3+ environment. To use it, the ajc
 * compiler is required. In a Maven environment this is usually done by means of the aspectj-maven-plugin with the
 * proper configuration.
 * If the Tidalwave Super POM is used, it can be done by activating the profiles it.tidalwave-aspectj-v2
 * and it.tidalwave-aspectj-v2.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public aspect SpringSimpleMessageSubscriberAspect
  {
    //
    // This aspect works by making the target bean implement InitializingBean and DisposableBean and introducing
    // an implementation that delegates the required subscribe/unsubscribe semantics to
    // SpringSimpleMessageSubscriberSupport
    //
    static interface MessageBusHelperAware extends InitializingBean, DisposableBean, BeanFactoryAware
      {
      }

    declare parents:
        @SimpleMessageSubscriber * implements MessageBusHelperAware;

    public SpringSimpleMessageSubscriberSupport MessageBusHelperAware.support;

    public void MessageBusHelperAware.setBeanFactory (BeanFactory beanFactory)
      {
        support = new SpringSimpleMessageSubscriberSupport(beanFactory, this);
      }

    public void MessageBusHelperAware.afterPropertiesSet()
      {
        support.subscribeAll();
      }

    public void MessageBusHelperAware.destroy()
      {
        support.unsubscribeAll();
      }
  }