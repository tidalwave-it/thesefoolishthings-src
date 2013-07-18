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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.tidalwave.util.Task;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContextManagerTest
  {
    private DefaultContextManager fixture;

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
    public void setupFixture()
      {
        fixture = new DefaultContextManager();

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
        assertThat(fixture.getContexts(), is(Collections.<Object>emptyList()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts()
      {
        fixture.addGlobalContext(globalContext1);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts_in_order()
      {
        fixture.addGlobalContext(globalContext1);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_and_retrieve_global_contexts()
      {
        fixture.addGlobalContext(globalContext1);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);
        fixture.removeGlobalContext(globalContext2);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_local_contexts()
      {
        fixture.addLocalContext(localContext1);

        assertThat(fixture.getContexts(), is(Arrays.asList(localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_local_contexts_in_order()
      {
        fixture.addLocalContext(localContext1);
        fixture.addLocalContext(localContext2);
        fixture.addLocalContext(localContext3);

        assertThat(fixture.getContexts(), is(Arrays.asList(localContext3, localContext2, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_and_retrieve_local_contexts()
      {
        fixture.addLocalContext(localContext1);
        fixture.addLocalContext(localContext2);
        fixture.addLocalContext(localContext3);
        fixture.removeLocalContext(localContext2);

        assertThat(fixture.getContexts(), is(Arrays.asList(localContext3, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_prioritize_global_contexts()
      {
        fixture.addLocalContext(localContext1);
        fixture.addLocalContext(localContext2);
        fixture.addGlobalContext(globalContext1);
        fixture.addLocalContext(localContext3);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3,
                                                           localContext3, localContext2, localContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_confine_local_contexts_in_their_thread()
      throws InterruptedException
      {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final CountDownLatch latch = new CountDownLatch(3);

        executorService.submit(new Runnable()
          {
            public void run()
              {
                fixture.addGlobalContext(globalContext1);
                fixture.addLocalContext(localContext1);
                latch.countDown();
              }
          });

        executorService.submit(new Runnable()
          {
            public void run()
              {
                fixture.addGlobalContext(globalContext2);
                fixture.addLocalContext(localContext2);
                latch.countDown();
              }
          });

        executorService.submit(new Runnable()
          {
            public void run()
              {
                fixture.addGlobalContext(globalContext3);
                fixture.addLocalContext(localContext3);
                latch.countDown();
              }
          });

        latch.await();

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // TODO: test findContext()

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void runWithContexts_must_temporarily_associate_local_contexts()
      throws InterruptedException
      {
        fixture.addGlobalContext(globalContext1);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3)));

        final String result = fixture.runWithContexts(Arrays.asList(localContext1, localContext2, localContext3),
                new Task<String, RuntimeException>()
          {
            @Override @Nonnull
            public String run()
              {
                assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3,
                                                                   localContext3, localContext2, localContext1)));
                return "result";
              }
          });

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3)));
        assertThat(result, is("result"));
      }
  }