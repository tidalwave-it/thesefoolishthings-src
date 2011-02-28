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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.Serializable;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 * @stable
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

    public DefaultDisplayable (final @Nonnull String displayName)
      {
        this(displayName, "???");
      }
    
    public DefaultDisplayable (final @Nonnull String displayName,
                               final @Nonnull String toStringName)
      {
        this.displayName = displayName;
        this.toStringName = toStringName;
        displayNameMap.put(Locale.ENGLISH, displayName);
      }

//    @Override
    @Nonnull
    public String getDisplayName()
      {
        return displayName;
      }

//    @Override
    @Nonnull
    public String getDisplayName (final @Nonnull Locale locale)
      {
        return displayNameMap.get(locale);
      }

//    @Override
    @Nonnull
    public SortedSet<Locale> getLocales()
      {
        return new TreeSet<Locale>(displayNameMap.keySet());
      }

//    @Override
    @Nonnull
    public Map<Locale, String> getDisplayNames()
      {
        return Collections.unmodifiableMap(displayNameMap);
      }

//    @Override
    @Nonnull
    public String toString()
      {
        return String.format("%s@%x$Displayable[]", toStringName, System.identityHashCode(this));
      }
  }
