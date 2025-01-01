/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.thesefoolishthings.examples.dci.swing;

import javax.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistry;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistryHelper;
import lombok.RequiredArgsConstructor;

/***************************************************************************************************************************************************************
 *
 * The default control for the presentation.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@RequiredArgsConstructor
public class DefaultPersonPresentationControl implements PersonPresentationControl
  {
    @Nonnull
    private final PersonPresentation presentation;

    @Nonnull
    private final PersonRegistry personRegistry;

    private final Action okAction = new AbstractAction("Ok")
      {
        public void actionPerformed (@Nonnull final ActionEvent event)
          {
            presentation.dispose();
          }
      };

    @PostConstruct
    private void initialize()
      {
        PersonRegistryHelper.populate(personRegistry);
        presentation.bind(okAction, personRegistry);
      }
  }
