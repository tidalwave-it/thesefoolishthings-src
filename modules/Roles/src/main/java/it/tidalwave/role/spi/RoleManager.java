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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import it.tidalwave.util.LazySupplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A service which retrieves DCI Roles for a given object.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface RoleManager
  {
    /*******************************************************************************************************************
     *
     * A locator for the {@link RoleManager} which uses the {@link ServiceLoader} facility to be independent of
     * any DI framework.
     *
     ******************************************************************************************************************/
    @Slf4j @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Locator
      {
        private static final LazySupplier<RoleManager> ROLE_MANAGER_REF =
                LazySupplier.of(RoleManager.Locator::findRoleManager);

        private static final LazySupplier<RoleManagerProvider> ROLE_MANAGER_PROVIDER_REF =
                LazySupplier.of(RoleManager.Locator::findRoleManagerProvider);

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        public static RoleManager find()
          {
            return ROLE_MANAGER_REF.get();
          }

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        private static RoleManager findRoleManager()
          {
            return Objects.requireNonNull(ROLE_MANAGER_PROVIDER_REF.get().getRoleManager(),
                                          "Cannot find RoleManager");
          }

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        private static RoleManagerProvider findRoleManagerProvider()
          {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Iterator<RoleManagerProvider> i =
                    ServiceLoader.load(RoleManagerProvider.class, classLoader).iterator();

            if (!i.hasNext())
              {
                throw new RuntimeException("No ServiceProvider for RoleManagerProvider");
              }

            final RoleManagerProvider roleManagerProvider = Objects.requireNonNull(i.next(),
                                                                                         "roleManagerProvider is null");
            assert roleManagerProvider != null; // for SpotBugs
            log.info("RoleManagerProvider instantiated from META-INF: {}", roleManagerProvider);
            return roleManagerProvider;
          }
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
    public <T> List<? extends T> findRoles (@Nonnull Object owner, @Nonnull Class<T> roleType);
  }
