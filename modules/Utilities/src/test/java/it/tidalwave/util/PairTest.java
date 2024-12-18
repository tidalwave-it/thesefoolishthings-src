/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PairTest
  {
    private static final String[] array = {"one", "two", "three", "four", "five" };

    private static final Iterable<String> iterable = List.of(array);

    private static final IntFunction<String> indexTransformer = i -> String.format("%d", i + 1);

    private static final IntFunction<String> valueSupplier = i -> String.format("#%d", i);

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_Pair()
      {
        // when
        // START SNIPPET: pair1
        final var p = Pair.of("foo bar", 7);
        final var fooBar = p.a;
        final var seven = p.b;
        // END SNIPPET: pair1
        // then
        assertThat(fooBar, is("foo bar"));
        assertThat(seven, is(7));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_pairRange()
      {
        // given
        final var value = "value";
        // when
        final var underTest = Pair.pairRange(value, 1, 5);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(value, 1),
                                                          Pair.of(value, 2),
                                                          Pair.of(value, 3),
                                                          Pair.of(value, 4))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_pairRangeClosed()
      {
        // given
        final var value = "value";
        // when
        final var underTest = Pair.pairRangeClosed(value, 1, 5);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(value, 1),
                                                          Pair.of(value, 2),
                                                          Pair.of(value, 3),
                                                          Pair.of(value, 4),
                                                          Pair.of(value, 5))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void testDoubleNestedLoops()
      {
        // given
        final var limit = 20;
        // when
        // START SNIPPET: loop2a
        final var actual =  IntStream.rangeClosed(1, limit)
                                     .boxed()
                                     .flatMap(a -> Pair.pairRangeClosed(a, a + 1, limit))
                                     .collect(toList());
        // END SNIPPET: loop2a
        // then
        // START SNIPPET: loop2b
        final List<Pair<Integer, Integer>> expected = new ArrayList<>();

        for (var a = 1; a <= limit; a++)
          {
            for (var b = a + 1; b <= limit; b++)
              {
                expected.add(Pair.of(a, b));
              }
          }
        // END SNIPPET: loop2b

        assertThat(actual, is(expected));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_array()
      {
        // START SNIPPET: indexed
        // when
        final var underTest = Pair.indexedPairStream(array);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(0, "one"),
                                                          Pair.of(1, "two"),
                                                          Pair.of(2, "three"),
                                                          Pair.of(3, "four"),
                                                          Pair.of(4, "five"))));
        // END SNIPPET: indexed
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_indexedPairStream_from_array_and_rebaser()
      {
        // when
        final var underTest = Pair.indexedPairStream(array, Pair.BASE_1);
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
        final var underTest = Pair.indexedPairStream(array, Pair.BASE_0, indexTransformer);
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
        final var underTest = Pair.indexedPairStream(iterable);
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
        final var underTest = Pair.indexedPairStream(iterable, Pair.BASE_1);
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
        final var underTest = Pair.indexedPairStream(iterable, Pair.BASE_0, indexTransformer);
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
    public void test_indexedPairStream_from_stream()
      {
        // when
        final var underTest = Pair.indexedPairStream(Arrays.stream(array));
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
    public void test_indexedPairStream_from_stream_and_rebaser()
      {
        // when
        final var underTest = Pair.indexedPairStream(Arrays.stream(array), Pair.BASE_1);
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
    public void test_indexedPairStream__from_stream_with_index_transformer()
      {
        // when
        final var underTest = Pair.indexedPairStream(Arrays.stream(array), Pair.BASE_0, indexTransformer);
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
        final var underTest = Pair.indexedPairStream(3, 9, valueSupplier);
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
        final var underTest = Pair.indexedPairStream(3, 9, valueSupplier, Pair.BASE_1);
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
        final var underTest = Pair.indexedPairStream(3, 9, valueSupplier, Pair.BASE_0, indexTransformer);
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
        final var stream = Pair.indexedPairStream(array);
        // when
        final var actual = stream.collect(Pair.pairsToMap());
        // then
        final var expected = Map.of(
                0, "one",
                1, "two",
                2, "three",
                3, "four",
                4, "five");
        assertThat(actual, is(expected));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_pairStream()
      {
        // given
        final var value = "value";
        // when
        final var underTest = Pair.pairStream(value, IntStream.rangeClosed(1, 5).boxed());
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(value, 1),
                                                          Pair.of(value, 2),
                                                          Pair.of(value, 3),
                                                          Pair.of(value, 4),
                                                          Pair.of(value, 5))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void zipPairTest1()
      {
        // START SNIPPET: zipPairTest1
        // given
        final var intStream = IntStream.range(0, 5).boxed();
        final var stringStream = IntStream.range(0, 5).mapToObj(n -> "string-" + (char)('a' + n));
        // when
        final var underTest = Pair.zip(intStream, stringStream);
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(0, "string-a"),
                                                          Pair.of(1, "string-b"),
                                                          Pair.of(2, "string-c"),
                                                          Pair.of(3, "string-d"),
                                                          Pair.of(4, "string-e"))));
        // END SNIPPET: zipPairTest1
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void examples()
      {
        // START SNIPPET: pairRangeClosed
        final var stream1 = Pair.pairRangeClosed("foo bar", 1, 3);
        // END SNIPPET: pairRangeClosed
        // START SNIPPET: indexedPairStream
        final var stream2 = Pair.indexedPairStream(List.of("foo", "bar"));
        // END SNIPPET: indexedPairStream
      }
  }
