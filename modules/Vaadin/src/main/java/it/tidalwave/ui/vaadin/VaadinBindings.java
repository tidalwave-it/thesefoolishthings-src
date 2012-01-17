/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.ui.vaadin;

import javax.annotation.Nonnull;
import com.vaadin.ui.Table;
import it.tidalwave.util.NotFoundException;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access=PRIVATE)
public final class VaadinBindings 
  {
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static void bind (final @Nonnull Table table, final @Nonnull TableModel tableModel)
      {
        table.setContainerDataSource(tableModel);
        
        try 
          {
            table.setColumnHeaders(tableModel.getHeaderLabels().toArray(new String[0]));
          } 
        catch (NotFoundException e) 
          {
             table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
          }
      }
  }