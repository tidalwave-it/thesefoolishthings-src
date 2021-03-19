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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.As;

/***********************************************************************************************************************
 *
 * An extension to be used with Lombok in order to provide "as" support to classes that don't implement the {@link As}
 * interface. The typical usage is to retrofit legacy code.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AsExtensions
  {
    private static final AsExtensionsBean bean = new AsExtensionsBean();

    @Nonnull
    public static <T> T as (@Nonnull final Object datum,
                            @Nonnull final Class<T> roleType)
      {
        return as(datum, roleType, As.Defaults.throwAsException(roleType));
      }

    @Nonnull
    public static <T> T as (@Nonnull final Object datum,
                            @Nonnull final Class<T> roleType,
                            @Nonnull final As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        return bean.as(datum, roleType, notFoundBehaviour);
      }

    @Nonnull
    public static <T> Optional<T> maybeAs (@Nonnull final Object datum,
                                           @Nonnull final Class<T> type)
      {
        return Optional.ofNullable(as(datum, type, throwable -> null));
      }

    @Nonnull
    public static <T> Collection<T> asMany (@Nonnull final Object datum,
                                            @Nonnull final Class<T> type)
      {
        return (Collection<T>)bean.asMany(datum, type);
      }
  }
