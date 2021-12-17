/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.As;
import it.tidalwave.util.Callback;
import it.tidalwave.util.Parameters;
import it.tidalwave.role.ui.impl.DefaultUserAction;
// import javafx.beans.property.BooleanProperty;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface UserAction extends As
  {
    public static final Class<UserAction> _UserAction_ = UserAction.class;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void actionPerformed();

    /*******************************************************************************************************************
     *
     * Returns the property describing the enabled status of this action.
     *
     * @return      the enabled property
     *
     ******************************************************************************************************************/
    @Nonnull
    public BoundProperty<Boolean> enabled(); // TODO: rename to enabledProperty()

    /*******************************************************************************************************************
     *
     * Creates a new instance out of a callback and a collection of roles.
     *
     * @param   callback    the callback
     * @param   roles       the roles (or role factories)
     * @return              the new instance
     * @since               3.2-ALPHA-1 (replaces {@code new UserActionSupport()}
     * @since               3.2-ALPHA-3 (refactored}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserAction of (@Nonnull final Callback callback, @Nonnull final Collection<Object> roles)
      {
        return new DefaultUserAction(callback, roles);
      }

    /*******************************************************************************************************************
     *
     * Creates a new instance out of a callback and a role (typically a {@link Displayable}.
     *
     * @param   callback    the callback
     * @param   role        the role (or role factory)
     * @return              the new instance
     * @since               3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserAction of (@Nonnull final Callback callback, @Nonnull final Object role)
      {
        Parameters.mustNotBeArrayOrCollection(role, "role");
        return of(callback, Collections.singletonList(role));
      }

    /*******************************************************************************************************************
     *
     * Creates a new instance out of a callback.
     *
     * @param   callback    the callback
     * @return              the new instance
     * @since 3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserAction of (@Nonnull final Callback callback)
      {
        return of(callback, Collections.emptyList());
      }
  }
