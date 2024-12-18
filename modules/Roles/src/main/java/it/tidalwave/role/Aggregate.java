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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import it.tidalwave.role.impl.MapAggregate;

/***********************************************************************************************************************
 *
 * The role of an aggregate object, that is an object which contains other named objects.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Aggregate<T>
  {
    public static final Class<Aggregate> _Aggregate_ = Aggregate.class;

    /*******************************************************************************************************************
     *
     * Returns an object given its name.
     *
     * @param   name      the name
     * @return  the object
     *
     ******************************************************************************************************************/
    @Nonnull
    public Optional<T> getByName (@Nonnull String name);

    /*******************************************************************************************************************
     *
     * Returns the names of contained objects.
     *
     * @return  the names of the objects
     * @since 3.1-ALPHA-8
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Set<String> getNames()
      {
        return Collections.emptySet();
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pairs.
     *
     * @param   <T>         the static type of the value
     * @param   mapByName   the map containing the pairs
     * @return              the new instance
     * @since               3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Aggregate<T> of (@Nonnull final Map<String, T> mapByName)
      {
        return new MapAggregate<>(mapByName);
      }

    /*******************************************************************************************************************
     *
     * Returns a new empty instance that will be populated by means of {@link #with(String, Object)}.
     *
     * @param   <T>         the static type of the aggregate
     * @return              the new instance
     * @since               3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Aggregate<T> newInstance()
      {
        return new MapAggregate<>();
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pair.
     *
     * @param   <T>         the static type of the aggregate
     * @param   name        the name in the pair
     * @param   value       the value in the pair
     * @return              the new instance
     * @since               3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Aggregate<T> of (@Nonnull final String name, @Nonnull final T value)
      {
        return new MapAggregate<T>().with(name, value);
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pair.
     *
     * @param   name        the name in the pair
     * @param   value       the value in the pair
     * @return              the new instance
     * @since               3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Aggregate<T> with (@Nonnull final String name, @Nonnull final T value)
      {
        return new MapAggregate<T>().with(name, value);
      }
  }
