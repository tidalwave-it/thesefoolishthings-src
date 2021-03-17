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

import it.tidalwave.role.impl.MapAggregate;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
public interface Aggregate<TYPE>
  {
    public static final Class<Aggregate> Aggregate = Aggregate.class;

    /*******************************************************************************************************************
     *
     * Returns an object given its name.
     *
     * @return  the optional object
     *
     ******************************************************************************************************************/
    @Nonnull
    public Optional<TYPE> getByName (@Nonnull String name);

    /*******************************************************************************************************************
     *
     * Returns the names of contained objects.
     *
     * @return  the names of the objects
     * @since 3.1-ALPHA-8
     *
     ******************************************************************************************************************/
    @Nonnull
    default public Collection<String> getNames()
      {
        return Collections.emptyList();
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pairs.
     *
     * @param   mapByName    the map containing the pairs
     * @return  the new instance
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <TYPE> Aggregate<TYPE> of (final @Nonnull Map<String, TYPE> mapByName)
      {
        return new MapAggregate<TYPE>(mapByName);
      }

    /*******************************************************************************************************************
     *
     * Returns a new empty instance that will be populated by means of {@link #with(String, Object)}.
     *
     * @return  the new instance
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <TYPE> Aggregate<TYPE> newInstance()
      {
        return new MapAggregate<TYPE>();
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pair.
     *
     * @param   name    the name in the pair
     * @param   value   the value in the pair
     * @return  the new instance
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <TYPE> Aggregate<TYPE> of (final @Nonnull String name, final @Nonnull TYPE value)
      {
        return new MapAggregate<TYPE>().with(name, value);
      }

    /*******************************************************************************************************************
     *
     * Returns a new instance with the specified (name, value) pair.
     *
     * @param   name    the name in the pair
     * @param   value   the value in the pair
     * @return  the new instance
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    default public Aggregate<TYPE> with (final @Nonnull String name, final @Nonnull TYPE value)
      {
        return new MapAggregate<TYPE>().with(name, value);
      }
  }
