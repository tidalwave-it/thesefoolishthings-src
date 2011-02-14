/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Map;

/***********************************************************************************************************************
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
     *
     ******************************************************************************************************************/
    public void setDisplayName (@Nonnull String displayName);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void setDisplayName (@Nonnull String displayName,
                                @Nonnull Locale locale);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void setDisplayNames (@Nonnull Map<Locale, String> displayNames);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void addPropertyChangeListener (@Nonnull PropertyChangeListener listener);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public void removePropertyChangeListener (@Nonnull PropertyChangeListener listener);
  }
