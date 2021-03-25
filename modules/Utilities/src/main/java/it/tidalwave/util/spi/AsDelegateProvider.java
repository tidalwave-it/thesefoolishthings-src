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
import java.util.Iterator;
import java.util.ServiceLoader;
import it.tidalwave.util.impl.EmptyAsDelegateProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface AsDelegateProvider
  {
    public static final Class<AsDelegateProvider> _AsDelegateProvider_ = AsDelegateProvider.class;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Locator
      {
        private static AsDelegateProvider asSpiProvider;

        @Nonnull
        public static synchronized AsDelegateProvider find()
          {
            if (asSpiProvider == null)
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

                asSpiProvider = i.next();
              }

            return asSpiProvider;
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
            asSpiProvider = provider;
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
            asSpiProvider = null;
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
        return new EmptyAsDelegateProvider();
      }
  }
