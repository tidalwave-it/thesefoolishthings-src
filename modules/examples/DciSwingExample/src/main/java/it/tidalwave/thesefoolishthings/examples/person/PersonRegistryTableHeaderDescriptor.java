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
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.thesefoolishthings.examples.dci.swing.role.TableColumnDescriptor;
import it.tidalwave.thesefoolishthings.examples.dci.swing.role.TableHeaderDescriptor;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = PersonRegistry.class)
@RequiredArgsConstructor
public class PersonRegistryTableHeaderDescriptor implements TableHeaderDescriptor
  {
    @Nonnull
    private final PersonRegistry datum;

    @Override @Nonnull
    public List<TableColumnDescriptor> getColumnDescriptors()
      {
        final List<TableColumnDescriptor> columnDescriptors = new ArrayList<TableColumnDescriptor>();
        columnDescriptors.add(new TableColumnDescriptor("firstName", "First Name"));
        columnDescriptors.add(new TableColumnDescriptor("lastName", "Last Name"));

        return columnDescriptors;
      }
  }
