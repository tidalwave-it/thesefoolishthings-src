/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.thesefoolishthings.examples.dci.swing.swing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import it.tidalwave.util.AsException;
import it.tidalwave.util.AsExtensions;
import it.tidalwave.role.HtmlRenderable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.HtmlRenderable._HtmlRenderable_;

/***********************************************************************************************************************
 *
 * A specialization of {@link javax.swing.DefaultListCellRenderer} that uses {@link HtmlRenderable} for rendering items.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
@ExtensionMethod(AsExtensions.class)
public class HtmlRenderableListCellRenderer extends DefaultListCellRenderer
  {
    @Override @Nonnull
    public Component getListCellRendererComponent (@Nonnull final JList list,
                                                   @Nonnull final Object value,
                                                   @Nonnegative final int index,
                                                   final boolean isSelected,
                                                   final boolean cellHasFocus)
      {
        return super.getListCellRendererComponent(list, getRenderableString(value), index, isSelected, cellHasFocus);
      }

    @Nonnull
    private static String getRenderableString (@Nonnull final Object value)
      {
        try
          {
            final StringBuilder builder = new StringBuilder("<html>");
            value.as(_HtmlRenderable_).renderTo(builder);
            return builder.append("</html>").toString();
          }
        catch (AsException e)
          {
            return value.toString();
          }
      }
  }
