/*
 * #%L
 * *********************************************************************************************************************
 *
 * SteelBlue
 * http://steelblue.tidalwave.it - git clone git@bitbucket.org:tidalwave/steelblue-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.spi;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class ArrayListFinder8Test
  {
    private final static List<Integer> ITEMS = IntStream.range(0, 10).mapToObj(Integer::valueOf).collect(toList());

    private ArrayListFinder8<Integer> underTest;

    @BeforeMethod
    public void setup()
      {
        underTest = new ArrayListFinder8<>(ITEMS);
      }

    @Test
    public void must_behave_correctly()
      {
        // when
        final List<? extends Integer> fullResults = underTest.results();
        final List<? extends Integer> resultsFrom5 = underTest.from(5).results();
        final List<? extends Integer> resultsFrom7Max2 = underTest.from(7).max(2).results();
        // then
        assertThat(fullResults, is(ITEMS));
        assertThat(resultsFrom5, is(ITEMS.subList(5, 10)));
        assertThat(resultsFrom7Max2, is(ITEMS.subList(7, 9)));
      }

    @Test
    public void result_must_be_a_mofidiable_list()
      {
        underTest.results().clear();
        underTest.from(5).results().clear();
      }
  }
