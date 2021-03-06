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
package it.tidalwave.role.ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.As;
import it.tidalwave.util.spi.AsDelegateProvider;
import static it.tidalwave.role.ui.Presentable._Presentable_;
import static it.tidalwave.util.test.MoreAnswers.CALLS_DEFAULT_METHODS;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PresentationModelTest
  {
    static interface MockRole1 {}

    static interface MockRole2 {}

    private final MockRole1 mockRole1 = mock(MockRole1.class);

    private final MockRole2 mockRole2 = mock(MockRole2.class);

    private final Collection<Object> roles = Arrays.asList(mockRole1, mockRole2);

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeClass
    public void setup()
      {
        AsDelegateProvider.Locator.set(AsDelegateProvider.empty());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @AfterClass
    public void cleanup()
      {
        AsDelegateProvider.Locator.reset();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_ofMaybePresentable_without_Presentable()
      {
        // given
        final As owner = mock(As.class);
        // when
        final PresentationModel actualPm = PresentationModel.ofMaybePresentable(owner, roles);
        // then
        assertThat(actualPm.as(MockRole1.class), is(sameInstance(mockRole1)));
        assertThat(actualPm.as(MockRole2.class), is(sameInstance(mockRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_ofMaybePresentable_with_Presentable()
      {
        // given
        final As owner = mock(As.class);
        final Presentable presentable = mock(Presentable.class, CALLS_DEFAULT_METHODS);
        final PresentationModel pm = mock(PresentationModel.class);
        when(presentable.createPresentationModel(anyCollection())).thenReturn(pm);
        when(owner.maybeAs(_Presentable_)).thenReturn(Optional.of(presentable));
        // when
        final PresentationModel actualPm = PresentationModel.ofMaybePresentable(owner, roles);
        // then
        assertThat(actualPm, is(sameInstance(pm)));
        verify(presentable).createPresentationModel(eq(roles));
        verifyNoMoreInteractions(presentable);
      }
  }
