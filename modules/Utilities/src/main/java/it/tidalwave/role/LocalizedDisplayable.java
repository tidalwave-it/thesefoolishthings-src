/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

/***********************************************************************************************************************
 *
 * A specialized {@link Displayable} which can manage names in multiple {@link Locale}s.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface LocalizedDisplayable extends Displayable
  {
    //@bluebook-begin other
    public final static Class<LocalizedDisplayable> LocalizedDisplayable = LocalizedDisplayable.class;
    
    //@bluebook-end other
    /*******************************************************************************************************************
     *
     * Returns the display name in the given {@link Locale}.
     *
     * @param    locale    the {@code Locale}
     * @return             the display name
     *
     ******************************************************************************************************************/
    @Nonnull
    public String getDisplayName (@Nonnull Locale locale);

    /*******************************************************************************************************************
     *
     * Returns all the display names in {@link Map} where they are indexed by {@code Locale}.
     * 
     * @return   the display names
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Locale, String> getDisplayNames();

    /*******************************************************************************************************************
     *
     * Returns the supported {@code Locale}s.
     *
     * @return             the available {@code Locale}s
     *
     ******************************************************************************************************************/
    @Nonnull
    public SortedSet<Locale> getLocales();
  }
