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
package it.tidalwave.thesefoolishthings.examples.finderexample1;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import it.tidalwave.util.Finder;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistry;
import it.tidalwave.thesefoolishthings.examples.person.Utils;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.finderexample1.PersonSortCriteria.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class FinderExample1
  {
    public static void main (@Nonnull final String ... args)
      {
        // START SNIPPET: ofCloned
        final List<String> names = IntStream.range(0, 100)
                                            .mapToObj(n -> String.format("Item #%d", n))
                                            .collect(Collectors.toList());
        System.out.println("From in memory: "
                           + Finder.ofCloned(names).from(10).max(5).results());
        // END SNIPPET: ofCloned

        final PersonRegistry registry = new PersonRegistryImpl1();
        Utils.populatePresidents(registry);

        // START SNIPPET: basic-example
        System.out.println("All: "
                           + registry.findPerson()
                                     .results());

        System.out.println("Two persons from the 3rd position: "
                           + registry.findPerson()
                                     .from(3)
                                     .max(2)
                                     .results());
        // END SNIPPET: basic-example

        // START SNIPPET: sort-example
        System.out.println("All, sorted by first name: "
                           + registry.findPerson()
                                     .sort(BY_FIRST_NAME)
                                     .results());

        System.out.println("All, sorted by last name, descending: "
                           + registry.findPerson()
                                     .sort(BY_LAST_NAME, DESCENDING)
                                     .results());
        // END SNIPPET: sort-example
      }
  }
