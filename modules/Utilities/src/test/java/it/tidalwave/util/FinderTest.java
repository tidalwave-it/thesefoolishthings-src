/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import it.tidalwave.util.spi.ExtendedFinderSupport;
import it.tidalwave.util.spi.HierarchicFinderSupport;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class FinderTest
  {
    private static final List<String> ALL_NAMES = List.of("");

    static interface NameFinder extends ExtendedFinderSupport<String, NameFinder>
      {
        @Nonnull
        public NameFinder startingWith (@Nonnull String prefix);
      }

    static class NameFinderImplementation extends HierarchicFinderSupport<String, NameFinderImplementation> implements NameFinder
      {
        private String prefix = "";

        public NameFinderImplementation()
          {
          }

        @Override @Nonnull
        protected List<? extends String> computeResults()
          {
            final List<String> results = new ArrayList<>();

            for (final var name : ALL_NAMES)
              {
                if (name.startsWith(prefix))
                  {
                    results.add(name);
                  }
              }

            return results;
          }

        @Nonnull
        public NameFinder startingWith (@Nonnull final String prefix)
          {
            this.prefix = prefix;
            return this;
          }
      }

    private static final List<Integer> ITEMS = IntStream.range(0, 10).boxed().collect(toList());

    @Test
    public void ofClone_must_behave_correctly()
      {
        // when
        final var underTest = Finder.ofCloned(ITEMS);
        // then
        assertThat(underTest.results(), is(ITEMS));
        assertThat(underTest.from(5).results(), is(ITEMS.subList(5, 10)));
        assertThat(underTest.from(7).max(2).results(), is(ITEMS.subList(7, 9)));
      }

    @Test
    public void ofClone_result_must_be_a_modifiable_list()
      {
        // when
        final var underTest = Finder.ofCloned(ITEMS);
        // then
        underTest.results().clear();
        underTest.from(5).results().clear();
      }

    public void test1() // just to see the syntax
      {
//        Composite<String, Finder<String>> composite = null;
//        List<? extends String> results1 = composite.findChildren().max(10).results();
//        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }

    public void test2() // just to see the syntax
      {
//        Composite<String, NameFinder> composite = null;
//        List<? extends String> results1 = composite.findChildren().from(2).startingWith("A").max(10).results();
//        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }
  }
