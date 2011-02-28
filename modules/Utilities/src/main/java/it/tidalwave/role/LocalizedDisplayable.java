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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.role;

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
 * @stable
 *
 **********************************************************************************************************************/
public interface LocalizedDisplayable extends Displayable
  {
    //@bluebook-begin other
    public final static Class<LocalizedDisplayable> LocalizedDisplayable = LocalizedDisplayable.class;
    
    //@bluebook-end other
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
