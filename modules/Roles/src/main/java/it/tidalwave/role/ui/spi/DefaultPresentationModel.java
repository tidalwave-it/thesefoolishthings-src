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
import java.beans.PropertyChangeSupport;
import it.tidalwave.util.As;
import it.tidalwave.util.spi.AsSupport;
import it.tidalwave.role.ui.PresentationModel;
import java.beans.PropertyChangeListener;
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
@ToString(exclude = {"asSupport", "pcs"})
public class DefaultPresentationModel implements PresentationModel
  {
    @Nonnull
    private final Object owner;

    @Delegate
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final AsSupport asSupport;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultPresentationModel (final @Nonnull Object owner,
                                     final @Nonnull Object ... rolesOrFactories)
      {
        this.owner = owner;
        asSupport = new AsSupport(owner, rolesOrFactories);
      }

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
        return asSupport.as(roleType, new NotFoundBehaviour<T>()
          {
            @Nonnull
            public T run (final Throwable t)
              {
                if (owner instanceof As)
                  {
                    final T role = ((As)owner).as(roleType);

                    if (role != null) // do check it for improper implementations or partial mocks
                      {
                        return role;
                      }
                  }

                return notFoundBehaviour.run(t);
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> Collection<T> asMany (final @Nonnull Class<T> roleType)
      {
        final Collection<T> result = asSupport.asMany(roleType);

        // FIXME: THESEFOOLISHTHINGS-127
        // The problem here is that we want only to add local roles in owner; but calling owner.as() will also
        // find again the global roles that were discovered by AsSupport.
        if (roleType.isAssignableFrom(owner.getClass()))
          {
            result.add(roleType.cast(owner));
          }
//        if (owner instanceof As)
//          {
//            final T role = ((As)owner).as(roleType);
//
//            if ((role != null) && !contains(result, role)) // do check it for improper implementations or partial mocks
//              {
//                result.add(roleType.cast(role));
//              }
//          }

        return result;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void dispose()
      {
        for (final PropertyChangeListener listener : pcs.getPropertyChangeListeners().clone())
          {
            pcs.removePropertyChangeListener(listener);
          }
      }
  }
