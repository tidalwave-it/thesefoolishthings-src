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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import javax.swing.Icon;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
// FIXME: move to Android replacement of javax.swing.AbstractAction
public abstract class AbstractAction implements Action, Cloneable, Serializable 
  {
    private static final String PROP_ENABLED = "enabled";
    
    private final Map<String, Object> valueMap = new HashMap<String, Object>();

    protected boolean enabled = true;

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public AbstractAction() 
      {
      }
    
    public AbstractAction (String name)
      {
	putValue(Action.NAME, name);
      }

    public AbstractAction (String name, Icon icon) 
      {
	this(name);
	putValue(Action.SMALL_ICON, icon);
      }
    
    public Object getValue (String key)
      {
        if (PROP_ENABLED.equals(key))
          {
            return enabled;
          }
        
	return valueMap.get(key);
      }
    
    public void putValue (String key, Object newValue)
      {
	Object oldValue = null;
        
        if (PROP_ENABLED.equals(key)) 
          {
            if (newValue == null || !(newValue instanceof Boolean))
              {
                newValue = false;
              }
            
            oldValue = enabled;
            enabled = (Boolean)newValue;
          }
        else 
          {
            if (valueMap.containsKey(key))
              {
                oldValue = valueMap.get(key);
              }
            
            if (newValue == null) 
              {
                valueMap.remove(key);
              }
            else 
              {
                valueMap.put(key, newValue);
              }
          }
        
	firePropertyChange(key, oldValue, newValue);
      }

    public boolean isEnabled() 
      {
	return enabled;
      }

    public void setEnabled (boolean newValue) 
      {
	boolean oldValue = this.enabled;

	if (oldValue != newValue) 
          {
	    this.enabled = newValue;
	    firePropertyChange(PROP_ENABLED, oldValue, newValue);
	  }
      }

    public Object[] getKeys() 
      {
        return valueMap.keySet().toArray();
      }

    protected void firePropertyChange (String propertyName, Object oldValue, Object newValue) 
      {
        if ((oldValue != null) && (newValue != null) && oldValue.equals(newValue))
          {
            return;
          }

        pcs.firePropertyChange(propertyName, oldValue, newValue);
      }

    public synchronized void addPropertyChangeListener (PropertyChangeListener listener) 
      {
        pcs.addPropertyChangeListener(listener);
      }

    public synchronized void removePropertyChangeListener (PropertyChangeListener listener) 
      {
        pcs.removePropertyChangeListener(listener);
      }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() 
      {
        return pcs.getPropertyChangeListeners();
      }
  }
