/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class AsExtensionsBean
  {
    @Inject @Nonnull
    private RoleManager roleManager;

    public <T> T as (final @Nonnull Object datum,
                     final @Nonnull Class<T> roleType,
                     final @Nonnull As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        assert roleManager != null : "roleManager not present or not injected";
        final List<? extends T> roles = roleManager.findRoles(datum, roleType);

        if (roles.isEmpty())
          {
            return notFoundBehaviour.run(new NotFoundException("No " + roleType.getName() + " in " + datum));
          }

        return roles.get(0);
      }
  }
