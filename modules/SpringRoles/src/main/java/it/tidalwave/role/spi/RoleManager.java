/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - Roles (Spring)
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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.List;

/***********************************************************************************************************************
 *
 * A service which retrieves DCI Roles for a given object.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface RoleManager
  {
    /*******************************************************************************************************************
     *
     * Retrieves the roles of the given class for the given owner object.
     *
     * @param   owner      the owner object
     * @param   roleType   the role type
     * @return             a list of roles
     *
     ******************************************************************************************************************/
    @Nonnull
    public <RoleType> List<? extends RoleType> findRoles (@Nonnull Object owner, @Nonnull Class<RoleType> roleType);
  }
