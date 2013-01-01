/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.beans;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.Serializable;

/*******************************************************************************
 *
 * Thanks to pupmonster@dev.java.net, jarppe2@dev.java.net for contributing the
 * base code.
 * 
 * @author  pupmonster@dev.java.net
 * @author  jarppe2@dev.java.net
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 ******************************************************************************/
public interface JavaBean extends Serializable
  {
    public void addPropertyChangeListener (String propertyName, PropertyChangeListener listener);

    public void addPropertyChangeListener (PropertyChangeListener listener);

    public void removePropertyChangeListener (String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener (PropertyChangeListener listener);

    public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener);

    public void addVetoableChangeListener(VetoableChangeListener listener);

    public PropertyChangeListener[] getPropertyChangeListeners();

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    public VetoableChangeListener[] getVetoableChangeListeners();

    public VetoableChangeListener[] getVetoableChangeListeners(String propertyName);

    public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener);

    public void removeVetoableChangeListener(VetoableChangeListener listener);
    
    /** Used for equals() implementation, don't use. FIXME: try to remove from this interface. */
    public Object __getDelegate();     
  }