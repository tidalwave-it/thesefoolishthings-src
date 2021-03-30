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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import org.mockito.Mockito;

/***********************************************************************************************************************
 *
 * A simple implementation of {@link AsDelegateProvider} that might be enough for running tests. It just instantiates
 * a Mockito mock for each requested role.
 *
 * Install it with {@code AsDelegateProvider.Locator.set(new MockSimpleAsDelegateProvider());} in a {@code BeforeClass}
 * method.
 *
 * @author  Fabrizio Giudici
 *
 * @it.tidalwave.javadoc.experimental
 * @since 3.2-ALPHA-8
 *
 **********************************************************************************************************************/
public class MockSimpleAsDelegateProvider implements AsDelegateProvider
  {
    static class MockSimpleAsDelegate implements AsDelegate
      {
        @Override @Nonnull
        public <T> Collection<? extends T> as (@Nonnull final Class<T> roleType)
          {
            return Collections.singleton(Mockito.mock(roleType));
          }
      }

    @Override @Nonnull
    public AsDelegate createAsDelegate (@Nonnull final Object datum)
      {
        return new MockSimpleAsDelegate();
      }
  }