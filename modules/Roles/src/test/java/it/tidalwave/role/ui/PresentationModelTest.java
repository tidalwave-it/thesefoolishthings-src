/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.role.ui;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import it.tidalwave.util.As;
import it.tidalwave.role.spi.SystemRoleFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static it.tidalwave.util.test.MoreAnswers.CALLS_DEFAULT_METHODS;
import static it.tidalwave.role.ui.Presentable._Presentable_;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class PresentationModelTest
  {
    static interface MockRole1 {}

    static interface MockRole2 {}

    private final MockRole1 mockRole1 = mock(MockRole1.class);

    private final MockRole2 mockRole2 = mock(MockRole2.class);

    private final Collection<Object> roles = List.of(mockRole1, mockRole2);

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    // START SNIPPET: setup
    @BeforeClass
    public void setup()
      {
        SystemRoleFactory.reset();
      }
    // END SNIPPET: setup

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void test_ofMaybePresentable_without_Presentable()
      {
        // given
        final var owner = mock(As.class);
        // when
        final var actualPm = PresentationModel.ofMaybePresentable(owner, roles);
        // then
        assertThat(actualPm.as(MockRole1.class), is(sameInstance(mockRole1)));
        assertThat(actualPm.as(MockRole2.class), is(sameInstance(mockRole2)));
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void test_ofMaybePresentable_with_Presentable()
      {
        // given
        final var owner = mock(As.class);
        final var presentable = mock(Presentable.class, CALLS_DEFAULT_METHODS);
        final var pm = mock(PresentationModel.class);
        when(presentable.createPresentationModel(anyCollection())).thenReturn(pm);
        when(owner.maybeAs(_Presentable_)).thenReturn(Optional.of(presentable));
        // when
        final var actualPm = PresentationModel.ofMaybePresentable(owner, roles);
        // then
        assertThat(actualPm, is(sameInstance(pm)));
        verify(presentable).createPresentationModel(eq(roles));
        verifyNoMoreInteractions(presentable);
      }
  }
