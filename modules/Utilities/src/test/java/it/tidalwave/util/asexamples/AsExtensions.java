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
package it.tidalwave.util.asexamples;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.AsException;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AsExtensions
  {
    @Nonnull
    public static <T> T as (@Nonnull final Object datum, @Nonnull final Class<? extends T> type)
      {
        return maybeAs(datum, type).orElseThrow(() -> new AsException(type));
      }

    @Nonnull
    public static <T> Optional<T> maybeAs (@Nonnull final Object datum, @Nonnull final Class<? extends T> type)
      {
        try
          {
            final var datumClass = datum.getClass();
            final var roleClassName = "it.tidalwave.util.asexamples."
                                      + datumClass.getSimpleName() + type.getSimpleName() + "Role";
            return Optional.of(type.cast(Class.forName(roleClassName).getConstructor(datumClass).newInstance(datum)));
          }
        catch (Exception e)
          {
            return Optional.empty();
          }
      }

    @Nonnull
    public static <T> Collection<T> asMany (@Nonnull final Object datum,
                                            @Nonnull final Class<? extends T> roleClass)
      {
        throw new UnsupportedOperationException();
      }
  }
