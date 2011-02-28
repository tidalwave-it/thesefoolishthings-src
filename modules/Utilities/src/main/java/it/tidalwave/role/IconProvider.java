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
package it.tidalwave.role;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft
 * 
 **********************************************************************************************************************/
public interface IconProvider
  {
    //@bluebook-begin other
    public static final Class<IconProvider> IconProvider = IconProvider.class;
    
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
    @Nonnull
    public Icon getIcon (@Nonnegative int size);
  }
