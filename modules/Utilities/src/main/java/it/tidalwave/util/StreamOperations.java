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

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * A collection of operations on {@link Stream}s.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-12
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamOperations
  {
    /*******************************************************************************************************************
     *
     * Zips two streams into a stream of {@link Pair}s.
     *
     * @param   streamA     the first {@link Stream}
     * @param   streamB     the second {@link Stream}
     * @param   <A>         the type of elements of the first {@link Stream}
     * @param   <B>         the type of elements of the second {@link Stream}
     * @return              the zipped {@link Stream}
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <A, B> Stream<Pair<A, B>> zip (@Nonnull final Stream<? extends A> streamA,
                                                 @Nonnull final Stream<? extends B> streamB)
      {
        return zip(streamA, streamB, Pair::of);
      }

    /*******************************************************************************************************************
     *
     * Zips two streams.
     *
     * @param   streamA     the first {@link Stream}
     * @param   streamB     the second {@link Stream}
     * @param   zipper      the zipping function
     * @param   <A>         the type of elements of the first {@link Stream}
     * @param   <B>         the type of elements of the second {@link Stream}
     * @param   <R>         the type of elements of the zipped {@link Stream}
     * @return              the zipped {@link Stream}
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <A, B, R> Stream<R> zip (@Nonnull final Stream<A> streamA,
                                           @Nonnull final Stream<B> streamB,
                                           @Nonnull final BiFunction<? super A, ? super B, ? extends R> zipper)
      {
        final boolean parallel = streamA.isParallel() || streamB.isParallel();
        final Spliterator<A> sa = streamA.spliterator();
        final Spliterator<B> sb = streamB.spliterator();
        final int characteristics =
                sa.characteristics() & sb.characteristics() & (Spliterator.SIZED | Spliterator.ORDERED);
        final Iterator<A> a = Spliterators.iterator(sa);
        final Iterator<B> b = Spliterators.iterator(sb);
        final long estSize = Math.min(sa.estimateSize(), sb.estimateSize());
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(estSize, characteristics)
            {
              @Override
              public boolean tryAdvance (@Nonnull final Consumer<? super R> action)
                {
                  if (a.hasNext() && b.hasNext())
                    {
                      action.accept(zipper.apply(a.next(), b.next()));
                      return true;
                    }

                  return false;
                }
            },
          parallel)
          .onClose(streamA::close)
          .onClose(streamB::close);
      }
  }