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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.As;
import it.tidalwave.util.NamedCallback;
import it.tidalwave.util.Parameters;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.impl.DefaultPresentationModel;
import static it.tidalwave.util.Parameters.r;
import static it.tidalwave.role.ui.Presentable._Presentable_;

/***********************************************************************************************************************
 *
 * TODO: As the NetBeans Node, it should allow children, have event listeners for children added/removed/changed.
 * This class so becomes the true M in MVC.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface PresentationModel extends As
  {
    public static final Class<PresentationModel> PresentationModel = PresentationModel.class;

    public static final As.Type<SimpleComposite<PresentationModel>> _SimpleCompositeOfPresentationModel_ =
            As.type(SimpleComposite.class);

    public static final String PROPERTY_CHILDREN = "children";

    /** This is an undocumented feature. If you add a {@link NamedCallback} with this name as a role in this object, it
     * will be called back when {@link #dispose()} is called. */
    public static final String CALLBACK_DISPOSE = "dispose";

    /*******************************************************************************************************************
     *
     * Disposes this object.
     *
     ******************************************************************************************************************/
    public void dispose();

    /*******************************************************************************************************************
     *
     * Adds a {@link PropertyChangeListener}.
     *
     * @param listener    the listener
     *
     ******************************************************************************************************************/
    public void addPropertyChangeListener (@Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     * Adds a {@link PropertyChangeListener} for the given property.
     *
     * @param propertyName  the name of the property
     * @param listener      the listener
     *
     ******************************************************************************************************************/
    public void addPropertyChangeListener (@Nonnull String propertyName, @Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     * Removes a {@link PropertyChangeListener}.
     *
     * @param listener    the listener
     *
     ******************************************************************************************************************/
    public void removePropertyChangeListener (@Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     * Removes a {@link PropertyChangeListener} for the given property.
     *
     * @param propertyName  the name of the property
     * @param listener      the listener
     *
     ******************************************************************************************************************/
    public void removePropertyChangeListener (@Nonnull String propertyName, @Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     * Checks whether the given property has been bound to listeners.
     *
     * @param propertyName  the name of the property
     * @return              {@code true} if the property is bound
     *
     ******************************************************************************************************************/
    public boolean hasListeners (@Nonnull String propertyName);

    /*******************************************************************************************************************
     *
     * Returns all the bound {@link PropertyChangeListener}s.
     *
     * @return              the listeners
     *
     ******************************************************************************************************************/
    @Nonnull
    public PropertyChangeListener[] getPropertyChangeListeners();

    /*******************************************************************************************************************
     *
     * Returns the bound {@link PropertyChangeListener}s for the given property.
     *
     * @param propertyName  the name of the property
     * @return              the listeners
     *
     ******************************************************************************************************************/
    @Nonnull
    public PropertyChangeListener[] getPropertyChangeListeners (@Nonnull String propertyName);

    /*******************************************************************************************************************
     *
     * Creates an instance given an owner and no roles.
     *
     * @param   owner   the owner
     * @return          the new instance
     * @since           3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel of (@Nonnull final Object owner)
      {
        Parameters.mustNotBeArrayOrCollection(owner, "owner");
        return of(owner, Collections.emptyList());
      }

    /*******************************************************************************************************************
     *
     * Creates an instance given an owner and a single role.
     *
     * @param   owner   the owner
     * @param   role    the role (or a {@link it.tidalwave.util.RoleFactory})
     * @return          the new instance
     * @since           3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel of (@Nonnull final Object owner, @Nonnull final Object role)
      {
        Parameters.mustNotBeArrayOrCollection(owner, "owner");
        Parameters.mustNotBeArrayOrCollection(role, "role");
        return of(owner, r(role));
      }

    /*******************************************************************************************************************
     *
     * Creates an instance given an owner and multiple roles.
     *
     * @param   owner   the owner
     * @param   roles   roles or {@link it.tidalwave.util.RoleFactory} instances
     * @return          the new instance
     * @since           3.2-ALPHA-1
     * @since           3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel of (@Nonnull final Object owner, @Nonnull final Collection<Object> roles)
      {
        Parameters.mustNotBeArrayOrCollection(owner, "owner");
        return new DefaultPresentationModel(owner, roles);
      }

    /*******************************************************************************************************************
     *
     * Returns an empty instance (no roles, with the exception of a dummy {@link Displayable}).
     *
     * @return          the empty instance
     * @since           3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel empty()
      {
        // TODO: cache a singleton, but don't do eager initialization (e.g. a final static), as it would deadlock with
        // SteelBlue.
        return of("", Displayable.of("<empty presentation model>"));
      }

    /*******************************************************************************************************************
     *
     * Creates an instance from an owner which might have the {@link Presentable} role. If it is present, it is called
     * to create the {@code PresentationModel}; otherwise a default one is created. Additional roles are added.
     *
     * @param   owner   the owner
     * @param   roles   roles or {@link it.tidalwave.util.RoleFactory} instances
     * @return          the new instance
     * @since           3.2-ALPHA-8
     * @it.tidalwave.javadoc.experimental TODO: perhaps it could be merged to of().
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel ofMaybePresentable (@Nonnull final As owner, @Nonnull final Collection<Object> roles)
      {
        Parameters.mustNotBeArrayOrCollection(owner, "owner");
        return owner.maybeAs(_Presentable_)
                    .map(p -> p.createPresentationModel(roles))
                    .orElseGet(() -> of(owner, roles));
      }

    /*******************************************************************************************************************
     *
     * Creates an instance from an owner which might have the {@link Presentable} role. If it is present, it is called
     * to create the {@code PresentationModel}; otherwise a default one is created.
     *
     * @param   owner   the owner
     * @return          the new instance
     * @since           3.2-ALPHA-8
     * @it.tidalwave.javadoc.experimental TODO: perhaps it could be merged to of().
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModel ofMaybePresentable (@Nonnull final As owner)
      {
        return ofMaybePresentable(owner, Collections.emptyList());
      }
  }
