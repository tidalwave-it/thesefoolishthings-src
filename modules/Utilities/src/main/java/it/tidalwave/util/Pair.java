/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A value object that contains a pair of values. Some factory methods allow to create pairs out of existing collections
 * or arrays associating an index.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-6
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(staticName = "of") @ToString @EqualsAndHashCode
public class Pair<A, B>
  {
    /** A base 0 index rebaser. */
    public static final IntUnaryOperator BASE_0 = i -> i;

    /** A base 1 index rebaser. */
    public static final IntUnaryOperator BASE_1 = i -> i + 1;

    @Getter @Nonnull
    public final A a;

    @Getter @Nonnull
    public final B b;

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in a given array made of {@link Pair}s {@code (index, value)}.
     *
     * @param       <T>             the type of the elements
     * @param       array           the array
     * @return                      the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnull final T[] array)
      {
        return Pair.indexedPairStream(array, BASE_0);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in the array, made of {@link Pair}s {@code (index, value)}. The
     * index can be rebased.
     *
     * @param       <T>               the type of the elements
     * @param       array             the array
     * @param       rebaser           the rebaser of the index (BASE_0, BASE_1 or a similar function)
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnull final T[] array,
                                                                  @Nonnull final IntUnaryOperator rebaser)
      {
        return Pair.indexedPairStream(array, rebaser, i -> i);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in a given array made of {@link Pair}s {@code (index, value)}. The
     * index is transformed with the given function.
     *
     * @param       <I>               the type of the transformed index
     * @param       <T>               the type of the elements
     * @param       array             the array
     * @param       indexTransformer  the transformer of the index
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <I, T> Stream<Pair<I, T>> indexedPairStream (@Nonnull final T[] array,
                                                               @Nonnull final IntFunction<I> indexTransformer)
      {
        return Pair.indexedPairStream(array, BASE_0, indexTransformer);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in the array, made of {@link Pair}s {@code (index, value)}.  The
     * index can be rebased and transformed with specific functions.
     *
     * @param       <T>               the type of the elements
     * @param       <I>               the type of the transformed index
     * @param       array             the array
     * @param       rebaser           the rebaser of the index (BASE_0, BASE_1 or a similar function)
     * @param       indexTransformer  the transformer of the index
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, I> Stream<Pair<I, T>> indexedPairStream (@Nonnull final T[] array,
                                                               @Nonnull final IntUnaryOperator rebaser,
                                                               @Nonnull final IntFunction<I> indexTransformer)
      {
        return IntStream.range(0, array.length).mapToObj(i -> of(indexTransformer.apply(rebaser.applyAsInt(i)), array[i]));
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in a given {@link Iterable} made of {@link Pair}s {@code (index,
     * value)}.
     *
     * @param       <T>             the type of the elements
     * @param       iterable        the iterable
     * @return                      the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnull final Iterable<T> iterable)
      {
        return Pair.indexedPairStream(iterable, BASE_0);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in a given {@link Iterable} made of {@link Pair}s {@code (index,
     * value)}. The index can be rebased.
     *
     * @param       <T>               the type of the elements
     * @param       iterable          the iterable
     * @param       rebaser           the rebaser of the index (BASE_0, BASE_1 or a similar function)
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnull final Iterable<T> iterable,
                                                                  @Nonnull final IntUnaryOperator rebaser)
      {
        return Pair.indexedPairStream(iterable, rebaser, i -> i);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements in a given {@link Iterable} made of {@link Pair}s {@code (index,
     * value)}. The index is transformed with the given function.
     *
     * @param       <I>               the type of the transformed index
     * @param       <T>               the type of the elements
     * @param       iterable          the iterable
     * @param       indexTransformer  the transformer of the index
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <I, T> Stream<Pair<I, T>> indexedPairStream (@Nonnull final Iterable<T> iterable,
                                                               @Nonnull final IntFunction<I> indexTransformer)
      {
        return Pair.indexedPairStream(iterable, BASE_0, indexTransformer);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements returned by an iterable, made of {@link Pair}s
     * {@code (index, value)}. The index is rebased and transformed with specific functions.
     *
     * @param       <T>               the type of the elements
     * @param       <I>               the type of the transformed index
     * @param       iterable          the iterable
     * @param       rebaser           the rebaser of the index (BASE_0, BASE_1 or a similar function)
     * @param       indexTransformer  the transformer of the index
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <I, T> Stream<Pair<I, T>> indexedPairStream (@Nonnull final Iterable<T> iterable,
                                                               @Nonnull final IntUnaryOperator rebaser,
                                                               @Nonnull final IntFunction<I> indexTransformer)
      {
        return new Factory<>().stream(iterable, rebaser, indexTransformer);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements returned by a supplier, made of {@link Pair}s
     * {@code (index, value)}.
     *
     * @param       <T>               the type of the elements
     * @param       from              the first index (included)
     * @param       to                the last index (excluded)
     * @param       valueSupplier     the supplier of values
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnegative final int from,
                                                                  @Nonnegative final int to,
                                                                  @Nonnull final IntFunction<T> valueSupplier)
      {
        return indexedPairStream(from, to, valueSupplier, BASE_0, i -> i);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements returned by a supplier, made of {@link Pair}s
     * {@code (index, value)}.
     *
     * @param       <T>               the type of the elements
     * @param       from              the first index (included)
     * @param       to                the last index (excluded)
     * @param       valueSupplier     the supplier of values
     * @param       rebaser           the rebaser of the index (BASE_0, BASE_1 or a similar function)
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Stream<Pair<Integer, T>> indexedPairStream (@Nonnegative final int from,
                                                                  @Nonnegative final int to,
                                                                  @Nonnull final IntFunction<T> valueSupplier,
                                                                  @Nonnull final IntUnaryOperator rebaser)
      {
        return indexedPairStream(from, to, valueSupplier, rebaser, i -> i);
      }

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} out of the elements returned by a supplier, made of {@link Pair}s
     * {@code (index, value)}. The index can be rebased and transformed with specific functions.
     *
     * @param       <I>               the type of the transformed index
     * @param       <T>               the type of the elements
     * @param       from              the first index (included)
     * @param       to                the last index (excluded)
     * @param       valueSupplier     the supplier of values
     * @param       rebaser           the rebaser of the index (pass i -> i+1 to have a 1-based collection)
     * @param       indexTransformer  the transformer of the index
     * @return                        the stream
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, I> Stream<Pair<I, T>> indexedPairStream (@Nonnegative final int from,
                                                               @Nonnegative final int to,
                                                               @Nonnull final IntFunction<T> valueSupplier,
                                                               @Nonnull final IntUnaryOperator rebaser,
                                                               @Nonnull final IntFunction<I> indexTransformer)
      {
        return IntStream.range(from, to).mapToObj(i -> Pair.of(indexTransformer.apply(rebaser.applyAsInt(i)),
                                                               valueSupplier.apply(i)));
      }

    /*******************************************************************************************************************
     *
     * A {@link Collector} that produces a {@link Map} whose key is field {@code a} and value field {@code b}. Use
     * with {@link Stream#collect(Collector)}.
     *
     * @return    the {@code Collector}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <A, B> Collector<? super Pair<A, B>, ?, Map<A, B>> pairsToMap()
      {
        return Collectors.toMap(p -> p.a, p -> p.b);
      }

    @NotThreadSafe
    static final class Factory<I, T>
      {
        private final AtomicInteger n = new AtomicInteger(0);

        @Nonnull
        public <I, T> Stream<Pair<I, T>> stream (@Nonnull final Iterable<T> iterable,
                                                 @Nonnull final IntUnaryOperator rebaser,
                                                 @Nonnull final IntFunction<I> indexFunction)
          {
            return StreamSupport.stream(iterable.spliterator(), false)
                                .map(o -> Pair.of(indexFunction.apply(rebaser.applyAsInt(n.getAndIncrement())), o));
          }
      }
  }
