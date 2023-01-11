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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import it.tidalwave.util.impl.TypeSafeHashMap;

/***********************************************************************************************************************
 *
 * A map that is type safe, i.e. the pairs (key, value) are type-checked. It's immutable.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
public interface TypeSafeMap extends Iterable<Map.Entry<Key<?>, Object>>
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
     * Returns an optional value given its key.
     *
     * @param   <T>   the type
     * @param   key   the key
     * @return        the value
     *
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Optional<T> getOptional (@Nonnull final Key<T> key)
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
    public Set<Key<?>> keySet();

    /*******************************************************************************************************************
     *
     * Returns a set of all the contained values.
     *
     * @return  the values as a mutable collection
     * @since   3.2-ALPHA-6
     *
     ******************************************************************************************************************/
    @Nonnull
    public Collection<Object> values();

    /*******************************************************************************************************************
     *
     * Returns a set of all the contained (key, value) pairs.
     *
     * @return  the pairs as a mutable collection
     * @since   3.2-ALPHA-6
     *
     ******************************************************************************************************************/
    @Nonnull
    public Set<Map.Entry<Key<?>, Object>> entrySet();

    /*******************************************************************************************************************
     *
     * Returns the size of this map.
     *
     * @return    the size
     *
     ******************************************************************************************************************/
    @Nonnegative
    public int size();

    /*******************************************************************************************************************
     *
     * Returns the contents as a plain {@link Map}.
     *
     * @return        the contents as a mutable map
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Object> asMap();

    /*******************************************************************************************************************
     *
     * Performs the given action on all the pairs (key, value) contained in this map.
     *
     * @param action  the action
     * @since         3.2-ALPHA-10
     *
     ******************************************************************************************************************/
    public void forEach (@Nonnull BiConsumer<? super Key<?>, ? super Object> action);

    /*******************************************************************************************************************
     *
     * Create a new instance with an additional pair (key, value=
     *
     * @param   <T>   the type
     * @param   key   the key
     * @param   value the value
     * @return        the new instance
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> TypeSafeMap with (@Nonnull final Key<T> key, @Nonnull final T value);

    /*******************************************************************************************************************
     *
     * Creates a new empty instance.
     *
     * @return        the new instance
     * @since         3.2-ALPHA-2
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
     * @return        the new instance
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static TypeSafeMap ofCloned (@Nonnull final Map<Key<?>, Object> map)
      {
        return new TypeSafeHashMap(map);
      }

    /** @deprecated Use {@link #keySet()} instead.
     * @return - */
    @Nonnull @Deprecated
    public default Set<Key<?>> getKeys()
      {
        return keySet();
      }

    /** @deprecated Use {@link #size()} instead.
     * @return - */
    @Deprecated
    public default int getSize()
      {
        return size();
      }
  }
