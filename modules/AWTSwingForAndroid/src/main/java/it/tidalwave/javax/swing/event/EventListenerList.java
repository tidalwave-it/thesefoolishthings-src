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
