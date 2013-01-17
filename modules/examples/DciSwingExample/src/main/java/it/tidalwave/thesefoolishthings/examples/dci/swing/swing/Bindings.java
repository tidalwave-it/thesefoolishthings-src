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

import javax.annotation.Nonnull;
import javax.swing.JList;
import javax.swing.JTable;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.beansbinding.BindingGroup;
import it.tidalwave.role.AsExtensions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.*;
import static it.tidalwave.thesefoolishthings.examples.dci.swing.role.ObservableListProvider.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bindings 
  {
    public static void bind (final @Nonnull BindingGroup bindings,
                             final @Nonnull Object source, 
                             final @Nonnull JList jList) 
      {
        final ObservableList<?> ol = source.as(ObservableListProvider).createObservableList();
        bindings.addBinding(SwingBindings.createJListBinding(READ, ol, jList));
        jList.setCellRenderer(new HtmlRenderableListCellRenderer());
      }
    
    public static void bind (final @Nonnull BindingGroup bindings,
                             final @Nonnull Object source, 
                             final @Nonnull JTable jTable) 
      {
        final ObservableList<?> ol = source.as(ObservableListProvider).createObservableList();
        bindings.addBinding(SwingBindings.createJTableBinding(READ_WRITE, ol, jTable));
      }
  }
