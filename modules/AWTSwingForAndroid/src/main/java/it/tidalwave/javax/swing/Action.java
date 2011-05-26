/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.javax.swing;

import it.tidalwave.java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Action extends ActionListener
  {
    public static final String DEFAULT = "Default";

    public static final String NAME = "Name";

    public static final String SHORT_DESCRIPTION = "ShortDescription";

    public static final String LONG_DESCRIPTION = "LongDescription";

    public static final String SMALL_ICON = "SmallIcon";

    public static final String ACTION_COMMAND_KEY = "ActionCommandKey";

    public static final String ACCELERATOR_KEY="AcceleratorKey";
    
    public static final String MNEMONIC_KEY="MnemonicKey";

    public static final String SELECTED_KEY = "SwingSelectedKey";

    public static final String DISPLAYED_MNEMONIC_INDEX_KEY = "SwingDisplayedMnemonicIndexKey";

    public static final String LARGE_ICON_KEY = "SwingLargeIconKey";

    public Object getValue (String key);

    public void putValue (String key, Object value);

    public void setEnabled (boolean b);

    public boolean isEnabled();

    public void addPropertyChangeListener (PropertyChangeListener listener);
    
    public void removePropertyChangeListener (PropertyChangeListener listener);
  }
