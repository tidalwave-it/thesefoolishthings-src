/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role-spring
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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.spi.RoleManager;
import org.springframework.beans.factory.annotation.Configurable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Configurable
public class AsExtensionsBean
  {
    @Inject @Nonnull
    private RoleManager roleManager;

    public <T> T as (@Nonnull final Object datum,
                     @Nonnull final Class<T> roleType,
                     @Nonnull final As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        final List<? extends T> roles = asMany(datum, roleType);

        if (roles.isEmpty())
          {
            return notFoundBehaviour.run(new NotFoundException("No " + roleType.getName() + " in " + datum));
          }

        return roles.get(0);
      }

    public <T> List<? extends T> asMany (@Nonnull final Object datum, @Nonnull final Class<T> roleType)
      {
        assert roleManager != null : "roleManager not present or not injected";
        return roleManager.findRoles(datum, roleType);
      }
  }
