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

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;
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
public final class StreamUtils
  {
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
    public static <A, B, R> Stream<R> zip (@Nonnull final Stream<? extends A> streamA,
                                           @Nonnull final Stream<? extends B> streamB,
                                           @Nonnull final BiFunction<? super A, ? super B, ? extends R> zipper)
      {
        final var parallel = streamA.isParallel() || streamB.isParallel();
        final var sa = streamA.spliterator();
        final var sb = streamB.spliterator();
        final var characteristics =
                sa.characteristics() & sb.characteristics() & (Spliterator.SIZED | Spliterator.ORDERED);
        final var a = Spliterators.iterator(sa);
        final var b = Spliterators.iterator(sb);
        final var estSize = Math.min(sa.estimateSize(), sb.estimateSize());
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

    /*******************************************************************************************************************
     *
     * Returns a {@code Stream} of random {@link LocalDateTime}s, in the given range.
     *
     * @param   seed      the random seed
     * @param   from      the lower bound of the range (included)
     * @param   to        the upper bound of the range (excluded)
     * @return            the stream
     * @since   3.2-ALPHA-19
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Stream<LocalDateTime> randomLocalDateTimeStream (final long seed,
                                                                   @Nonnull final LocalDateTime from,
                                                                   @Nonnull final LocalDateTime to)
      {
        final var zo = ZoneOffset.UTC;
        return new Random(seed)
                .longs(from.toEpochSecond(zo), to.toEpochSecond(zo))
                .mapToObj(l -> LocalDateTime.ofInstant(Instant.ofEpochSecond(l), ZoneId.of(zo.getId())));
      }
  }
