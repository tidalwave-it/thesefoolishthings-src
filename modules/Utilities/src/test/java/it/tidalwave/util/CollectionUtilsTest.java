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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        // GIVEN
        final var l1 = IntStream.rangeClosed(1, 9).boxed().collect(toList());
        final Integer i2 = 10;
        // WHEN
        final var actual = concat(l1, i2);
        // THEN
        final var expected = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_concat_list()
      {
        // GIVEN
        final var l1 = IntStream.rangeClosed(1, 5).boxed().collect(toList());
        final var l2 = IntStream.rangeClosed(6, 10).boxed().collect(toList());
        // WHEN
        final var actual = concat(l1, l2);
        // THEN
        final var expected = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_reversed()
      {
        // GIVEN
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // WHEN
        final var actual = reversed(l);
        // THEN
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
        // GIVEN
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // WHEN
        final var actual = optionalHead(l);
        // THEN
        final var expected = Optional.of(1);
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_optionalHead_with_empty_list()
      {
        // GIVEN
        final List<Integer> l = Collections.emptyList();
        // WHEN
        final var actual = optionalHead(l);
        // THEN
        final Optional<Integer> expected = Optional.empty();
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_head()
      {
        // GIVEN
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // WHEN
        final var actual = head(l);
        // THEN
        final Integer expected = 1;
        assertThat(expected, is(actual));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "List is empty")
    public void test_head_with_empty_list()
      {
        // GIVEN
        final List<Integer> l = Collections.emptyList();
        // WHEN
        final var actual = head(l);
        // THEN
        // throws exception...
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_tail()
      {
        // GIVEN
        final var l = IntStream.rangeClosed(1, 10).boxed().collect(toList());
        // WHEN
        final var actual = tail(l);
        // THEN
        final var expected = IntStream.rangeClosed(2, 10).boxed().collect(toList());
        assertThat(expected, is(actual));
      }
  }
