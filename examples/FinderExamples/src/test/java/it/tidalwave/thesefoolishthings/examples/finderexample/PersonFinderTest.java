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
package it.tidalwave.thesefoolishthings.examples.finderexample;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PersonFinderTest 
  {
    private PersonFinder fixture;
    
    @Before
    public void setupFixture() 
      {
        fixture = new PersonRegistry().findPersons();
      }

    @Test
    public void test1()
      {
        assertThat(fixture.results().toString(),
                   is("[Richard Nixon, Jimmy Carter, Ronald Reagan, George Bush, "
                    + "Bill Clinton, George Walker Bush, Barack Obama]"));
      }
    
    @Test
    public void test2()
      {
        assertThat(fixture.withFirstName("B.*").results().toString(),
                   is("[Bill Clinton, Barack Obama]"));
      }
    
    @Test
    public void test2a()
      {
        assertThat(fixture.withFirstName("B.*").sort(PersonSortCriterion.BY_FIRST_NAME).results().toString(),
                   is("[Barack Obama, Bill Clinton]"));
      }
    
    @Test
    public void test3()
      {
        assertThat(fixture.withLastName("Bush").results().toString(),
                   is("[George Bush, George Walker Bush]"));
      }
  }