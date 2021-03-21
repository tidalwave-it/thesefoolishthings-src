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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

/***********************************************************************************************************************
 *
 * Objects implementing this interface can provide am adapter of the required type. The adapter can be found with a
 * variety of approaches that depend on the implementation. This capability can be used to implement a design based
 * on the Data, Context and Interaction pattern (DCI).
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface As
  {
    /*******************************************************************************************************************
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    public static interface NotFoundBehaviour<T>
      {
        @Nonnull
        public T run (@Nullable final Throwable t);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static final class Defaults
      {
        private Defaults()
          {
          }

        public static <X> NotFoundBehaviour<X> throwAsException (@Nonnull final Class<? extends X> clazz)
          {
            return new NotFoundBehaviour<X>()
              {
//                @Override
                @Nonnull
                public X run (@Nonnull final Throwable t)
                  {
                    throw new AsException(clazz, t);
                  }
              };
          }
      }

    /*******************************************************************************************************************
     *
     * Returns an adapter to this object of the specified type. If the implementation can find multiple compliant
     * adapters, only one will be returned.
     *
     * @param   type    the type
     * @return          the adapter
     * @throws          AsException if no adapter is found
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull Class<? extends T> type);

    /*******************************************************************************************************************
     *
     * Returns an adapter to this object of the specified type. If the implementation can find multiple compliant
     * adapters, only one will be returned. If no adapter is found, the result provided by the given default
     * behaviour will be returned.
     *
     * @param   type                the type
     * @param   notFoundBehaviour   the behaviour to apply when an adapter is not found
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull Class<? extends T> type, @Nonnull NotFoundBehaviour<? extends T> notFoundBehaviour);

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>     the role type
     * @param   type    the role type
     * @return          the optional role
     * @since           3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    default <T> Optional<T> maybeAs (@Nonnull final Class<? extends T> type)
      {
        return Optional.ofNullable(as(type, throwable -> null));
      }

    /*******************************************************************************************************************
     *
     * @deprecated  Use {@link #maybeAs(Class)}.
     *
     ******************************************************************************************************************/
    @Nonnull @Deprecated
    default <T> Optional<T> asOptional (@Nonnull final Class<? extends T> type)
      {
        return maybeAs(type);
      }

    /*******************************************************************************************************************
     *
     * Searches for multiple adapters of the given type and returns them.
     *
     * @param  type     the adapter type
     * @return          a collection of adapters, possibly empty
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> asMany (@Nonnull Class<? extends T> type);
  }
