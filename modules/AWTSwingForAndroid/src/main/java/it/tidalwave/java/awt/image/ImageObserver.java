/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.java.awt.image;

import it.tidalwave.java.awt.Image;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public interface ImageObserver
  {
    public static final int WIDTH = 0; // FIXME: values are not the original ones
    public static final int HEIGHT = 1;
    public static final int PROPERTIES = 2;
    public static final int SOMEBITS = 3;
    public static final int FRAMEBITS = 4;
    public static final int ALLBITS = 5;
    public static final int ERROR = 6;
    public static final int ABORT = 7;

    public boolean imageUpdate (Image image, int a, int b, int c, int d, int e);
  }
