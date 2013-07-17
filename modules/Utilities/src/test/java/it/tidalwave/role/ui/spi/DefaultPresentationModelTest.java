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
package it.tidalwave.role.ui.spi;

import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.MockAs;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.util.mock.VoidAsDelegateProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
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
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());

        localRole1 = mock(Role1.class);
        localRole2 = mock(Role2.class);
        role2InOwner = mock(Role2.class);
        ownerNoAs = new Object();

        ownerAsWithNoRoles = MockAs.mockWithAsSupport(As.class);
        ownerAsWithRole2 = MockAs.mockWithAsSupport(As.class, role2InOwner);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_local_roles()
      {
        final DefaultPresentationModel fixture1 = new DefaultPresentationModel(ownerNoAs, localRole1);
        final DefaultPresentationModel fixture2 = new DefaultPresentationModel(ownerNoAs, localRole1, localRole2);

        assertThat(fixture1.as(Role1.class), is(sameInstance(localRole1)));
        assertThat(fixture2.as(Role1.class), is(sameInstance(localRole1)));
        assertThat(fixture2.as(Role2.class), is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role()
      {
        final DefaultPresentationModel fixture = new DefaultPresentationModel(ownerNoAs, localRole1);

        fixture.as(Role2.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role_bis()
      {
        final DefaultPresentationModel fixture = new DefaultPresentationModel(ownerAsWithRole2, localRole2);

        fixture.as(Role1.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_roles_in_owner()
      {
        final DefaultPresentationModel fixture = new DefaultPresentationModel(ownerAsWithRole2);

        assertThat(fixture.as(Role2.class), is(sameInstance(role2InOwner)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_give_priority_to_local_roles()
      {
        final DefaultPresentationModel fixture = new DefaultPresentationModel(ownerAsWithRole2, localRole2);

        assertThat(fixture.as(Role2.class), is(sameInstance(localRole2)));
      }
  }