/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings Examples - DCI Swing
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.dci.swing;

import javax.annotation.Nonnull;
import javax.swing.Action;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistry;

/***********************************************************************************************************************
 *
 * The presentation for the example.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface PersonPresentation
  {
    /*******************************************************************************************************************
     *
     * Initialize the presentation and bind it to some data and callbacks.
     *
     * @param  okAction         the callback for the Ok button
     * @param  personRegistry   the persons
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Action okAction, @Nonnull PersonRegistry personRegistry);

    /*******************************************************************************************************************
     *
     * Disposes the presentation.
     *
     ******************************************************************************************************************/
    public void dispose();
  }
