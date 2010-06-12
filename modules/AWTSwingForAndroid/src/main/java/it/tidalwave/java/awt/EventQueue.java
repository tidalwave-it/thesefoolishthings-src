/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.java.awt;

import java.lang.reflect.InvocationTargetException;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public class EventQueue
  {
    public static boolean isDispatchThread()
      {
        return false;
      }

    public static void invokeLater (Runnable runnable)
      {
      }
 
    public static void invokeAndWait (Runnable runnable)
      throws InterruptedException, InvocationTargetException
      {
      }
  }
