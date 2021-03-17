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

import it.tidalwave.util.impl.DefaultTimeProvider;
import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/***********************************************************************************************************************
 *
 * A provider of current time. It should be used by code requiring a timestamp, so it can be mocked during tests.
 * {@code MockTimeProvider} in module "Test Utilities" is a suitable mock for performing tests.
 * 
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-1 (was previously InstantProvider since 1.39)
 *
 **********************************************************************************************************************/
public interface TimeProvider extends Supplier<Instant>
  {
    // FIXME: should be private
    public static AtomicReference<TimeProvider> __INSTANCE = new AtomicReference<>();

    /*******************************************************************************************************************
     *
     * Returns the current time.
     *
     * @return    the current time as an {@link Instant}
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public Instant currentInstant();

    /*******************************************************************************************************************
     *
     * Returns the current time. This method is provided to implement {@link Supplier}{@code <Instant>}.
     *
     * @return    the current time as an {@link Instant}
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    default public Instant get()
      {
        return currentInstant();
      }

    /*******************************************************************************************************************
     *
     * Returns the current time.
     *
     * @return    the current time as a {@link ZonedDateTime} in the default zone.
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    default public ZonedDateTime currentZonedDateTime()
      {
        return ZonedDateTime.ofInstant(currentInstant(), ZoneId.systemDefault());
      }

    /*******************************************************************************************************************
     *
     * Returns the current time.
     *
     * @return    the current time as a {@link LocalDateTime} in the default zone.
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    default public LocalDateTime currentLocalDateTime()
      {
        return LocalDateTime.ofInstant(currentInstant(), ZoneId.systemDefault());
      }

    /*******************************************************************************************************************
     *
     * Returns the default instance.
     *
     * @return    the default instance
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    static public TimeProvider getInstance()
      {
        synchronized (TimeProvider.class)
          {
            if (__INSTANCE.get() == null)
              {
                __INSTANCE.set(new DefaultTimeProvider());
              }

            return __INSTANCE.get();
          }
      }
  }
