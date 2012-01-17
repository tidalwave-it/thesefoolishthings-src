/***********************************************************************************************************************
 *
 * blueBill Stats
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://bluebill.tidalwave.it/mobile/
 * SCM: http://java.net/hg/bluebill-server~stats-src
 *
 **********************************************************************************************************************/
package it.tidalwave.ui.vaadin;

import javax.annotation.Nonnull;
import java.util.List;
import it.tidalwave.util.NotFoundException;
import com.vaadin.data.Container;

/***********************************************************************************************************************
 *
 * This class extends {@link Container} to provide all the required data to populate a {@link Table}, such as the 
 * header labels and so on.
 * 
 * @stereotype  Model
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface TableModel extends Container
  {
    /*******************************************************************************************************************
     *
     * Returns the labels for the table headers. For a convenient implementation, delegate to 
     * {@link HeaderLabelProvider}.
     * 
     * @return                     the labels
     * @throws  NotFoundException  if the {@code Table} must not have header labels 
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<String> getHeaderLabels()
      throws NotFoundException; 
  }
