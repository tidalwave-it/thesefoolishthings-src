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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.tidalwave.util.Task;
import it.tidalwave.role.ContextManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultContextManagerTest
  {
    private DefaultContextManager underTest;

    private Object globalContext1;

    private Object globalContext2;

    private Object globalContext3;

    private Object localContext1;

    private Object localContext2;

    private Object localContext3;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        underTest = new DefaultContextManager();

        globalContext1 = "globalContext1";
        globalContext2 = "globalContext2";
        globalContext3 = "globalContext3";
        localContext1 = "localContext1";
        localContext2 = "localContext2";
        localContext3 = "localContext3";
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_start_with_no_contexts()
      {
        // given fresh DefaultContextManager
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(Collections.emptyList()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts()
      {
        // given
        underTest.addGlobalContext(globalContext1);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(globalContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts_in_order()
      {
        // given
        underTest.addGlobalContext(globalContext1);
        underTest.addGlobalContext(globalContext2);
        underTest.addGlobalContext(globalContext3);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(globalContext1, globalContext2, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_and_retrieve_global_contexts()
      {
        // given
        underTest.addGlobalContext(globalContext1);
        underTest.addGlobalContext(globalContext2);
        underTest.addGlobalContext(globalContext3);
        underTest.removeGlobalContext(globalContext2);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(globalContext1, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_local_contexts()
      {
        // given
        underTest.addLocalContext(localContext1);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_local_contexts_in_order()
      {
        // given
        underTest.addLocalContext(localContext1);
        underTest.addLocalContext(localContext2);
        underTest.addLocalContext(localContext3);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(localContext3, localContext2, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_and_retrieve_local_contexts()
      {
        // given
        underTest.addLocalContext(localContext1);
        underTest.addLocalContext(localContext2);
        underTest.addLocalContext(localContext3);
        underTest.removeLocalContext(localContext2);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(localContext3, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_prioritize_global_contexts()
      {
        // given
        underTest.addLocalContext(localContext1);
        underTest.addLocalContext(localContext2);
        underTest.addGlobalContext(globalContext1);
        underTest.addLocalContext(localContext3);
        underTest.addGlobalContext(globalContext2);
        underTest.addGlobalContext(globalContext3);
        // when
        final List<Object> contexts = underTest.getContexts();
        // then
        assertThat(contexts, is(asList(globalContext1, globalContext2, globalContext3,
                                       localContext3, localContext2, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_confine_local_contexts_in_their_thread()
      throws InterruptedException
      {
        // given
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final CountDownLatch latch = new CountDownLatch(3);

        final Runnable r1 = () ->
          {
            underTest.addGlobalContext(globalContext1);
            underTest.addLocalContext(localContext1);
            latch.countDown();
          };

        final Runnable r2 = () ->
          {
            underTest.addGlobalContext(globalContext2);
            underTest.addLocalContext(localContext2);
            latch.countDown();
          };

        final Runnable r3 = () ->
          {
            underTest.addGlobalContext(globalContext3);
            underTest.addLocalContext(localContext3);
            latch.countDown();
          };
        // when
        executorService.submit(r1);
        executorService.submit(r2);
        executorService.submit(r3);
        latch.await();
        // then
        assertThat(underTest.getContexts(), is(asList(globalContext1, globalContext2, globalContext3)));
      }

    // TODO: test findContextOfType()

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void runWithContexts_must_temporarily_associate_local_contexts()
      {
        // given
        underTest.addGlobalContext(globalContext1);
        underTest.addGlobalContext(globalContext2);
        underTest.addGlobalContext(globalContext3);
        final List<Object> contextsBefore = underTest.getContexts();
        // when
        final List<Object> contextsInThread = new ArrayList<>();
        final String result = underTest.runWithContexts(asList(localContext1, localContext2, localContext3),
            new Task<String, RuntimeException>()
          {
            @Override @Nonnull
            public String run()
              {
                contextsInThread.addAll(underTest.getContexts());
                return "result";
              }
          });
        final List<Object> contextsAfter = underTest.getContexts();
        // then
        assertThat(contextsBefore, is(asList(globalContext1, globalContext2, globalContext3)));
        assertThat(contextsInThread, is(asList(globalContext1, globalContext2, globalContext3,
                                               localContext3, localContext2, localContext1)));
        assertThat(contextsAfter, is(contextsBefore));
        assertThat(result, is("result"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_local_contexts()
      {
        // given
        final DefaultContextManager underTest = spy(DefaultContextManager.class);
        final Runnable body = mock(Runnable.class);
        // when
        try (ContextManager.Binder binder = underTest.binder(localContext1, localContext2, localContext3))
          {
            body.run();
          }
        // then
        verify(underTest).addLocalContext(same(localContext1));
        verify(underTest).addLocalContext(same(localContext2));
        verify(underTest).addLocalContext(same(localContext3));
        verify(body).run();
        verify(underTest).removeLocalContext(same(localContext1));
        verify(underTest).removeLocalContext(same(localContext2));
        verify(underTest).removeLocalContext(same(localContext3));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_local_contexts_when_exception_throw()
      {
        // given
        final DefaultContextManager underTest = spy(DefaultContextManager.class);
        final Runnable body = mock(Runnable.class);
        // when
        try (ContextManager.Binder binder = underTest.binder(localContext1, localContext2, localContext3))
          {
            body.run();
            throw new RuntimeException("Purportedly generated exception");
          }
        catch (RuntimeException e)
          {
            // caught
          }
        // then
        verify(underTest).addLocalContext(same(localContext1));
        verify(underTest).addLocalContext(same(localContext2));
        verify(underTest).addLocalContext(same(localContext3));
        verify(body).run();
        verify(underTest).removeLocalContext(same(localContext1));
        verify(underTest).removeLocalContext(same(localContext2));
        verify(underTest).removeLocalContext(same(localContext3));
      }
  }