/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role.spring;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import it.tidalwave.role.spi.RoleManager;
import it.tidalwave.util.As;
import it.tidalwave.util.NotFoundException;
import org.springframework.beans.factory.annotation.Configurable;

/***********************************************************************************************************************
 *
 * An implementation for {@link As} based on Spring.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class SpringAsSupport implements As
  {
    @Inject @Nonnull
    private RoleManager roleManager;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T as (final @Nonnull Class<T> roleType)
      {
        return as(roleType, As.Defaults.throwAsException(roleType));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T as (final @Nonnull Class<T> roleType, final @Nonnull NotFoundBehaviour<T> notFoundBehaviour)
      {
        final List<? extends T> roles = roleManager.findRoles(this, roleType);

        if (roles.isEmpty())
          {
            return notFoundBehaviour.run(new NotFoundException("No " + roleType.getName() + " in " + this));
          }

        return roles.get(0);
      }
  }
