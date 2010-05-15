/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.java.awt;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public abstract class GraphicsEnvironment
  {
    public static synchronized GraphicsEnvironment getLocalGraphicsEnvironment()
      {
        throw new RuntimeException("Stub!");
      }

    public abstract GraphicsDevice getDefaultScreenDevice()
      throws HeadlessException;

    public static boolean isHeadless()
      {
        throw new RuntimeException("Stub!");
      }
  }
