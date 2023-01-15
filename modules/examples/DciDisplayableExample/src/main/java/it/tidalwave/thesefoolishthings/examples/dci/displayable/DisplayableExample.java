/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.dci.displayable;

import it.tidalwave.util.AsExtensions;
import it.tidalwave.util.Id;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Displayable._Displayable_;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// START-SNIPPET: extensionmethod
@ExtensionMethod(AsExtensions.class) @Slf4j
public class DisplayableExample
  {
    public void run()
      {
        var joe = new Person(new Id("1"), "Joe", "Smith");
        var luke = new Person(new Id("2"), "Luke", "Skywalker");
        
        log.info("******** (joe as Displayable).displayName: {}", joe.as(_Displayable_).getDisplayName());
        log.info("******** (luke as Displayable).displayName: {}", luke.as(_Displayable_).getDisplayName());
      }
  }
// END-SNIPPET: extensionmethod
