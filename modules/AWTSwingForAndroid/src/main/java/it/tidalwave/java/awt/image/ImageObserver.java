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
