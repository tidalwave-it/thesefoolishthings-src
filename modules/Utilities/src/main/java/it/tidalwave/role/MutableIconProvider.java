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
import javax.swing.Icon;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft
 *
 **********************************************************************************************************************/
public interface MutableIconProvider extends IconProvider
  {
    public static final Class<MutableIconProvider> MutableIconProvider = MutableIconProvider.class;
    
    public final static String PROP_ICON = "icon";
    
    @Nonnull
    public void setIcon (@Nonnull Icon icon);
    
    public void addPropertyChangeListener (@Nonnull PropertyChangeListener listener);

    public void removePropertyChangeListener (@Nonnull PropertyChangeListener listener);
  }
