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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import it.tidalwave.util.spi.AsDelegate;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;
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
            final var roles = asMany(type);
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
        final var delegate = mock(AsDelegate.class);
        final var role = new Role();
        when(delegate.as(Role.class)).thenReturn((List)List.of(role));
        // when
        final var underTest = new UnderTest(delegate);
        // then
        final var result = underTest.maybeAs(Role.class);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(sameInstance(role)));
      }

    @Test
    public void must_return_an_empty_Optional_when_the_role_is_not_present()
      {
        // given
        final var delegate = mock(AsDelegate.class);
        when(delegate.as(Role.class)).thenReturn(Collections.emptyList());
        // when
        final var underTest = new UnderTest(delegate);
        // then
        final var result = underTest.maybeAs(Role.class);
        assertThat(result.isPresent(), is(false));
      }

    static interface RoleWithGeneric<T>
      {
      }

    private static final As.Type<RoleWithGeneric<String>> _roleOfStrings_ = As.type(RoleWithGeneric.class);

    @Test
    public void as_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final var underTest = mock(As.class);
        when(underTest.as(_roleOfStrings_)).thenReturn(role);
        // when
        final var actualRole = underTest.as(_roleOfStrings_);
        // then
        assertThat(actualRole, is(role));
      }

    @Test
    public void maybeAs_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final var underTest = mock(As.class);
        when(underTest.maybeAs(_roleOfStrings_)).thenReturn(Optional.of(role));
        // when
        final var actualRole = underTest.maybeAs(_roleOfStrings_);
        // then
        assertThat(actualRole.get(), is(role));
      }

    @Test
    public void asMany_with_ref_must_properly_work()
      {
        // given
        final RoleWithGeneric<String> role = mock(RoleWithGeneric.class);
        final var underTest = mock(As.class);
        when(underTest.asMany(_roleOfStrings_)).thenReturn(List.of(role));
        // when
        final var actualRoles = underTest.asMany(_roleOfStrings_);
        // then
        assertThat(actualRoles, is(List.of(role)));
      }

    // START SNIPPET: dataretriever
    interface DataRetriever<T>
      {
        public List<T> retrieve();
      }
    // END SNIPPET: dataretriever

    @SuppressWarnings("LocalCanBeFinal")
    static class AsTypeCodeSample
      {
        // START SNIPPET: as_Type
        private static final As.Type<DataRetriever<String>> _StringRetriever_ = As.type(DataRetriever.class);
        private static final As.Type<DataRetriever<LocalDate>> _LocalDateRetriever_ = As.type(DataRetriever.class);
        // END SNIPPET: as_Type

        @SuppressWarnings("RedundantExplicitVariableType")
        public void method (As object1, As object2)
          {
            // The assignments below raise a warning ('Unchecked assignment').
            // START SNIPPET: as1
            List<String> f1 = object1.as(DataRetriever.class).retrieve();
            List<LocalDate> f2 = object2.as(DataRetriever.class).retrieve();
            // END SNIPPET: as1
            // The assignments below are fine (at the expense of a warning in the declarations of As.Types).
            // Don't use 'var' otherwise the code doesn't clearly explain the concept
            // START SNIPPET: as2
            List<String> f3 = object1.as(_StringRetriever_).retrieve();
            List<LocalDate> f4 = object2.as(_LocalDateRetriever_).retrieve();
            // END SNIPPET: as2
          }
      }

    static class AsImplementationCodeSample1
      {
        @SuppressWarnings({"InnerClassMayBeStatic", "LocalCanBeFinal"})
        // START SNIPPET: as_impl_1
        class MyObject implements As
          {
            private final As delegate = As.forObject(this);

            @Override @Nonnull
            public <T> Optional<T> maybeAs (@Nonnull Class<T> type)
              {
                return delegate.maybeAs(type);
              }

            @Override @Nonnull
            public <T> Collection<T> asMany (@Nonnull Class<T> type)
              {
                return delegate.asMany(type);
              }
          }
        // END SNIPPET: as_impl_1
      }

    static class AsImplementationCodeSample2
      {
        @SuppressWarnings("InnerClassMayBeStatic")
        // START SNIPPET: as_impl_2
        @EqualsAndHashCode(exclude = "delegate") @ToString(exclude = "delegate")
        class MyObject implements As
          {
            @Delegate
            private final As delegate = As.forObject(this);
          }
        // END SNIPPET: as_impl_2
      }
  }
