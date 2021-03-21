/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.impl.EmptyAsDelegateProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class AsSupportTest
  {
    @RequiredArgsConstructor
    public static class FixedAsDelegateProvider implements AsDelegateProvider
      {
        @Nonnull
        private final Collection<Object> roles;

        @Override @Nonnull
        public AsDelegate createAsDelegate (@Nonnull final Object datum)
          {
            return new AsDelegate()
              {
                @Override @Nonnull
                public <T> Collection<? extends T> as (@Nonnull final Class<? extends T> roleType)
                  {
                    final List<T> result = new ArrayList<>();

                    for (final Object role : roles)
                      {
                        if (roleType.isAssignableFrom(role.getClass()))
                          {
                            result.add(roleType.cast(role));
                          }
                      }

                    return result;
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
        @Override @Nonnull
        public Object createRoleFor (@Nonnull final Object owner)
          {
            return new Role3(owner);
          }
      }

    private Object owner;
    private Role1 localRole1;
    private Role2 localRole2;
    private Role2 localRole2b;
    private Role2 delegateRole2;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        AsDelegateProvider.Locator.set(new EmptyAsDelegateProvider()); // reset
        owner = new Object();
        localRole1 = mock(Role1.class);
        localRole2 = mock(Role2.class);
        localRole2b = mock(Role2.class);
        delegateRole2 = mock(Role2.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_local_roles()
      {
        // given
        final AsSupport underTest1 = new AsSupport(owner, r(localRole1));
        final AsSupport underTest2 = new AsSupport(owner, r(localRole1, localRole2));
        // when
        final Role1 ut1role1 = underTest1.as(Role1.class);
        final Role1 ut2Role1 = underTest2.as(Role1.class);
        final Role2 ut2Role2 = underTest2.as(Role2.class);
        // then
        assertThat(ut1role1, is(sameInstance(localRole1)));
        assertThat(ut2Role1, is(sameInstance(localRole1)));
        assertThat(ut2Role2, is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_role_from_factory()
      {
        // given
        final AsSupport underTest = new AsSupport(owner, r(new RoleFactory3()));
        // when
        final Role3 role = underTest.as(Role3.class);
        // then
        assertThat(role, is(notNullValue()));
        assertThat(role.getOwner(), is(sameInstance(owner)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_not_find_inexistent_role()
      {
        // given
        final AsSupport underTest = new AsSupport(owner, r(localRole1));
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
        final AsSupport underTest = new AsSupport(owner, r(localRole2));
        // when
        underTest.as(Role1.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_find_roles_in_delegate()
      {
        // given
        AsDelegateProvider.Locator.set(new FixedAsDelegateProvider(r(delegateRole2)));
        final AsSupport underTest = new AsSupport(owner);
        // when
        final Role2 role = underTest.as(Role2.class);
        // then
        assertThat(role, is(sameInstance(delegateRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_give_priority_to_local_roles()
      {
        // given
        AsDelegateProvider.Locator.set(new FixedAsDelegateProvider(r(delegateRole2)));
        final AsSupport underTest = new AsSupport(owner, r(localRole2));
        // when
        final Role2 role = underTest.as(Role2.class);
        // then
        assertThat(role, is(sameInstance(localRole2)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_retrieve_multiple_local_roles()
      {
        // given
        final AsSupport underTest = new AsSupport(owner, r(localRole2, localRole2b));
        // when
        final Collection<Role2> roles = underTest.asMany(Role2.class);
        // then
        assertThat("" + roles, roles.size(), is(2));
        assertThat("" + roles, roles.contains(localRole2), is(true));
        assertThat("" + roles, roles.contains(localRole2b), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_retrieve_multiple_local_and_global_roles()
      {
        // given
        AsDelegateProvider.Locator.set(new FixedAsDelegateProvider(r(delegateRole2)));
        final AsSupport underTest = new AsSupport(owner, r(localRole2, localRole2b));
        // when
        final Collection<Role2> roles = underTest.asMany(Role2.class);
        // then
        assertThat("" + roles, roles.size(), is(3));
        assertThat("" + roles, roles.contains(localRole2), is(true));
        assertThat("" + roles, roles.contains(localRole2b), is(true));
        assertThat("" + roles, roles.contains(delegateRole2), is(true));
      }
  }