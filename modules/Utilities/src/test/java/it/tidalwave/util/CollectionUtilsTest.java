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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.*;
import static it.tidalwave.util.CollectionUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class CollectionUtilsTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_concat()
      {
        // given
        final var l1 = IntStream.rangeClosed(1, 9).boxed().collect(toList());
        final Integer i2 = 10;
        // when
        final var actual = concat(l1, i2);
        // then
        final var expected = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_concat_list()
      {
        // given
        final var l1 = IntStream.rangeClosed(1, 5).boxed().collect(toList());
        final var l2 = IntStream.rangeClosed(6, 10).boxed().collect(toList());
        // when
        final var actual = concat(l1, l2);
        // then
        final var expected = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_reversed()
      {
        // given
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // when
        final var actual = reversed(l);
        // then
        final var expected = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        Collections.reverse(expected);
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_optionalHead()
      {
        // given
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // when
        final var actual = optionalHead(l);
        // then
        final var expected = Optional.of(1);
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_optionalHead_with_empty_list()
      {
        // given
        final List<Integer> l = Collections.emptyList();
        // when
        final var actual = optionalHead(l);
        // then
        final Optional<Integer> expected = Optional.empty();
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_head()
      {
        // given
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // when
        final var actual = head(l);
        // then
        final Integer expected = 1;
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "List is empty")
    public void test_head_with_empty_list()
      {
        // given
        final List<Integer> l = Collections.emptyList();
        // when
        final var actual = head(l);
        // then
        // throws exception...
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    public void test_tail()
      {
        // given
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // when
        final var actual = tail(l);
        // then
        final var expected = IntStream.rangeClosed(2, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(invocationCount = 5)
    public void test_sorted()
      {
        // given
        final var list = new Random().ints().limit(10).boxed().collect(toList());
        // when
        final var actualResult = CollectionUtils.sorted(list);
        // then
        final var expectedResult = new ArrayList<>(list);
        Collections.sort(expectedResult);
        assertThat(actualResult, is(expectedResult));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(invocationCount = 5)
    public void test_sorted_with_comparator()
      {
        // given
        final var list = new Random().ints().limit(10).boxed().collect(toList());
        final Comparator<Integer> comparator = Comparator.reverseOrder();
        // when
        final var actualResult = CollectionUtils.sorted(list, comparator);
        // then
        final var expectedResult = new ArrayList<>(list);
        expectedResult.sort(comparator);
        assertThat(actualResult, is(expectedResult));
      }
  }
