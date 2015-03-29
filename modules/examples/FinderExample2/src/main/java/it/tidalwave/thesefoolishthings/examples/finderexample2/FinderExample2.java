/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import javax.annotation.Nonnull;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.thesefoolishthings.examples.person.Utils;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.finderexample1.PersonSortCriterion.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FinderExample2
  {
    public static void main (final @Nonnull String ... args)
      throws NotFoundException
      {
        final PersonRegistry2 registry = new DefaultPersonRegistry2();
        Utils.populatePresidents(registry);

        //@bluebook-begin example
        System.out.println("All: "
                           + registry.findPerson().results());

        System.out.println("All, sorted by first name: "
                           + registry.findPerson().sort(BY_FIRST_NAME).results());

        System.out.println("All, sorted by last name, descending: "
                           + registry.findPerson().sort(BY_LAST_NAME, DESCENDING).results());

        System.out.println("Two persons from the 3rd position: "
                           + registry.findPerson().from(3).max(2).results());

        final PersonFinder withFirstNameStartingWithB = registry.findPerson().withFirstName("B.*");

        System.out.println("Whose first name starts with B: "
                           + withFirstNameStartingWithB.results());

        System.out.println("Whose first name starts with B, sorted by first name: "
                           + withFirstNameStartingWithB.sort(BY_FIRST_NAME).results());

        System.out.println("The first found whose last name is Bush: "
                           + registry.findPerson().withLastName("Bush").firstResult());
        //@bluebook-end example
     }
  }
