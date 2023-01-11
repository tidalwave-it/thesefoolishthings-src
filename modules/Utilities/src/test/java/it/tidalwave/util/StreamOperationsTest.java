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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class StreamOperationsTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void zipTest1()
      {
        // when
        final Stream<String> underTest = StreamOperations.zip(IntStream.range(0, 7).boxed(),
                                                              stringStream( 5),
                                                              (n, s) -> String.format("%d - %s", n, s));
        // then
        assertThat(underTest.collect(toList()), is(asList("0 - string-a",
                                                          "1 - string-b",
                                                          "2 - string-c",
                                                          "3 - string-d",
                                                          "4 - string-e")));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void zipPairTest1()
      {
        // when
        final Stream<Pair<Integer, String>> underTest = StreamOperations.zip(IntStream.range(0, 7).boxed(),
                                                                             stringStream( 5));
        // then
        assertThat(underTest.collect(toList()), is(asList(Pair.of(0, "string-a"),
                                                          Pair.of(1, "string-b"),
                                                          Pair.of(2, "string-c"),
                                                          Pair.of(3, "string-d"),
                                                          Pair.of(4, "string-e"))));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Stream<String> stringStream (@Nonnegative final int size)
      {
        return IntStream.range(0, size).mapToObj(n -> "string-" + (char)('a' + n));
      }
  }
