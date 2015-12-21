/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
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
 * *********************************************************************************************************************
 * 
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.dci.swing.swing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import it.tidalwave.util.AsException;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.role.HtmlRenderable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.HtmlRenderable.*;

/***********************************************************************************************************************
 *
 * A specialization of {@link javax.swing.DefaultListCellRenderer} that uses {@link HtmlRenderable} for rendering items.
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
                                                   final @Nonnegative int index,
                                                   final boolean isSelected,
                                                   final boolean cellHasFocus)
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
