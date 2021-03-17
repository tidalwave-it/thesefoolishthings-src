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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import it.tidalwave.util.impl.TypeSafeHashMap;

/***********************************************************************************************************************
 *
 * A map that is type safe, i.e the pairs (key, value) are type-checked. It's immutable.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
public interface TypeSafeMap extends Iterable<Object>
  {
    /*******************************************************************************************************************
     *
     * Returns a value given its key.
     *
     * @param   <T>   the type
     * @param   key   the key
     * @return        the value
     * @throws        NotFoundException if the key is not found
     * @deprecated    Use {@link #getOptional(Key)} instead
     *
     ******************************************************************************************************************/
    @Nonnull @Deprecated
    public <T> T get (@Nonnull Key<T> key)
      throws NotFoundException;

    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Optional<T> getOptional (@Nonnull Key<T> key)
      {
        try
          {
            return Optional.of(get(key));
          }
        catch (NotFoundException e)
          {
            return Optional.empty();
          }
      }

    /*******************************************************************************************************************
     *
     * Checks whether a pair has been stored.
     *
     * @param   key   the key
     * @return        {@code true} if the pair is present
     *
     ******************************************************************************************************************/
    public boolean containsKey (@Nonnull Key<?> key);

    /*******************************************************************************************************************
     *
     * Returns a set of all the contained keys.
     *
     * @return  the keys as a mutable set
     *
     ******************************************************************************************************************/
    @Nonnull
    public Set<Key<?>> getKeys();

    /*******************************************************************************************************************
     *
     * Returns the size of this map.
     *
     * @return    the size
     *
     ******************************************************************************************************************/
    @Nonnegative
    public int getSize();

    /*******************************************************************************************************************
     *
     * Returns the contents as a plain {@link Map}.
     *
     * @return    the contents as a mutable map
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Object> asMap();

    /*******************************************************************************************************************
     *
     * Creates a new empty instance.
     *
     * @return  the new instance
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static TypeSafeMap newInstance()
      {
        return new TypeSafeHashMap(Collections.emptyMap());
      }

    /*******************************************************************************************************************
     *
     * Creates an instance cloning the given map.
     *
     * @param   map   the map to clone
     * @return  the new instance
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static TypeSafeMap ofCloned (final @Nonnull Map<Key<?>, Object> map)
      {
        return new TypeSafeHashMap(map);
      }

    /*******************************************************************************************************************
     *
     * Create a new instance with an additional pair (key, value=
     *
     * @param   key   the key
     * @param   value the value
     * @return  the new instance
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> TypeSafeMap with (final @Nonnull Key<T> key, final @Nonnull T value);
  }
