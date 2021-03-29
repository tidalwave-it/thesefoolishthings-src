/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/thesefoolishthings-examples/it-tidalwave-thesefoolishthings-examples-finderexample2
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.extendedfinderexample;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistryHelper;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.inmemoryfinderexample.PersonSortCriteria.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @hidden
 *
 **********************************************************************************************************************/
public class FinderExample2
  {
    public static void main (@Nonnull final String ... args)
      {
        final PersonRegistry2 registry = new PersonRegistryImpl2();
        PersonRegistryHelper.populate(registry);

        System.out.println("All: "
                           + registry.findPerson()
                                     .results());

        System.out.println("All, sorted by first name: "
                           + registry.findPerson()
                                     .sort(BY_FIRST_NAME)
                                     .results());

        System.out.println("All, sorted by last name, descending: "
                           + registry.findPerson()
                                     .sort(BY_LAST_NAME, DESCENDING)
                                     .results());

        System.out.println("Two persons from the 3rd position: "
                           + registry.findPerson()
                                     .from(3)
                                     .max(2)
                                     .results());

        final PersonFinder withFirstNameStartingWithB = registry.findPerson();

        // START SNIPPET: extended-example
        System.out.println("Whose first name starts with B: "
                           + registry.findPerson()
                                     .withFirstName("B.*")
                                     .results());

        System.out.println("Whose first name starts with B, sorted by first name: "
                           + registry.findPerson()
                                     .sort(BY_FIRST_NAME)
                                     .withFirstName("B.*")
                                     .results());
        // END SNIPPET: extended-example

        System.out.println("The first found whose last name is Bush: "
                           + registry.findPerson()
                                     .withFirstName("B.*")
                                     .withLastName("Bush")
                                     .optionalFirstResult());

        // START SNIPPET: stream-example
        // Here both filtering and sorting are performed by the Finder, which could make it happen in the data source.
        System.out.println("Whose first name starts with B, sorted by first name: "
                           + registry.findPerson()
                                     .withFirstName("B.*")
                                     .sort(BY_FIRST_NAME)
                                     .results());

        // Here filtering is performed as above, but sorting is done in memory after all data have been retrieved.
        System.out.println("Whose first name starts with B, sorted by first name: "
                           + registry.findPerson()
                                     .withFirstName("B.*")
                                     .stream()
                                     .sorted(Comparator.comparing(Person::getFirstName))
                                     .collect(Collectors.toList()));

        // Here both filtering and sorting are performed in memory.
        System.out.println("Whose first name starts with B, sorted by first name: "
                           + registry.findPerson()
                                     .stream()
                                     .filter(p -> Pattern.matches("B.*", p.getFirstName()))
                                     .sorted(Comparator.comparing(Person::getFirstName))
                                     .collect(Collectors.toList()));
        // END SNIPPET: stream-example
     }
  }