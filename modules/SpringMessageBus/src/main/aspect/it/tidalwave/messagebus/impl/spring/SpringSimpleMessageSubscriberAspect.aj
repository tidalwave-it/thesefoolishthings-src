/**
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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;

/***********************************************************************************************************************
 *
 * This aspect implements the semantics of @SimpleMessageSubscriber in a Spring 3+ environment.
 *
 * @author  Fabrizio Giudici
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public aspect SpringSimpleMessageSubscriberAspect
  {
    //
    // This aspect works by making the target bean implement InitializingBean and DisposableBean and introducing
    // an implementation that delegates the required subscribe/unsubscribe semantics to
    // SpringSimpleMessageSubscriberSupport
    //
    static interface MessageBusHelperAware extends InitializingBean, DisposableBean
      {
      }

    declare parents:
        @SimpleMessageSubscriber * implements MessageBusHelperAware;

    public SpringSimpleMessageSubscriberSupport MessageBusHelperAware.support =
        new SpringSimpleMessageSubscriberSupport(this);

    public void MessageBusHelperAware.afterPropertiesSet()
      {
        support.subscribeAll();
      }

    public void MessageBusHelperAware.destroy()
      {
        support.unsubscribeAll();
      }
  }