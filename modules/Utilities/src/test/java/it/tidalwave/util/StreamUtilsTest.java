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

import javax.annotation.Nonnegative;
import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StreamUtilsTest
  {
    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void zipTest1()
      {
        // when
        final var underTest = StreamUtils.zip(IntStream.range(0, 7).boxed(),
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
     * @param seed            a random seed
     * @param from            the lower bound of the range
     * @param to              the upper bound of the range
     * @param expectedResult  the expected range
     ******************************************************************************************************************/
    @Test(dataProvider = "randomLocalDateTimeStreamData")
    public void test_randomLocalDateTimeStream (final long seed,
                                                @Nonnull final LocalDateTime from,
                                                @Nonnull final LocalDateTime to,
                                                @Nonnull final List<LocalDateTime> expectedResult)
      {
        // when
        final var underTest = StreamUtils.randomLocalDateTimeStream(seed, from, to);
        // then
        assertThat(underTest.limit(expectedResult.size()).collect(toList()), is(expectedResult));
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @DataProvider
    private static Object[][] randomLocalDateTimeStreamData()
      {
        return new Object[][]
          {
            { 7,
              LocalDateTime.of(2022, 1, 1, 0, 0),
              LocalDateTime.of(2023, 1, 12, 0, 0),
              List.of(LocalDateTime.of(2022, 10, 26, 17, 17),
                      LocalDateTime.of(2022, 7, 3, 12, 10, 4),
                      LocalDateTime.of(2022, 6, 22, 16, 26, 22))
            },
            { 5,
              LocalDateTime.of(1970, 1, 1, 0, 0),
              LocalDateTime.of(2000, 1, 12, 0, 0),
              List.of(LocalDateTime.of(1986, 2, 28, 15, 41, 32),
                      LocalDateTime.of(1974, 3, 21, 2, 1, 20),
                      LocalDateTime.of(1995, 4, 18, 20, 49, 41),
                      LocalDateTime.of(1979, 12, 18, 15, 37, 7),
                      LocalDateTime.of(1971, 10, 11, 20, 3, 53))
            }
          };
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Nonnull
    private static Stream<String> stringStream (@Nonnegative final int size)
      {
        return IntStream.range(0, size).mapToObj(n -> "string-" + (char)('a' + n));
      }
  }
