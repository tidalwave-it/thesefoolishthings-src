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
package it.tidalwave.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PairTest
  {
    private static final String[] array = {"one", "two", "three", "four", "five" };

    private static final Iterable<String> iterable = Arrays.asList(array);

    private static final IntFunction<String> indexTransformer = i -> String.format("%d", i + 1);

    private static final IntFunction<String> valueSupplier = i -> String.format("#%d", i);

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_array()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(array);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(0, "one"),
                                                          Pair.of(1, "two"),
                                                          Pair.of(2, "three"),
                                                          Pair.of(3, "four"),
                                                          Pair.of(4, "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_array_and_rebaser()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(array, Pair.BASE_1);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(1, "one"),
                                                          Pair.of(2, "two"),
                                                          Pair.of(3, "three"),
                                                          Pair.of(4, "four"),
                                                          Pair.of(5, "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream__from_array_with_index_transformer()
      {
        // when
        final Stream<Pair<String, String>> underTest = Pair.indexedPairStream(array, Pair.BASE_0, indexTransformer);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of("1", "one"),
                                                          Pair.of("2", "two"),
                                                          Pair.of("3", "three"),
                                                          Pair.of("4", "four"),
                                                          Pair.of("5", "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_list_as_iterable()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(iterable);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(0, "one"),
                                                          Pair.of(1, "two"),
                                                          Pair.of(2, "three"),
                                                          Pair.of(3, "four"),
                                                          Pair.of(4, "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_list_as_iterable_and_rebaser()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(iterable, Pair.BASE_1);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(1, "one"),
                                                          Pair.of(2, "two"),
                                                          Pair.of(3, "three"),
                                                          Pair.of(4, "four"),
                                                          Pair.of(5, "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream__from_list_as_iterable_with_index_transformer()
      {
        // when
        final Stream<Pair<String, String>> underTest = Pair.indexedPairStream(iterable, Pair.BASE_0, indexTransformer);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of("1", "one"),
                                                          Pair.of("2", "two"),
                                                          Pair.of("3", "three"),
                                                          Pair.of("4", "four"),
                                                          Pair.of("5", "five"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_with_range_and_supplier()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(3, 9, valueSupplier);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(3, "#3"),
                                                          Pair.of(4, "#4"),
                                                          Pair.of(5, "#5"),
                                                          Pair.of(6, "#6"),
                                                          Pair.of(7, "#7"),
                                                          Pair.of(8, "#8"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_with_range_and_supplier_and_rebaser()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = Pair.indexedPairStream(3, 9,
                                                                               valueSupplier, Pair.BASE_1);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(4, "#3"),
                                                          Pair.of(5, "#4"),
                                                          Pair.of(6, "#5"),
                                                          Pair.of(7, "#6"),
                                                          Pair.of(8, "#7"),
                                                          Pair.of(9, "#8"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_with_range_and_supplier_and_index_transformer()
      {
        // when
        final Stream<Pair<String, String>> underTest = Pair.indexedPairStream(3, 9,
                                                                              valueSupplier,
                                                                              Pair.BASE_0,
                                                                              indexTransformer);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of("4", "#3"),
                                                          Pair.of("5", "#4"),
                                                          Pair.of("6", "#5"),
                                                          Pair.of("7", "#6"),
                                                          Pair.of("8", "#7"),
                                                          Pair.of("9", "#8"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_collector_to_map()
      {
        // given
        final Stream<Pair<Integer, String>> stream = Pair.indexedPairStream(array);
        // when
        final Map<Integer, String> actual = stream.collect(Pair.pairsToMap());
        // then
        final Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "one");
        expected.put(1, "two");
        expected.put(2, "three");
        expected.put(3, "four");
        expected.put(4, "five");
        assertThat(actual, is(expected));
      }
  }
