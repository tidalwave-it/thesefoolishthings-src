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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import it.tidalwave.util.LazySupplier;
import it.tidalwave.role.impl.ServiceLoaderLocator;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * The provider of the singleton {@link OwnerRoleFactory}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface OwnerRoleFactoryProvider
  {
    static class Inner // TODO: will go away with Java 17
      {
        private static final LazySupplier<OwnerRoleFactoryProvider> PROVIDER_REF =
                LazySupplier.of(() -> ServiceLoaderLocator.findService(OwnerRoleFactoryProvider.class));

        private static final LazySupplier<EmptyOwnerRoleFactory> EMPTY_REF =
                LazySupplier.of(EmptyOwnerRoleFactory::new);
      }

    /*******************************************************************************************************************
     *
     * Returns the singleton instance of {@link OwnerRoleFactoryProvider}
     *
     * @return    the singleton instance
     *
     ******************************************************************************************************************/
    @Nonnull
    public static OwnerRoleFactoryProvider getInstance ()
      {
        return Inner.PROVIDER_REF.get();
      }

    /*******************************************************************************************************************
     *
     * Creates an {@link OwnerRoleFactory} for the given object
     *
     * @param     owner   the object
     * @return            {@code AsDelegate}
     *
     ******************************************************************************************************************/
    @Nonnull
    public OwnerRoleFactory createRoleFactory (@Nonnull Object owner);

    /*******************************************************************************************************************
     *
     * Installs a {@link OwnerRoleFactory}. <b>This method is for testing only (used to set up a testing context).</b>
     *
     * @param   ownerRoleFactory    the {@link OwnerRoleFactory}
     * @see     #reset()
     *
     ******************************************************************************************************************/
    public static void set (@Nonnull final OwnerRoleFactory ownerRoleFactory)
      {
        Inner.PROVIDER_REF.set(new SimpleOwnerRoleFactoryProvider(ownerRoleFactory));
      }

    /*******************************************************************************************************************
     *
     * Removes a previously installed {@link OwnerRoleFactory}. <b>This method is for testing only (used to clean up a
     * testing context).</b>
     *
     * @see     #set(OwnerRoleFactory)
     *
     ******************************************************************************************************************/
    public static void reset()
      {
        Inner.PROVIDER_REF.clear();
      }

    /*******************************************************************************************************************
     *
     * Returns an empty implementation of factory. Useful for setting up a test environment.
     *
     * @return    the empty implementation
     * @since     3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static OwnerRoleFactory emptyRoleFactory()
      {
        return Inner.EMPTY_REF.get();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class SimpleOwnerRoleFactoryProvider implements OwnerRoleFactoryProvider
      {
        @Nonnull
        private final OwnerRoleFactory ownerRoleFactory;

        @Override @Nonnull
        public OwnerRoleFactory createRoleFactory (@Nonnull final Object owner)
          {
            return ownerRoleFactory;
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static class EmptyOwnerRoleFactory implements OwnerRoleFactory
      {
        @Override @Nonnull
        public <T> Collection<T> findRoles (@Nonnull final Class<? extends T> type)
          {
            return new ArrayList<>(); // must be mutable
          }
      }
  }
