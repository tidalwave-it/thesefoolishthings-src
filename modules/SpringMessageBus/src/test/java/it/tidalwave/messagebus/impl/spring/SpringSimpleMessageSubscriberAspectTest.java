/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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
package it.tidalwave.messagebus.impl.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.messagebus.MessageBus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static it.tidalwave.messagebus.impl.spring.ListenerAdapterMatcher.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SpringSimpleMessageSubscriberAspectTest
  {
    private ApplicationContext context;

    private MockSubscriber1 subscriber1;

    private MockSubscriber2 subscriber2;

    private MessageBus applicationMessageBus;

    private MessageBus otherMessageBus;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("SpringSimpleMessageSubscriberAspectTestBeans.xml");
        subscriber1 = context.getBean(MockSubscriber1.class);
        subscriber2 = context.getBean(MockSubscriber2.class);
        applicationMessageBus = context.getBean("applicationMessageBus", MessageBus.class);
        otherMessageBus = context.getBean("otherMessageBus", MessageBus.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_subscribe_to_default_message_bus()
      throws Exception
      {
        reset(applicationMessageBus);
        reset(otherMessageBus);

        assertThat(subscriber1, is(instanceOf(InitializingBean.class)));
        ((InitializingBean)subscriber1).afterPropertiesSet();

        verify(applicationMessageBus).subscribe(eq(MockEvent1.class), argThat(listenerAdapter()
                                                                            .withMethodName("onMockEvent1")
                                                                            .withOwner(subscriber1)
                                                                            .withTopic(MockEvent1.class)));
        verify(applicationMessageBus).subscribe(eq(MockEvent2.class), argThat(listenerAdapter()
                                                                            .withMethodName("onMockEvent2")
                                                                            .withOwner(subscriber1)
                                                                            .withTopic(MockEvent2.class)));
        verifyNoMoreInteractions(applicationMessageBus);
        verifyZeroInteractions(otherMessageBus);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_subscribe_to_default_message_bus")
    public void must_unsubscribe_from_default_message_bus()
      throws Exception
      {
        reset(applicationMessageBus);
        reset(otherMessageBus);

        assertThat(subscriber1, is(instanceOf(DisposableBean.class)));
        ((DisposableBean)subscriber1).destroy();

        verify(applicationMessageBus).unsubscribe(argThat(listenerAdapter().withMethodName("onMockEvent1")
                                                                .withOwner(subscriber1)
                                                                .withTopic(MockEvent1.class)));
        verify(applicationMessageBus).unsubscribe(argThat(listenerAdapter().withMethodName("onMockEvent2")
                                                                .withOwner(subscriber1)
                                                                .withTopic(MockEvent2.class)));
        verifyNoMoreInteractions(applicationMessageBus);
        verifyZeroInteractions(otherMessageBus);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_subscribe_to_default_message_bus")
    public void must_subscribe_to_alternate_message_bus()
      throws Exception
      {
        reset(applicationMessageBus);
        reset(otherMessageBus);

        assertThat(subscriber2, is(instanceOf(InitializingBean.class)));
        ((InitializingBean)subscriber2).afterPropertiesSet();

        verify(otherMessageBus).subscribe(eq(MockEvent1.class), argThat(listenerAdapter()
                                                                            .withMethodName("onMockEvent1")
                                                                            .withOwner(subscriber2)
                                                                            .withTopic(MockEvent1.class)));
        verify(otherMessageBus).subscribe(eq(MockEvent2.class), argThat(listenerAdapter()
                                                                            .withMethodName("onMockEvent2")
                                                                            .withOwner(subscriber2)
                                                                            .withTopic(MockEvent2.class)));
        verifyNoMoreInteractions(otherMessageBus);
        verifyZeroInteractions(applicationMessageBus);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_unsubscribe_from_default_message_bus")
    public void must_unsubscribe_from_alternate_message_bus()
      throws Exception
      {
        reset(applicationMessageBus);
        reset(otherMessageBus);

        assertThat(subscriber2, is(instanceOf(DisposableBean.class)));
        ((DisposableBean)subscriber2).destroy();

        verify(otherMessageBus).unsubscribe(argThat(listenerAdapter().withMethodName("onMockEvent1")
                                                                     .withOwner(subscriber2)
                                                                     .withTopic(MockEvent1.class)));
        verify(otherMessageBus).unsubscribe(argThat(listenerAdapter().withMethodName("onMockEvent2")
                                                                     .withOwner(subscriber2)
                                                                     .withTopic(MockEvent2.class)));
        verifyNoMoreInteractions(otherMessageBus);
        verifyZeroInteractions(applicationMessageBus);
      }
  }