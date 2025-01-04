/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import it.tidalwave.util.spi.ExtendedFinderSupport;
import it.tidalwave.util.spi.HierarchicFinderSupport;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
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
        protected List<String> computeResults()
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

    @Test
    public void test_Finder_ofSupplier()
      {
        // START SNIPPET: ofsupplier-example
        // given
        final var list = List.of(6, 3, 8, 4, 1);
        final var called = new AtomicBoolean(false);
        final Supplier<Collection<Integer>> supplier = () -> { called.set(true); return list; };
        final var underTest = Finder.ofSupplier(supplier);
        final var calledTooEarly = called.get();
        // when
        final var actualResult = underTest.results();
        // then
        assertThat(actualResult, is(list));
        assertThat(called.get(), is(true));
        assertThat(calledTooEarly, is(false));
        // END SNIPPET: ofsupplier-example
      }

    @Test
    public void test_Finder_ofProvider()
      {
        // START SNIPPET: ofProvider-example
        // given
        final BiFunction<Integer, Integer, List<String>> provider =
                // This stands for a complex computation to make data available
                (from, max) -> IntStream.range(from, Math.min(from + max, 10))
                                        .mapToObj(Integer::toString)
                                        .collect(toList());
        final var underTest = Finder.ofProvider(provider);
        // when
        final var actualResult1 = underTest.results();
        final var actualResult2 = underTest.from(4).max(3).results();
        // then
        final var expectedResult1 = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        final var expectedResult2 = List.of("4", "5", "6");
        assertThat(actualResult1, is(expectedResult1));
        assertThat(actualResult2, is(expectedResult2));
        // END SNIPPET: ofProvider-example
      }

    @Test
    public void test_Finder_mapping()
      {
        // START SNIPPET: mapping-example
        // given
        final var list = List.of(9, 5, 7, 6, 3);
        final var delegate = Finder.ofCloned(list);
        final Function<Integer, String> multiplyAndStringify = n -> Integer.toString(n * 2);
        final var underTest = Finder.mapping(delegate, multiplyAndStringify);
        // when
        final var actualResult1 = underTest.results();
        final var actualResult2 = underTest.from(2).max(2).results();
        // then
        final var expectedResult1 = List.of("18", "10", "14", "12", "6");
        final var expectedResult2 = List.of("14", "12");
        assertThat(actualResult1, is(expectedResult1));
        assertThat(actualResult2, is(expectedResult2));
        // END SNIPPET: mapping-example
      }
  }
