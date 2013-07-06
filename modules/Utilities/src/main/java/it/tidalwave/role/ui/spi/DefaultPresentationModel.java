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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.ui.PresentationModel;
import lombok.Delegate;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A default implementation of {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ToString
public class DefaultPresentationModel implements PresentationModel
  {
    @Nonnull
    private final Object datum;

    @Nonnull
    private final List<Object> roles;

    @Delegate
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultPresentationModel (final @Nonnull Object datum,
                                     final @Nonnull Object ... rolesOrFactories)
      {
        this.datum = datum;
        this.roles = resolveRoles(Arrays.asList(rolesOrFactories));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> T as (final @Nonnull Class<T> type)
      {
        return as(type, As.Defaults.throwAsException(type));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> T as (final @Nonnull Class<T> type, NotFoundBehaviour<T> notFoundBehaviour)
      {
        for (final Object role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                return type.cast(role);
              }
          }

        if (datum instanceof As)
          {
            final T as = ((As)datum).as(type);

            if (as != null) // do check it for improper implementations or partial mocks
              {
                return as;
              }
          }

        return notFoundBehaviour.run(new AsException(type));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveRoles (final @Nonnull List<Object> rolesOrFactories)
      {
        final List<Object> roles = new ArrayList<Object>();

        for (final Object roleOrFactory : rolesOrFactories)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                roles.add(((RoleFactory<Object>)roleOrFactory).createRoleFor(datum));
              }
            else
              {
                roles.add(roleOrFactory);
              }
          }

        return roles;
      }
  }
