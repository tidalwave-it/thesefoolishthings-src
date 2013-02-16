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
package it.tidalwave.role;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/***********************************************************************************************************************
 *
 * The role of an object that can provide an icon for rendering.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public interface IconProvider
  {
    //@bluebook-begin other
    public static final Class<IconProvider> IconProvider = IconProvider.class;

    /*******************************************************************************************************************
     *
     * A default {@code IconProvider} with a empty icon.
     *
     ******************************************************************************************************************/
    public final static IconProvider DEFAULT = new IconProvider()
      {
        private final Icon EMPTY_ICON = new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR));

        @Nonnull
        public Icon getIcon (final @Nonnegative int size)
          {
            return EMPTY_ICON;
          }
      };

    //@bluebook-end other
    /*******************************************************************************************************************
     *
     * Returns the icon for this object. Note that the {@code size} parameter is just a hint to allow implementations
     * to pick the correctly sized icon in an optimized fashion. In particular, implementations should try to do their
     * best for providing an icon whose size is equal or greater than the requested one, but this is not guaranteed.
     * It's up to the client code to eventually resize the returned icon for its purposes.
     *
     * @param  requestedSize  the requested icon size
     * @return                the icon
     *
     ******************************************************************************************************************/
    @Nonnull
    public Icon getIcon (@Nonnegative int requestedSize);
  }
