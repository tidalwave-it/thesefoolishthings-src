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
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Callback;
import it.tidalwave.util.NamedCallback;
import it.tidalwave.role.ui.PresentationModel;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A default implementation of {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ToString(exclude = {"as", "pcs"}) @Slf4j
public class DefaultPresentationModel implements PresentationModel
  {
    @Nonnull
    private final Object owner;

    @Delegate
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final As as;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultPresentationModel (@Nonnull final Object owner, @Nonnull final Collection<Object> roles)
      {
        this.owner = owner;
        as = As.forObject(owner, roles);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T as (@Nonnull final Class<? extends T> roleType)
      {
        return maybeAs(roleType).orElseThrow(() -> new AsException(roleType));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> Optional<T> maybeAs (@Nonnull final Class<? extends T> roleType)
      {
        // Undocumented feature: for instance Zephyr needs to fire property events
        if (roleType.equals(PropertyChangeSupport.class))
          {
            return Optional.of(roleType.cast(pcs));
          }

        final Optional<T> t = as.maybeAs(roleType);

        if (t.isPresent())
          {
            return t;
          }

        if (owner instanceof As)
          {
            try
              {
                final T role = ((As)owner).as(roleType);

                if (role != null) // do check it for improper implementations or partial mocks
                  {
                    return Optional.of(role);
                  }
              }
            catch (AsException e)
              {
                // fallback
              }
          }

        return Optional.empty();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> Collection<T> asMany (@Nonnull final Class<? extends T> roleType)
      {
        final Collection<T> result = as.asMany(roleType);

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
