/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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

import it.tidalwave.util.NotFoundException;
import it.tidalwave.thesefoolishthings.examples.person.Utils;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.finderexample1.PersonSortCriterion.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PersonFinderTest
  {
    private PersonFinder finder;

    @BeforeMethod
    public void setup()
      {
        final PersonRegistry2 registry = new DefaultPersonRegistry2();
        Utils.populatePresidents(registry);
        finder = registry.findPerson();
      }

    @Test
    public void testAllPersons()
      {
        assertThat(finder.results().toString(),
                   is("[Richard Nixon, Jimmy Carter, Ronald Reagan, George Bush, "
                    + "Bill Clinton, George Walker Bush, Barack Obama]"));
      }


    @Test
    public void testAllPersonsSortedByFirstName()
      {
        assertThat(finder.sort(BY_FIRST_NAME).results().toString(),
                   is("[Barack Obama, Bill Clinton, George Bush, George Walker Bush, "
                    + "Jimmy Carter, Richard Nixon, Ronald Reagan]"));
      }

    @Test
    public void testAllPersonsSortedByLastNameDescending()
      {
        assertThat(finder.sort(BY_LAST_NAME, DESCENDING).results().toString(),
                   is("[Ronald Reagan, Barack Obama, Richard Nixon, Bill Clinton, "
                    + "Jimmy Carter, George Bush, George Walker Bush]"));
      }

    @Test
    public void testPersonRange()
      {
        assertThat(finder.from(3).max(2).results().toString(),
                   is("[George Bush, Bill Clinton]"));
      }

    @Test
    public void testFirstNameStartingWithB()
      {
        assertThat(finder.withFirstName("B.*").results().toString(),
                   is("[Bill Clinton, Barack Obama]"));
      }

    @Test
    public void testFirstNameStartingWithBSortedByFirstName()
      {
        assertThat(finder.withFirstName("B.*").sort(BY_FIRST_NAME).results().toString(),
                   is("[Barack Obama, Bill Clinton]"));
      }

    @Test
    public void testLastNameIsBushFirstResult()
      throws NotFoundException
      {
        assertThat(finder.withLastName("Bush").firstResult().toString(),
                   is("George Bush"));
      }
  }
