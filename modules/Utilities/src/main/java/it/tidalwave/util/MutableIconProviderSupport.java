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
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft
 *
 **********************************************************************************************************************/
public abstract class MutableIconProviderSupport implements MutableIconProvider
  {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

//    @Override
    public void addPropertyChangeListener (final @Nonnull PropertyChangeListener listener)
      {
        pcs.addPropertyChangeListener(listener);
      }

//    @Override
    public void removePropertyChangeListener (final @Nonnull PropertyChangeListener listener)
      {
        pcs.removePropertyChangeListener(listener);
      }

    protected void fireIconChange (final @Nonnull Icon oldIcon, final @Nonnull Icon newIcon)
      {
        pcs.firePropertyChange(PROP_ICON, oldIcon, newIcon); // FIXME: should be in the EDT?
      }
  }
