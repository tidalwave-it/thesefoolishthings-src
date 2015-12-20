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
package it.tidalwave.thesefoolishthings.examples.dci.swing;

import javax.swing.JFrame;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.thesefoolishthings.examples.dci.swing.swing.SwingPersonPresentation;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.ExtensionMethod;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class) @Slf4j
public class SwingDciExample
  {
    public void run()
      throws Exception
      {
        final SwingPersonPresentation personPresentation = new SwingPersonPresentation();
        final DefaultPersonPresentationControl control = new DefaultPersonPresentationControl(personPresentation);
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(personPresentation);
        frame.setSize(800, 600);
        frame.setVisible(true);
      }
  }
