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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import it.tidalwave.util.impl.LazyReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface AsDelegateProvider
  {
    public static final LazyReference<EmptyAsDelegateProvider> EMPTY_REF =
            LazyReference.of(EmptyAsDelegateProvider::new);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Locator
      {
        private static final LazyReference<AsDelegateProvider> AS_DELEGATE_PROVIDER_REF =
                LazyReference.of(Locator::findAsSpiProvider);

        @Nonnull
        public static AsDelegateProvider find()
          {
            return AS_DELEGATE_PROVIDER_REF.get();
          }

        @Nonnull
        private static AsDelegateProvider findAsSpiProvider()
          {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Iterator<AsDelegateProvider> i =
                    ServiceLoader.load(AsDelegateProvider.class, classLoader).iterator();

            if (!i.hasNext())
              {
                final String message = "No ServiceProvider for AsDelegateProvider found in a ServiceLoader - if " +
                                       "you are running tests perhaps you should first call " +
                                       "AsDelegateProvider.Locator.set(AsDelegateProvider.empty()) or " +
                                       "AsDelegateProvider.Locator.set(new MockSimpleAsDelegateProvider()) or " +
                                       "another appropriate AsDelegateProvider";
                throw new RuntimeException(message);
              }

            return i.next();
          }

        /***************************************************************************************************************
         *
         * <b>This method is for testing only.</b> Sets the global {@link AsDelegateProvider}.
         *
         * @param   provider    the provider
         * @see     #reset() 
         *
         **************************************************************************************************************/
        public static void set (@Nonnull final AsDelegateProvider provider)
          {
            AS_DELEGATE_PROVIDER_REF.set(provider);
          }

        /***************************************************************************************************************
         *
         * <b>This method is for testing only.</b> Resets the global {@link AsDelegateProvider}; it must be called at
         * the test completion whenever {@link #set(AsDelegateProvider)} has been called, to avoid polluting the
         * context of further tests.
         * 
         * @see     #set(AsDelegateProvider)
         *
         **************************************************************************************************************/
        public static void reset()
          {
            AS_DELEGATE_PROVIDER_REF.clear();
          }
      }

    /*******************************************************************************************************************
     *
     * Creates an {@link AsDelegate} for the given object
     *
     * @param     datum   the object
     * @return            {@code AsDelegate}
     *
     ******************************************************************************************************************/
    @Nonnull
    public AsDelegate createAsDelegate (@Nonnull Object datum);

    /*******************************************************************************************************************
     *
     * Returns an empty implementation. Useful for setting up test environment.
     *
     * <pre>
     * AsDelegateProvider.Locator.set(AsDelegateProvider.empty());
     * </pre>
     *
     * @return    the empty implementation
     * @since     3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static AsDelegateProvider empty()
      {
        return EMPTY_REF.get();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static class EmptyAsDelegateProvider implements AsDelegateProvider
      {
        @Override @Nonnull
        public AsDelegate createAsDelegate (@Nonnull final Object owner)
          {
            return new AsDelegate()
              {
                @Override @Nonnull
                public <T> Collection<T> as (@Nonnull final Class<T> type)
                  {
                    return new ArrayList<>(); // must be mutable
                  }
              };
          }
      }
  }
