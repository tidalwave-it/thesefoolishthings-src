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
public abstract class Toolkit
  {
    public Dimension getBestCursorSize (int w, int h)
      throws HeadlessException
      {
        throw new RuntimeException("Stub!");
      }

    public Cursor createCustomCursor (Image image, Point point, String string)
      throws IndexOutOfBoundsException, HeadlessException
      {
        throw new RuntimeException("Stub!");
      }

    public static Toolkit getToolkit() // FIXME: wrong?
      {
        throw new RuntimeException("Stub!");
      }

    public static synchronized Toolkit getDefaultToolkit()
      {
        throw new RuntimeException("Stub!");
      }

    public int getMenuShortcutKeyMask()
      throws HeadlessException
      {
        throw new RuntimeException("Stub!");
      }

    public abstract Dimension getScreenSize()
      throws HeadlessException;

    public Insets getScreenInsets (GraphicsConfiguration graphicsConfiguration)
      throws HeadlessException
      {
        throw new RuntimeException("Stub!");
      }
  }
