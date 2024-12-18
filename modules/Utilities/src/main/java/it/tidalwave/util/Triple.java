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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A value object that contains a triple of values.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-12
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Getter @Immutable @RequiredArgsConstructor(staticName = "of") @ToString @EqualsAndHashCode
public class Triple<A, B, C>
  {
    @Nonnull
    public final A a;

    @Nonnull
    public final B b;

    @Nonnull
    public final C c;

    /*******************************************************************************************************************
     *
     * Creates a {@code Triple} from a {@code Pair} and another value.
     *
     * @param   <T>     the former type of the {@code Pair}
     * @param   <U>     the latter type of the {@code Pair}
     * @param   <V>     the type of the value
     * @param   pair    the {@code Pair}
     * @param   value   the value
     * @return          the {@code Stream} of {@code Triple}s
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, U, V> Triple<T, U, V> of (@Nonnull final Pair<T, U> pair, @Nonnull final V value)
      {
        return of(pair.a, pair.b, value);
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link Stream} of {@code Triple}s composed of a given {@code Pair} and another element taken from
     * {@link Stream}.
     *
     * @param   <T>     the former type of the {@code Pair}
     * @param   <U>     the latter type of the {@code Pair}
     * @param   <V>     the type of the {@code Stream}
     * @param   pair    the {@code Pair}
     * @param   stream  the {@code Stream}
     * @return          the {@code Stream} of {@code Triple}s
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, U, V> Stream<Triple<T, U, V>> tripleStream (@Nonnull final Pair<T, U> pair,
                                                                  @Nonnull final Stream<? extends V> stream)
      {
        return stream.map(value -> of(pair, value));
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link Stream} of {@code Triple}s composed of a given fixed {@code Pair} and an integer in the given
     * range.
     *
     * @param   <T>     the former type of the {@code Pair}
     * @param   <U>     the latter type of the {@code Pair}
     * @param   pair    the {@code Pair}
     * @param   from    the first value of the integer {@code Stream} (included)
     * @param   to      the last value of the integer {@code Stream} (excluded)
     * @return          the {@code Stream} of {@code Triple}s
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, U> Stream<Triple<T, U, Integer>> tripleRange (@Nonnull final Pair<T, U> pair,
                                                                    @Nonnegative final int from,
                                                                    @Nonnegative final int to)
      {
        return tripleStream(pair, IntStream.range(from, to).boxed());
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link Stream} of {@code Triple}s composed of a given fixed {@code Pair} and an integer in the given
     * range.
     *
     * @param   <T>     the former type of the {@code Pair}
     * @param   <U>     the latter type of the {@code Pair}
     * @param   pair    the {@code Pair}
     * @param   from    the first value of the integer {@code Stream} (included)
     * @param   to      the last value of the integer {@code Stream} (included)
     * @return          the {@code Stream} of {@code Triple}s
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, U> Stream<Triple<T, U, Integer>> tripleRangeClosed (@Nonnull final Pair<T, U> pair,
                                                                          @Nonnegative final int from,
                                                                          @Nonnegative final int to)
      {
        return tripleStream(pair, IntStream.rangeClosed(from, to).boxed());
      }
  }
