/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.Serializable;
import it.tidalwave.role.LocalizedDisplayable;

/***********************************************************************************************************************
 *
 * A default implementation of {@link LocalizedDisplayable} which a single, immutable display name in 
 * {@code Locale.ENGLISH} language.
 * 
 * @author Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public class DefaultDisplayable implements LocalizedDisplayable, Serializable 
  {
    private static final long serialVersionUID = 45345436345634734L;

    @Nonnull
    private final String displayName;

    @Nonnull
    private final String toStringName;

    @Nonnull
    private final Map<Locale, String> displayNameMap = new HashMap<Locale, String>();

    private final Locale defaultLocale = Locale.ENGLISH;

    /*******************************************************************************************************************
     *
     * Creates an instance with a given display name.
     * 
     * @param  displayName   the display name
     *
     ******************************************************************************************************************/
    public DefaultDisplayable (final @Nonnull String displayName)
      {
        this(displayName, "???");
      }
    
    /*******************************************************************************************************************
     *
     * Creates an instance with a given display name in {@code Locale.ENGLISH} and an explicit identifier for 
     * {@code toString()}.
     * 
     * @param  displayName   the display name
     * @param  toStringName  the name to be rendered when {@code toString()} is called
     *
     ******************************************************************************************************************/
    public DefaultDisplayable (final @Nonnull String displayName,
                               final @Nonnull String toStringName)
      {
        this.displayName = displayName;
        this.toStringName = toStringName;
        displayNameMap.put(defaultLocale, displayName);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public String getDisplayName()
      {
        return displayName;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public String getDisplayName (final @Nonnull Locale locale)
      {
        return displayNameMap.get(locale);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public SortedSet<Locale> getLocales()
      {
        return new TreeSet<Locale>(displayNameMap.keySet());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public Map<Locale, String> getDisplayNames()
      {
        return Collections.unmodifiableMap(displayNameMap);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public String toString()
      {
        return String.format("%s@%x$Displayable[]", toStringName, System.identityHashCode(this));
      }
  }
