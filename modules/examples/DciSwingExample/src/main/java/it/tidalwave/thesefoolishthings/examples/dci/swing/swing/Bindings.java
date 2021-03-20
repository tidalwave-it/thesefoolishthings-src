/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.dci.swing.swing;

import javax.annotation.Nonnull;
import javax.swing.JList;
import javax.swing.JTable;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.thesefoolishthings.examples.dci.swing.role.ObservableListProvider;
import it.tidalwave.thesefoolishthings.examples.dci.swing.role.TableColumnDescriptor;
import it.tidalwave.thesefoolishthings.examples.dci.swing.role.TableHeaderDescriptor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.*;
import static it.tidalwave.thesefoolishthings.examples.dci.swing.role.ObservableListProvider.*;
import static it.tidalwave.thesefoolishthings.examples.dci.swing.role.TableHeaderDescriptor.*;

/***********************************************************************************************************************
 *
 * A facility to bind some Swing components to data.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bindings
  {
    /*******************************************************************************************************************
     *
     * Binds a source to a {@link JList}. Two roles are used:
     *
     * <ol>
     * <li>{@link ObservableListProvider} to retrieve the list model</li>
     * <li>{@link HtmlRenderableListCellRenderer} to retrieve rendering for each item</li>
     * </ol>
     *
     * @param  bindings  the {@link BindingGroup} to add the new binding to
     * @param  datum     the datum
     * @param  jList     the {@code JList}
     *
     ******************************************************************************************************************/
    public static void bind (@Nonnull final BindingGroup bindings,
                             @Nonnull final Object datum,
                             @Nonnull final JList jList)
      {
        final ObservableList<?> ol = datum.as(_ObservableListProvider_).createObservableList();
        bindings.addBinding(SwingBindings.createJListBinding(READ, ol, jList));
        jList.setCellRenderer(new HtmlRenderableListCellRenderer());
      }

    /*******************************************************************************************************************
     *
     * Binds a source to a {@link JTable}. Two roles are used:
     *
     * <ol>
     * <li>{@link ObservableListProvider} to retrieve the list model</li>
     * <li>{@link TableHeaderDescriptor} to retrieve the column definition</li>
     * </ol>
     *
     * @param  bindings  the {@link BindingGroup} to add the new binding to
     * @param  datum     the datum
     * @param  jTable    the {@code JTable}
     *
     ******************************************************************************************************************/
    public static void bind (@Nonnull final BindingGroup bindings,
                             @Nonnull final Object datum,
                             @Nonnull final JTable jTable)
      {
        final ObservableList<?> ol = datum.as(_ObservableListProvider_).createObservableList();
        final JTableBinding tb = SwingBindings.createJTableBinding(READ_WRITE, ol, jTable);

        for (final TableColumnDescriptor columnDescriptor : datum.as(_TableHeaderDescriptor_).getColumnDescriptors())
          {
            final BeanProperty property = BeanProperty.create(columnDescriptor.getPropertyName());
            tb.addColumnBinding(property).setColumnName(columnDescriptor.getHeaderName());
          }

        bindings.addBinding(tb);
      }
  }
