/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - Beans
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
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
 * *********************************************************************************************************************
 * 
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/*******************************************************************************
 *
 * Code adapted from AbstractBean by SwingLabs.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 ******************************************************************************/
public abstract class JavaBeanSupport implements JavaBean
  {
    protected transient PropertyChangeSupport propertyChangeSupport;
    protected transient VetoableChangeSupport vetoableChangeSupport;

    public final void addPropertyChangeListener (final PropertyChangeListener listener)
      {
        propertyChangeSupport.addPropertyChangeListener(listener);
      }

    public final void removePropertyChangeListener (final PropertyChangeListener listener)
      {
        propertyChangeSupport.removePropertyChangeListener(listener);
      }

    public final PropertyChangeListener[] getPropertyChangeListeners()
      {
        return propertyChangeSupport.getPropertyChangeListeners();
      }

    public final void addPropertyChangeListener (final String propertyName, final PropertyChangeListener listener)
      {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
      }

    public final void removePropertyChangeListener (final String propertyName, final PropertyChangeListener listener)
      {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
      }

    public final PropertyChangeListener[] getPropertyChangeListeners (final String propertyName)
      {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
      }

    public final void addVetoableChangeListener (final VetoableChangeListener listener)
      {
        vetoableChangeSupport.addVetoableChangeListener(listener);
      }

    public final void removeVetoableChangeListener (final VetoableChangeListener listener)
      {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
      }

    public final VetoableChangeListener[] getVetoableChangeListeners()
      {
        return vetoableChangeSupport.getVetoableChangeListeners();
      }

    public final void addVetoableChangeListener (final String propertyName, final VetoableChangeListener listener)
      {
        vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
      }

    public final void removeVetoableChangeListener (final String propertyName, final VetoableChangeListener listener)
      {
        vetoableChangeSupport.removeVetoableChangeListener(propertyName, listener);
      }

    public final VetoableChangeListener[] getVetoableChangeListeners (final String propertyName)
      {
        return vetoableChangeSupport.getVetoableChangeListeners(propertyName);
      }

    public Object __getDelegate()
      {
        return this;
      }
  }
