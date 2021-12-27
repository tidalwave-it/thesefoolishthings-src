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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.As;
import it.tidalwave.util.spi.AsSupport;

/***********************************************************************************************************************
 *
 * An extension to be used with Lombok in order to provide "as" support to classes that don't implement the {@link As}
 * interface. The typical usage is to retrofit legacy code.
 *
 * FIXME: this class doesn't cache - every as*() call instantiates new objects.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AsExtensions
  {
    @Nonnull
    public static <T> T as (@Nonnull final Object datum, @Nonnull final Class<T> roleType)
      {
        return asSupport(datum).as(roleType);
      }

    @Nonnull
    public static <T> T as (@Nonnull final Object datum,
                            @Nonnull final Class<T> roleType,
                            @Nonnull final As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        return asSupport(datum).as(roleType, notFoundBehaviour);
      }

    @Nonnull
    public static <T> Optional<T> maybeAs (@Nonnull final Object datum, @Nonnull final Class<T> type)
      {
        return asSupport(datum).maybeAs(type);
      }

    @Nonnull
    public static <T> Collection<T> asMany (@Nonnull final Object datum, @Nonnull final Class<T> type)
      {
        return asSupport(datum).asMany(type);
      }

    @Nonnull
    private static AsSupport asSupport (@Nonnull final Object datum)
      {
        return new AsSupport(datum);
      }
  }
