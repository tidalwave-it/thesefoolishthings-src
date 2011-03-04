/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Map;

/***********************************************************************************************************************
 *
 * A specialized {@link Displayable} which is mutable and fires {@code PropertyChangeEvent}s.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @stable
 *
 **********************************************************************************************************************/
public interface MutableDisplayable extends Displayable
  {
    public final static String PROP_DISPLAY_NAME = "displayName";
    public final static String PROP_DISPLAY_NAMES = "displayNames";
    
    public final static Class<MutableDisplayable> MutableDisplayable = MutableDisplayable.class;
    
    /*******************************************************************************************************************
     *
     * Sets the display name in {@code Locale.ENGLISH}.
     * 
     * @param  displayName  the display name
     *
     ******************************************************************************************************************/
    public void setDisplayName (@Nonnull String displayName);

    /*******************************************************************************************************************
     *
     * Sets the display name in the given {@code Locale}.
     * 
     * @param  displayName  the display name
     * @param  locale       the locale
     *
     ******************************************************************************************************************/
    public void setDisplayName (@Nonnull String displayName, @Nonnull Locale locale);

    /*******************************************************************************************************************
     *
     * Sets a bag of display names for a number of {@code Locale}s.
     * 
     * @param  displayNames  the display names
     *
     ******************************************************************************************************************/
    public void setDisplayNames (@Nonnull Map<Locale, String> displayNames);

    /*******************************************************************************************************************
     *
     * Registers a {@link PropertyChangeListener}.
     * 
     * @param  listener   the listener
     *
     ******************************************************************************************************************/
    public void addPropertyChangeListener (@Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     * Unregisters a {@link PropertyChangeListener}.
     * 
     * @param  listener   the listener
     *
     ******************************************************************************************************************/
    public void removePropertyChangeListener (@Nonnull PropertyChangeListener listener);
  }
