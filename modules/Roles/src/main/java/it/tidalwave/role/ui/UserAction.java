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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.role.ui.impl.DefaultUserAction;
import it.tidalwave.util.As;
import it.tidalwave.util.Callback;
import it.tidalwave.util.Parameters;
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
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public BoundProperty<Boolean> enabled();

    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-1 (replaces {@code new UserActionSupport()}
     * @since 3.2-ALPHA-3 (refactored}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserAction of (@Nonnull final Callback callback, @Nonnull final Collection<Object> rolesOrFactories)
      {
        return new DefaultUserAction(callback, rolesOrFactories);
      }

    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-3
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
     * @since 3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserAction of (@Nonnull final Callback callback)
      {
        return of(callback, Collections.emptyList());
      }
  }
