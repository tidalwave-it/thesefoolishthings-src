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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import it.tidalwave.util.spi.AsDelegate;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * An extension of {@link As} for Java 8 which makes use of {@link Optional}.
 *
 * @since   3.1-ALPHA-2
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AsTest
  {
    static class Role
      {
      }

    @RequiredArgsConstructor
    static class UnderTest implements As
      {
        private final AsDelegate delegate;

        @Override @Nonnull
        public <T> Optional<T> maybeAs (@Nonnull final Class<T> type)
          {
            final Collection<T> roles = asMany(type);
            return roles.isEmpty() ? Optional.empty() : Optional.of(roles.iterator().next());
          }

        @Override @Nonnull
        public <T> Collection<T> asMany (@Nonnull final Class<T> type)
          {
            return (Collection<T>)delegate.as(type);
          }
      }

    @Test
    public void must_return_a_filled_Optional_when_the_role_is_present()
      {
        // given
        final AsDelegate delegate = mock(AsDelegate.class);
        final Role role = new Role();
        when(delegate.as(Role.class)).thenReturn((List)Arrays.asList(role));
        // when
        final UnderTest underTest = new UnderTest(delegate);
        // then
        final Optional<Role> result = underTest.maybeAs(Role.class);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(sameInstance(role)));
      }

    @Test
    public void must_return_an_empty_Optional_when_the_role_is_not_present()
      {
        // given
        final AsDelegate delegate = mock(AsDelegate.class);
        when(delegate.as(Role.class)).thenReturn(Collections.emptyList());
        // when
        final UnderTest underTest = new UnderTest(delegate);
        // then
        final Optional<Role> result = underTest.maybeAs(Role.class);
        assertThat(result.isPresent(), is(false));
      }

    static interface RoleWithGeneric<T>
      {
      }

    private static final As.Ref<RoleWithGeneric<String>> _roleOfStrings_ = As.ref(RoleWithGeneric.class);

    @Test
    public void as_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final As underTest = mock(As.class);
        when(underTest.as(_roleOfStrings_)).thenReturn(role);
        // when
        final RoleWithGeneric<String> actualRole = underTest.as(_roleOfStrings_);
        // then
        assertThat(actualRole, is(role));
      }

    @Test
    public void maybeAs_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final As underTest = mock(As.class);
        when(underTest.maybeAs(_roleOfStrings_)).thenReturn(Optional.of(role));
        // when
        final Optional<RoleWithGeneric<String>> actualRole = underTest.maybeAs(_roleOfStrings_);
        // then
        assertThat(actualRole.get(), is(role));
      }

    @Test
    public void asMany_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final As underTest = mock(As.class);
        when(underTest.asMany(_roleOfStrings_)).thenReturn(Arrays.asList(role));
        // when
        final Collection<RoleWithGeneric<String>> actualRoles = underTest.asMany(_roleOfStrings_);
        // then
        assertThat(actualRoles, is(Arrays.asList(role)));
      }
  }
