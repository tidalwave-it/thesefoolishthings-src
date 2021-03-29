/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import it.tidalwave.role.ContextManager;
import it.tidalwave.util.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ContextSamplerTest
  {
    @RequiredArgsConstructor @Getter static
    class MockContextManagerProvider implements ContextManagerProvider
      {
        @Nonnull
        private final ContextManager contextManager;
      }

    private ContextSampler underTest;

    private ContextManager contextManager;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        contextManager = mock(ContextManager.class);
        ContextManager.Locator.set(new MockContextManagerProvider(contextManager));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "contextProvider")
    public void must_sample_Contexts_at_construction_time (@Nonnull final List<Object> contexts)
      {
        // given
        when(contextManager.getContexts()).thenReturn(contexts);
        // when
        underTest = new ContextSampler(new Object());
        // then
        assertThat(underTest.getContexts(), is(contexts));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "contextProvider")
    public void must_delegate_runWithContexts_to_ContextManager (@Nonnull final List<Object> contexts)
      {
        // given
        when(contextManager.getContexts()).thenReturn(contexts);
        final Task<String, RuntimeException> task = mock(Task.class);
//        when(contextManager.runWithContexts(any(List.class), eq(task))).thenReturn("result");
        underTest = new ContextSampler(new Object());
        reset(contextManager);
        // when
        final String result = underTest.runWithContexts(task);
        // then
        verify(contextManager, times(1)).runWithContexts(eq(contexts), same(task));
        verifyNoMoreInteractions(contextManager);
//        assertThat(result, is("result")); FIXME: depend on commented stubbing above
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider
    public Object[][] contextProvider()
      {
        return new Object[][]
          {
            { Collections.emptyList()            },
            { Arrays.asList("a", "b", "c")       },
            { Arrays.asList("a", "b", "c" , "d") },
          };
      }
  }
