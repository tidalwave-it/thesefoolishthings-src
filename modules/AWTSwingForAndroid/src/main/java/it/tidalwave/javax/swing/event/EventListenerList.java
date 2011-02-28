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
/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.javax.swing.event;

import java.util.EventListener;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public class EventListenerList
  {
    private final static Object[] NO_OBJECTS = new Object[0];
    private final static EventListener[] NO_LISTENERS = new EventListener[0];

    public Object[] getListenerList()
      {
        return NO_OBJECTS;
      }

    public EventListener[] getListeners (Class clazz)
      {
        return NO_LISTENERS;
      }

    public int getListenerCount()
      {
        return 0;
      }

    public int getListenerCount (Class clazz)
      {
        return 0;
      }

    public synchronized void add (Class clazz, EventListener eventListener)
      {
      }

    public synchronized void remove (Class clazz, EventListener eventListener)
      {
      }
  }
