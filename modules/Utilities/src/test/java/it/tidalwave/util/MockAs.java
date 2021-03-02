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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;
import it.tidalwave.util.spi.AsSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/***********************************************************************************************************************
 *
 * A provider of static factory methods for creating mocks with {@link As} support.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
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
     * @param   rolesOrFactories    a collection of roles or factories for roles
     * @return                      the mock
     *
     **********************************************************************************************************************/
    @Nonnull
    public static <T extends As> T mockWithAsSupport (final @Nonnull Class<T> clazz,
                                                      final @Nonnull Object ... rolesOrFactories)
      {
        final T mock = mock(clazz);
        final AsSupport asSupport = new AsSupport(mock, rolesOrFactories);
        when(mock.as(any(Class.class))).thenAnswer(new Answer<Object>()
          {
            @Override @Nonnull
            public Object answer (final @Nonnull InvocationOnMock invocation)
              {
                final Class<?> roleType = (Class<?>)invocation.getArguments()[0];
                return asSupport.as(roleType);
              }
          });

        return mock;
      }
  }
