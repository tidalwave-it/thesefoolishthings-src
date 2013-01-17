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
package it.tidalwave.thesefoolishthings.examples.dci.swing.swing;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.util.AsException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.HtmlRenderable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
@ExtensionMethod(AsExtensions.class)
public class HtmlRenderableListCellRenderer extends DefaultListCellRenderer
  {
    @Override @Nonnull
    public Component getListCellRendererComponent (final @Nonnull JList list, 
                                                   final @Nonnull Object value,
                                                   final @Nonnull int index, 
                                                   final @Nonnull boolean isSelected,
                                                   final @Nonnull boolean cellHasFocus)
      {
        return super.getListCellRendererComponent(list, getRenderableString(value), index, isSelected, cellHasFocus); 
      }
    
    @Nonnull
    private static String getRenderableString (final @Nonnull Object value)
      {
        try
          {
            final StringBuilder builder = new StringBuilder("<html>");
            value.as(HtmlRenderable).renderTo(builder);
            return builder.append("</html>").toString();
          }
        catch (AsException e)
          {
            return value.toString();
          }        
      }
  }
