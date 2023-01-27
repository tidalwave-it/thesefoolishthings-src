/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.impl;

import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.mock.MockAsFactory;
import it.tidalwave.role.spi.SystemRoleFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.util.Parameters.r;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultPresentationModelTest
  {
    public static interface Role1
      {
      }

    public static interface Role2
      {
      }

    public static interface Role3
      {
      }

    private Role1 localRole1;
    private Role2 localRole2;
    private Role2 role2InOwner;
    private Object ownerNoAs;
    private As ownerAsWithNoRoles;
    private As ownerAsWithRole2;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        // Not called by tests, we only need it's there
        SystemRoleFactory.reset();

        localRole1 = mock(Role1.class);
        localRole2 = mock(Role2.class);
        role2InOwner = mock(Role2.class);
        ownerNoAs = new Object();

        ownerAsWithNoRoles = MockAsFactory.mockWithAs(As.class);
        ownerAsWithRole2 = MockAsFactory.mockWithAs(As.class, r(role2InOwner));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_local_roles()
      {
        // given
        final var underTest1 = new DefaultPresentationModel(ownerNoAs, r(localRole1));
        final var underTest2 = new DefaultPresentationModel(ownerNoAs, r(localRole1, localRole2));
        // when
        final var ut1Role1 = underTest1.as(Role1.class);
        final var ut2Role1 = underTest2.as(Role1.class);
        final var ut2Role2 = underTest2.as(Role2.class);
        //then
        assertThat(ut1Role1, is(sameInstance(localRole1)));
        assertThat(ut2Role1, is(sameInstance(localRole1)));
        assertThat(ut2Role2, is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role()
      {
        // given
        final var underTest = new DefaultPresentationModel(ownerNoAs, r(localRole1));
        // when
        underTest.as(Role2.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role_bis()
      {
        // given
        final var underTest = new DefaultPresentationModel(ownerAsWithRole2, r(localRole2));
        // when
        underTest.as(Role1.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_roles_in_owner()
      {
        // given
        final var underTest = new DefaultPresentationModel(ownerAsWithRole2, r());
        // when
        final var role2 = underTest.as(Role2.class);
        // then
        assertThat(role2, is(sameInstance(role2InOwner)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_give_priority_to_local_roles()
      {
        // given
        final var underTest = new DefaultPresentationModel(ownerAsWithRole2, r(localRole2));
        // when
        final var role2 = underTest.as(Role2.class);
        // then
        assertThat(role2, is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_TFT_248_regression()
      {
        // given
        final var underTest = new DefaultPresentationModel(ownerAsWithRole2, r(localRole2));
        // when
        final var role3 = underTest.maybeAs(Role3.class);
        // then
        assertThat(role3.isPresent(), is(false));
      }
  }