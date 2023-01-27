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
import java.util.List;
import it.tidalwave.util.LazySupplier;
import static it.tidalwave.role.impl.ServiceLoaderLocator.lazySupplierOf;

/***********************************************************************************************************************
 *
 * A service which retrieves DCI Roles for a given object.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface SystemRoleFactory
  {
    static class Inner
      {
        private static final LazySupplier<SystemRoleFactory> INSTANCE_REF =
                LazySupplier.of(() -> Inner.PROVIDER_REF.get().getSystemRoleFactory());

        private static final LazySupplier<SystemRoleFactoryProvider> PROVIDER_REF =
                lazySupplierOf(SystemRoleFactoryProvider.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    public static SystemRoleFactory getInstance()
      {
        return Inner.INSTANCE_REF.get();
      }

    /*******************************************************************************************************************
     *
     * Removes a previously installed {@link SystemRoleFactory}. <b>This method is for testing only (used to clean up a
     * testing context).</b>
     *
     * @see     #set(SystemRoleFactory)
     *
     ******************************************************************************************************************/
    public static void reset()
      {
        Inner.PROVIDER_REF.clear();
        Inner.INSTANCE_REF.clear();
      }

    /*******************************************************************************************************************
     *
     * Installs a {@link SystemRoleFactory}. <b>This method is for testing only (used to set up a testing context).</b>
     *
     * @param   systemRoleFactory   the {@link SystemRoleFactory}
     * @see     #reset()
     *
     ******************************************************************************************************************/
    public static void set (@Nonnull final SystemRoleFactory systemRoleFactory)
      {
        Inner.PROVIDER_REF.set(() -> systemRoleFactory);
      }

    /*******************************************************************************************************************
     *
     * Retrieves the roles of the given class for the given owner object.
     *
     * @param <T>           the static type of the roles
     * @param   owner       the owner object
     * @param   roleType    the dynamic type of the roles
     * @return              a list of roles
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> List<T> findRoles (@Nonnull Object owner, @Nonnull Class<? extends T> roleType);
  }
