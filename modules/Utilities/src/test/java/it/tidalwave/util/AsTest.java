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
import org.testng.annotations.Test;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

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
        public <T> T as (@Nonnull final Class<T> type)
          {
            return as(type, As.Defaults.throwAsException(type));
          }

        @Override @Nonnull
        public <T> T as (@Nonnull final Class<T> type, @Nonnull final NotFoundBehaviour<T> notFoundBehaviour)
          {
            final Collection<T> roles = asMany(type);

            return roles.isEmpty() ? notFoundBehaviour.run(null) : roles.iterator().next();
          }

        @Override @Nonnull
        public <T> Collection<T> asMany (@Nonnull final Class<T> type)
          {
            return (Collection)delegate.as(type);
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
  }
