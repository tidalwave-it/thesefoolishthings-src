/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.thesefoolishthings.examples.person.DefaultPersonRegistry;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistry;
import it.tidalwave.thesefoolishthings.examples.person.Utils;

/***********************************************************************************************************************
 *
 * The default control for the presentation.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultPersonPresentationControl implements PersonPresentationControl
  {
    @Nonnull
    private final PersonPresentation presentation;

    private final Action okAction = new AbstractAction("Ok")
      {
        public void actionPerformed (final @Nonnull ActionEvent event)
          {
            presentation.dispose();
          }
      };

    public DefaultPersonPresentationControl (final @Nonnull PersonPresentation presentation)
      {
        this.presentation = presentation;
        final PersonRegistry personRegistry = new DefaultPersonRegistry();
        Utils.populatePresidents(personRegistry);

        presentation.bind(okAction, personRegistry);
      }
  }
