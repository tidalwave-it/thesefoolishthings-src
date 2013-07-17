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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.mock.VoidAsDelegateProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class AsSupportTest
  {
    public static class FixedAsDelegateProvider implements AsDelegateProvider
      {
        @Nonnull
        private final Object[] roles;

        public FixedAsDelegateProvider (final @Nonnull Object ... roles)
          {
            this.roles = roles;
          }

        @Nonnull
        public AsDelegate createAsDelegate (final @Nonnull Object datum)
          {
            return new AsDelegate()
              {
                public <T> T as (final @Nonnull Class<T> roleType,
                                 final @Nonnull As.NotFoundBehaviour<T> notFoundBehaviour)
                  {
                    for (final Object role : roles)
                      {
                        if (roleType.isAssignableFrom(role.getClass()))
                          {
                            return roleType.cast(role);
                          }
                      }

                    return notFoundBehaviour.run(new AsException(roleType));
                  }
              };
          }
      }

    public static interface Role1
      {
      }

    public static interface Role2
      {
      }

    @RequiredArgsConstructor @Getter
    public static class Role3
      {
        @Nonnull
        private final Object owner;
      }

    public static class RoleFactory3 implements RoleFactory<Object>
      {
        @Nonnull
        public Object createRoleFor (final @Nonnull Object owner)
          {
            return new Role3(owner);
          }
      }

    private Object owner;
    private Role1 localRole1;
    private Role2 localRole2;
    private Role2 delegateRole2;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        owner = new Object();
        localRole1 = mock(Role1.class);
        localRole2 = mock(Role2.class);
        delegateRole2 = mock(Role2.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_local_roles()
      {
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());
        final AsSupport fixture1 = new AsSupport(owner, localRole1);
        final AsSupport fixture2 = new AsSupport(owner, localRole1, localRole2);

        assertThat(fixture1.as(Role1.class), is(sameInstance(localRole1)));
        assertThat(fixture2.as(Role1.class), is(sameInstance(localRole1)));
        assertThat(fixture2.as(Role2.class), is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_role_from_factory()
      {
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());
        final AsSupport fixture = new AsSupport(owner, new RoleFactory3());

        final Role3 role = fixture.as(Role3.class);

        assertThat(role, is(notNullValue()));
        assertThat(role.getOwner(), is(sameInstance(owner)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role()
      {
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());
        final AsSupport fixture = new AsSupport(owner, localRole1);

        fixture.as(Role2.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role_bis()
      {
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());
        final AsSupport fixture = new AsSupport(owner, localRole2);

        fixture.as(Role1.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_roles_in_delegate()
      {
        AsDelegateProvider.Locator.set(new FixedAsDelegateProvider(delegateRole2));
        final AsSupport fixture = new AsSupport(owner);

        assertThat(fixture.as(Role2.class), is(sameInstance(delegateRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_give_priority_to_local_roles()
      {
        AsDelegateProvider.Locator.set(new FixedAsDelegateProvider(delegateRole2));
        final AsSupport fixture = new AsSupport(owner, localRole2);

        assertThat(fixture.as(Role2.class), is(sameInstance(localRole2)));
      }
  }