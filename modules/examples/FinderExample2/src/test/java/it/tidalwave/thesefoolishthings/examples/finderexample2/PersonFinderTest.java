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
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import it.tidalwave.util.NotFoundException;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistryHelper;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.finderexample1.PersonSortCriteria.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PersonFinderTest
  {
    private PersonFinder underTest;

    @BeforeMethod
    public void setup()
      {
        final PersonRegistry2 registry = new PersonRegistryImpl2();
        PersonRegistryHelper.populate(registry);
        underTest = registry.findPerson();
      }

    @Test
    public void testAllPersons()
      {
        assertThat(underTest.results().toString(),
                   is("[Michelangelo Buonarroti, Lorenzo Bernini, Leonardo Da Vinci, Pietro Perugino, " +
                      "Paolo Uccello, Andrea Mantegna, Ambrogio Lorenzetti, Piero della Francesca, Giotto da Bondone]"));
      }


    @Test
    public void testAllPersonsSortedByFirstName()
      {
        assertThat(underTest.sort(BY_FIRST_NAME).results().toString(),
                   is("[Ambrogio Lorenzetti, Andrea Mantegna, Giotto da Bondone, Leonardo Da Vinci, " +
                      "Lorenzo Bernini, Michelangelo Buonarroti, Paolo Uccello, Piero della Francesca, Pietro Perugino]"));
      }

    @Test
    public void testAllPersonsSortedByLastNameDescending()
      {
        assertThat(underTest.sort(BY_LAST_NAME, DESCENDING).results().toString(),
                   is("[Piero della Francesca, Giotto da Bondone, Paolo Uccello, Pietro Perugino, " +
                      "Andrea Mantegna, Ambrogio Lorenzetti, Leonardo Da Vinci, Michelangelo Buonarroti, Lorenzo Bernini]"));
      }

    @Test
    public void testPersonRange()
      {
        assertThat(underTest.from(3).max(2).results().toString(),
                   is("[Pietro Perugino, Paolo Uccello]"));
      }

    @Test
    public void testLastNameStartingWithB()
      {
        assertThat(underTest.withLastName("B.*").results().toString(),
                   is("[Michelangelo Buonarroti, Lorenzo Bernini]"));
      }

    @Test
    public void testLastNameStartingWithBSortedByFirstName()
      {
        assertThat(underTest.withLastName("B.*").sort(BY_FIRST_NAME).results().toString(),
                   is("[Lorenzo Bernini, Michelangelo Buonarroti]"));
      }

    @Test
    public void testLastNameIsBerniniFirstResult()
      throws NotFoundException
      {
        assertThat(underTest.withLastName("Bernini").optionalFirstResult().get().toString(),
                   is("Lorenzo Bernini"));
      }
  }
