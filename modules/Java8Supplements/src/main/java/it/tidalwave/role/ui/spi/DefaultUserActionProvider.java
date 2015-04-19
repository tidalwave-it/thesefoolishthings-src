/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.UserActionProvider;

/***********************************************************************************************************************
 *
 * A default implementation of {@link UserActionProvider} which returns no actions.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultUserActionProvider implements UserActionProvider
  {
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Collection<? extends UserAction> getActions()
      {
        return Collections.emptyList();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public UserAction getDefaultAction()
      throws NotFoundException
      {
        throw new NotFoundException("No default UserAction");
      }
  }