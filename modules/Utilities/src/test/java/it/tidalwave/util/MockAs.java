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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.spi.AsSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * A provider of static factory methods for creating mocks with {@link As} support.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockAs
  {
    /***********************************************************************************************************************
     *
     * Creates a mock with Mockito that fully supports {@link As}.
     *
     * @param   clazz               the mock class
     * @return                      the mock
     * @since                       3.2-ALPHA-3 (refactored)
     *
     **********************************************************************************************************************/
    @Nonnull
    public static <T extends As> T mockWithAsSupport (@Nonnull final Class<T> clazz)
      {
        return mockWithAsSupport(clazz, Collections.emptyList());
      }

    /***********************************************************************************************************************
     *
     * Creates a mock with Mockito that fully supports {@link As}.
     *
     * @param   clazz               the mock class
     * @param   roles               a collection of roles or factories for roles
     * @return                      the mock
     * @since                       3.2-ALPHA-3 (refactored)
     *
     **********************************************************************************************************************/
    @Nonnull
    public static <T extends As> T mockWithAsSupport (@Nonnull final Class<T> clazz,
                                                      @Nonnull final Collection<Object> roles)
      {
        final T mock = mock(clazz);
        final AsSupport asSupport = new AsSupport(mock, roles);
        when(mock.as(any(Class.class))).thenAnswer(new Answer<Object>()
          {
            @Override @Nonnull
            public Object answer (@Nonnull final InvocationOnMock invocation)
              {
                final Class<?> roleType = (Class<?>)invocation.getArguments()[0];
                return asSupport.as(roleType);
              }
          });

        return mock;
      }
  }
