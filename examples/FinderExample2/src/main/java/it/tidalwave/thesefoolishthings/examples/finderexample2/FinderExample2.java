/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import javax.annotation.Nonnull;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.thesefoolishthings.examples.finderexample1.Person;
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

        registry.add(new Person("Richard", "Nixon"));
        registry.add(new Person("Jimmy", "Carter"));
        registry.add(new Person("Ronald", "Reagan"));
        registry.add(new Person("George", "Bush"));
        registry.add(new Person("Bill", "Clinton"));
        registry.add(new Person("George Walker", "Bush"));
        registry.add(new Person("Barack", "Obama"));
        
        //@bluebook-begin example
        System.out.println("All: " 
                           + registry.findPersons().results());
        
        System.out.println("All, sorted by first name: " 
                           + registry.findPersons().sort(BY_FIRST_NAME).results());
        
        System.out.println("All, sorted by last name, descending: " 
                           + registry.findPersons().sort(BY_LAST_NAME, DESCENDING).results());
        
        System.out.println("Two persons from the 3rd position: " 
                           + registry.findPersons().from(3).max(2).results());
        
        final PersonFinder withFirstNameStartingWithB = registry.findPersons().withFirstName("B.*");
        
        System.out.println("Whose first name starts with B: " 
                           + withFirstNameStartingWithB.results());
        
        System.out.println("Whose first name starts with B, sorted by first name: " 
                           + withFirstNameStartingWithB.sort(BY_FIRST_NAME).results());
        
        System.out.println("The first found whose last name is Bush: "
                           + registry.findPersons().withLastName("Bush").firstResult());
        //@bluebook-end example
     }
  }
