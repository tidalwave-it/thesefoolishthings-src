/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

/***********************************************************************************************************************
 *
 * This interface defines the behavior of objects that can display their names possibly according to the current
 * {@link Locale}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Displayable
  {
    public final static Class<Displayable> Displayable = Displayable.class;
    
    /***************************************************************************
     *
     * Returns the display name in the current {@link Locale}.
     *
     * @returns  the display name
     *
     **************************************************************************/
    @Nonnull
    public String getDisplayName();

    /***************************************************************************
     *
     * Returns the display name in the given {@link Locale}.
     *
     * @param    locale    the <code>Locale</code>
     * @return             the display name
     *
     **************************************************************************/
    @Nonnull
    public String getDisplayName (@Nonnull Locale locale);

    @Nonnull
    public Map<Locale, String> getDisplayNames();

    /***************************************************************************
     *
     * Returns the available {@link Locale}s.
     *
     * @return             the available <code>Locale</code>s
     *
     **************************************************************************/
    @Nonnull
    public SortedSet<Locale> getLocales();
  }
