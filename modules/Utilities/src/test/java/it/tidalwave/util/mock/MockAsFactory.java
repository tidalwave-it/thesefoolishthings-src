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
package it.tidalwave.util.mock;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.As;
import it.tidalwave.util.impl.DefaultAs;
import it.tidalwave.util.spi.AsDelegateProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * A provider of static factory methods for creating mocks with {@link As} support.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockAsFactory
  {
    /*******************************************************************************************************************
     *
     * Creates a mock with Mockito that fully supports {@link As}.
     *
     * @param   clazz               the mock class
     * @return                      the mock
     * @since                       3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> T mockWithAs (@Nonnull final Class<T> clazz)
      {
        return mockWithAs(clazz, Collections.emptyList());
      }

    /*******************************************************************************************************************
     *
     * Creates a mock with Mockito that fully supports {@link As}. This method doesn't call
     * {@link AsDelegateProvider.Locator#find()}.
     *
     * @param   clazz               the mock class
     * @param   roles               a collection of roles or factories for roles
     * @return                      the mock
     * @since                       3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> T mockWithAs (@Nonnull final Class<T> clazz,
                                               @Nonnull final Collection<Object> roles)
      {
        final var mock = mock(clazz);
        final As as = new DefaultAs(AsDelegateProvider.empty()::createAsDelegate, mock, roles);
        when(mock.as(any(Class.class))).thenCallRealMethod();
        when(mock.maybeAs(any(Class.class))).thenAnswer(i -> as.maybeAs((Class<?>)i.getArguments()[0]));
        when(mock.asMany(any(Class.class))).thenAnswer(i -> as.asMany((Class<?>)i.getArguments()[0]));
        when(mock.as(any(As.Type.class))).thenCallRealMethod();
        when(mock.maybeAs(any(As.Type.class))).thenCallRealMethod();
        when(mock.asMany(any(As.Type.class))).thenCallRealMethod();

        return mock;
      }
  }
