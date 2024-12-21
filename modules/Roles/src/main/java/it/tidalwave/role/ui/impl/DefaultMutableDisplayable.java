/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import it.tidalwave.role.ui.MutableLocalizedDisplayable;

/***************************************************************************************************************************************************************
 *
 * A default implementation of {@link MutableLocalizedDisplayable} starting which a single display name in
 * {@code Locale.ENGLISH} language.
 *
 * This is no more a public class; use
 *
 * @author Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **************************************************************************************************************************************************************/
public class DefaultMutableDisplayable implements MutableLocalizedDisplayable
  {
    // private static final long serialVersionUID = 45345436345634734L;

    @Nonnull
    private final String toStringName;

    @Nonnull
    private  final Map<Locale, String> displayNameMap = new HashMap<>();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final Locale defaultLocale = Locale.ENGLISH;

    /***********************************************************************************************************************************************************
     * Creates an instance with an initial given display name in {@code Locale.ENGLISH}.
     *
     * @param  displayName   the display name
     **********************************************************************************************************************************************************/
    public DefaultMutableDisplayable (@Nonnull final String displayName)
      {
        this(displayName, "???");
      }

    /***********************************************************************************************************************************************************
     * Creates an instance with an initial given display name in {@code Locale.ENGLISH} and an explicit identifier for
     * {@code toString()}.
     *
     * @param  displayName   the display name
     * @param  toStringName  the name to be rendered when {@code toString()} is called
     **********************************************************************************************************************************************************/
    public DefaultMutableDisplayable (@Nonnull final String displayName, @Nonnull final String toStringName)
      {
        this.toStringName = toStringName;
        displayNameMap.put(defaultLocale, displayName);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String getDisplayName()
      {
        return getDisplayName(defaultLocale);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String getDisplayName (@Nonnull final Locale locale)
      {
        return displayNameMap.get(locale);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public SortedSet<Locale> getLocales()
      {
        return new TreeSet<>(displayNameMap.keySet());
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Map<Locale, String> getDisplayNames()
      {
        return Collections.unmodifiableMap(displayNameMap);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void setDisplayName (@Nonnull final String displayName)
      {
        final var oldDisplayName = getDisplayName(defaultLocale);
        setDisplayName(displayName, defaultLocale);
        pcs.firePropertyChange(PROP_DISPLAY_NAME, oldDisplayName, displayName);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void setDisplayName (@Nonnull final String displayName, @Nonnull final Locale locale)
      {
        final Map<Locale, String> oldDisplayNameMap = new HashMap<>(displayNameMap);
        displayNameMap.put(locale, displayName);
        pcs.firePropertyChange(PROP_DISPLAY_NAMES, oldDisplayNameMap, displayNameMap);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void setDisplayNames (@Nonnull final Map<Locale, String> displayNames)
      {
        final Map<Locale, String> oldDisplayNameMap = new HashMap<>(displayNameMap);
        displayNameMap.putAll(displayNames);
        pcs.firePropertyChange(PROP_DISPLAY_NAMES, oldDisplayNameMap, displayNameMap);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void addPropertyChangeListener (@Nonnull final PropertyChangeListener listener)
      {
        pcs.addPropertyChangeListener(listener);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public void removePropertyChangeListener (@Nonnull final PropertyChangeListener listener)
      {
        pcs.removePropertyChangeListener(listener);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("%s@%x$MutableDisplayable[]", toStringName, System.identityHashCode(this));
      }
  }
