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
package it.tidalwave.thesefoolishthings.examples.finderexample3;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static it.tidalwave.util.Finder.SortDirection.*;
import static it.tidalwave.thesefoolishthings.examples.finderexample3.JPAExampleFinder.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JPAExampleFinderTest 
  {
    private EntityManagerMockHolder emmh;

    private JPAExampleFinder fixture;

    @Before
    public void setupFixture()
      {
        emmh = new EntityManagerMockHolder();
        fixture = new JPAExampleFinder(emmh.em);
      }
    
    @Test
    public void testSimpleQuery()
      {
        final List<? extends String> results = fixture.results();
        
        assertThat(emmh.sqlQuery, is("SELECT p.firstName FROM Person p"));
        assertThat(emmh.firstResult, is(0));
        assertThat(emmh.maxResults, is(Integer.MAX_VALUE));
      }
    
    @Test
    public void testQueryWithAscendingSortAndFirstMax()
      {
        final List<? extends String> results = fixture.sort(BY_FIRST_NAME).from(2).max(4).results();
        
        assertThat(emmh.sqlQuery, is("SELECT p.firstName FROM Person p ORDER BY p.firstName"));
        assertThat(emmh.firstResult, is(2));
        assertThat(emmh.maxResults, is(4));
      }
    
    @Test
    public void testQueryWithDesccendingSortAndFirstMax()
      {
        final List<? extends String> results = fixture.sort(BY_LAST_NAME, DESCENDING).from(2).max(4).results();
        
        assertThat(emmh.sqlQuery, is("SELECT p.firstName FROM Person p ORDER BY p.lastName DESC"));
        assertThat(emmh.firstResult, is(2));
        assertThat(emmh.maxResults, is(4));
      }
    
    @Test
    public void testQueryWithCount()
      {
        final int count = fixture.count();
        
        assertThat(emmh.sqlQuery, is("SELECT COUNT(*) FROM Person p"));
        assertThat(emmh.firstResult, is(0));
        assertThat(emmh.maxResults, is(Integer.MAX_VALUE));
      }
  }