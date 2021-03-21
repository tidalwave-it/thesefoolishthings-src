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
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import it.tidalwave.util.As;
import it.tidalwave.util.Callback;
import it.tidalwave.util.NamedCallback;
import it.tidalwave.util.spi.AsSupport;
import it.tidalwave.role.ui.PresentationModel;
import java.beans.PropertyChangeListener;
import lombok.experimental.Delegate;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A default implementation of {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ToString(exclude = {"asSupport", "pcs"}) @Slf4j
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
    public DefaultPresentationModel (@Nonnull final Object owner, @Nonnull final Collection<Object> rolesOrFactories)
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
    public <T> T as (@Nonnull final Class<? extends T> roleType)
      {
        return as(roleType, As.Defaults.throwAsException(roleType));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T as (@Nonnull final Class<? extends T> roleType,
                     @Nonnull final NotFoundBehaviour<? extends T> notFoundBehaviour)
      {
        // Undocumented feature: for instance Zephyr needs to fire property events
        if (roleType.equals(PropertyChangeSupport.class))
          {
            return roleType.cast(pcs);
          }

        return asSupport.as(roleType, new NotFoundBehaviour<T>()
          {
            @SuppressWarnings("ConstantConditions")
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
    public <T> Collection<T> asMany (@Nonnull final Class<? extends T> roleType)
      {
        final Collection<T> result = asSupport.asMany(roleType);

        // The problem here is that we want only to add local roles in owner; but calling owner.as() will also
        // find again the global roles that were discovered by AsSupport.
        if (roleType.isAssignableFrom(owner.getClass()))
          {
            result.add(roleType.cast(owner));
          }

        if (owner instanceof As)
          {
            result.addAll(((As)owner).asMany(roleType));
          }

        return result;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void dispose()
      {
        for (final PropertyChangeListener listener : pcs.getPropertyChangeListeners().clone())
          {
            pcs.removePropertyChangeListener(listener);
          }

        asMany(NamedCallback.class).stream()
                                   .filter(c -> c.getName().equals(CALLBACK_DISPOSE))
                                   .forEach(callback -> wrap(callback, "While calling 'dispose' callbacks"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private static void wrap (@Nonnull final Callback callback, @Nonnull final String logMessage)
      {
        try
          {
            callback.call();
          }
        catch (Throwable t)
          {
            log.error(logMessage, t);
          }
      }
  }
