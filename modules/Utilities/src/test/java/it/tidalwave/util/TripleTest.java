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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.*;

/***************************************************************************************************************************************************************
 *
 **************************************************************************************************************************************************************/
public class TripleTest
  {
    /**********************************************************************************************************************************************************/
    @Test
    public void test_Triple()
      {
        // when
        // START SNIPPET: triple1
        final var t = Triple.of("foo bar", 7, false);
        final var fooBar = t.a;
        final var seven = t.b;
        final var bool = t.c;
        // END SNIPPET: triple1
        // then
        assertThat(fooBar).isEqualTo("foo bar");
        assertThat(seven).isEqualTo(7);
        assertThat(bool).isFalse();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_tripleStream()
      {
        // given
        final var pair = Pair.of("value", false);
        // when
        final var underTest = Triple.tripleStream(pair, IntStream.rangeClosed(1, 5).boxed());
        // then
        assertThat(underTest.collect(toList())).containsExactly(Triple.of(pair, 1),
                                                                Triple.of(pair, 2),
                                                                Triple.of(pair, 3),
                                                                Triple.of(pair, 4),
                                                                Triple.of(pair, 5));
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_tripleRange()
      {
        // given
        final var pair = Pair.of("value", false);
        // when
        final var underTest = Triple.tripleRange(pair, 1, 5);
        // then
        assertThat(underTest.collect(toList())).containsExactly(Triple.of(pair, 1),
                                                                Triple.of(pair, 2),
                                                                Triple.of(pair, 3),
                                                                Triple.of(pair, 4));
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_tripleRangeClosed()
      {
        // given
        final var pair = Pair.of("value", false);
        // when
        final var underTest = Triple.tripleRangeClosed(pair, 1, 5);
        // then
        assertThat(underTest.collect(toList())).containsExactly(Triple.of(pair, 1),
                                                                Triple.of(pair, 2),
                                                                Triple.of(pair, 3),
                                                                Triple.of(pair, 4),
                                                                Triple.of(pair, 5));
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void testTripleNestedLoops()
      {
        // given
        final var limit = 20;
        // when
        // START SNIPPET: loop2a
        final var actual = IntStream.rangeClosed(1, limit)
                                    .boxed()
                                    .flatMap(a -> Pair.pairRangeClosed(a, a + 1, limit))
                                    .flatMap(p -> Triple.tripleRangeClosed(p, p.b + 1, limit))
                                    .collect(toList());
        // END SNIPPET: loop2a
        // then
        // START SNIPPET: loop2b
        final List<Triple<Integer, Integer, Integer>> expected = new ArrayList<>();

        for (var a = 1; a <= limit; a++)
          {
            for (var b = a + 1; b <= limit; b++)
              {
                for (var c = b + 1; c <= limit; c++)
                  {
                    expected.add(Triple.of(a, b, c));
                  }
              }
          }
        // END SNIPPET: loop2b

        assertThat(actual).isEqualTo(expected);
      }
  }
