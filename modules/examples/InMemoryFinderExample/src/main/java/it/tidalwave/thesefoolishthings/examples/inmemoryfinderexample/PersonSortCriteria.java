/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/thesefoolishthings-examples/it-tidalwave-thesefoolishthings-examples-finderexample1
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
package it.tidalwave.thesefoolishthings.examples.inmemoryfinderexample;

import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.InMemorySortCriterion;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import static java.util.Comparator.comparing;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public final class PersonSortCriteria
  {
    // START SNIPPET: sort-criteria
    public static final SortCriterion BY_FIRST_NAME = InMemorySortCriterion.of(comparing(Person::getFirstName));

    public static final SortCriterion BY_LAST_NAME = InMemorySortCriterion.of(comparing(Person::getLastName));
    // END SNIPPET: sort-criteria
  }
