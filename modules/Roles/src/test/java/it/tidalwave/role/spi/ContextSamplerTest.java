/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.List;
import it.tidalwave.role.ContextManager;
import it.tidalwave.util.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ContextSamplerTest
  {
    @RequiredArgsConstructor @Getter
    class MockContextManagerProvider implements ContextManagerProvider
      {
        @Nonnull
        private final ContextManager contextManager;
      }

    private ContextSampler fixture;

    private ContextManager contextManager;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        contextManager = mock(ContextManager.class);
        ContextManager.Locator.set(new MockContextManagerProvider(contextManager));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "contextProvider")
    public void must_sample_Contexts_at_construction_time (final @Nonnull List<Object> contexts)
      {
        when(contextManager.getContexts()).thenReturn(contexts);

        fixture = new ContextSampler();

        assertThat(fixture.getContexts(), is(contexts));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "contextProvider")
    public void must_delegate_runWithContexts_to_ContextManager (final @Nonnull List<Object> contexts)
      {
        when(contextManager.getContexts()).thenReturn(contexts);
//        final Class<List<Object>> listClass = (Class)List.class;
//        final Class<Task<Object, RuntimeException>> taskClass = (Class)Task.class;
//        when(contextManager.<Object>runWithContexts(CoreMatchers.any(listClass),
//                                                    CoreMatchers.any((taskClass))));
        final Task<String, RuntimeException> task = mock(Task.class);

        fixture = new ContextSampler();
        reset(contextManager);
        final Object result = fixture.runWithContexts(task);

        verify(contextManager, times(1)).runWithContexts(eq(contexts), same(task));
        verifyNoMoreInteractions(contextManager);
//        assertThat(result, is((Object)"result")); FIXME: need to stub
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider(name = "contextProvider")
    public Object[][] contextProvider()
      {
        return new Object[][]
          {
            { Collections.<Object>emptyList()    },
            { Arrays.asList("a", "b", "c")       },
            { Arrays.asList("a", "b", "c" , "d") },
          };
      }
  }
