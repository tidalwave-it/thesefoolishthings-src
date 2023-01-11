/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import java.util.List;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl.JpaFinder.*;
import static it.tidalwave.util.Finder.SortDirection.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class JpaFinderTest
  {
    private JpaMockHelper jpaMock;

    private JpaFinder underTest;

    @BeforeMethod
    public void setup()
      {
        jpaMock = new JpaMockHelper();
        underTest = new JpaFinder<>(PersonEntity.class, PersonEntity::toPerson, jpaMock.mockTxManager);
      }

    // START SNIPPET: tests
    @Test
    public void testSimpleQuery()
      {
        // when
        final List<? extends Person> results = underTest.results();
        // then
        assertThat(jpaMock.sqlQuery, is("SELECT p FROM PersonEntity p"));
        assertThat(jpaMock.firstResult, is(0));
        assertThat(jpaMock.maxResults, is(Integer.MAX_VALUE));
      }

    @Test
    public void testQueryWithAscendingSortAndFirstMax()
      {
        // when
        final List<? extends Person> results = underTest.sort(BY_FIRST_NAME).from(2).max(4).results();
        // then
        assertThat(jpaMock.sqlQuery, is("SELECT p FROM PersonEntity p ORDER BY p.firstName"));
        assertThat(jpaMock.firstResult, is(2));
        assertThat(jpaMock.maxResults, is(4));
      }

    @Test
    public void testQueryWithDescendingSortAndFirstMax()
      {
        // when
        final List<? extends Person> results = underTest.sort(BY_LAST_NAME, DESCENDING).from(3).max(7).results();
        // then
        assertThat(jpaMock.sqlQuery, is("SELECT p FROM PersonEntity p ORDER BY p.lastName DESC"));
        assertThat(jpaMock.firstResult, is(3));
        assertThat(jpaMock.maxResults, is(7));
      }

    @Test
    public void testQueryWithDoubleSort()
      {
        // when
        final List<? extends Person> results = underTest.sort(BY_LAST_NAME, DESCENDING)
                                                        .sort(BY_FIRST_NAME, ASCENDING)
                                                        .results();
        // then
        assertThat(jpaMock.sqlQuery, is("SELECT p FROM PersonEntity p ORDER BY p.lastName DESC, p.firstName"));
        assertThat(jpaMock.firstResult, is(0));
        assertThat(jpaMock.maxResults, is(Integer.MAX_VALUE));
      }

    @Test
    public void testQueryWithCount()
      {
        // when
        final int count = underTest.count();
        // then
        assertThat(jpaMock.sqlQuery, is("SELECT COUNT(p) FROM PersonEntity p"));
        assertThat(jpaMock.firstResult, is(0));
        assertThat(jpaMock.maxResults, is(Integer.MAX_VALUE));
      }
    // END SNIPPET: tests
  }
