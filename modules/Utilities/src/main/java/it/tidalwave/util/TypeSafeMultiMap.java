/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-util
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
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import it.tidalwave.util.impl.TypeSafeHashMultiMap;

/***********************************************************************************************************************
 *
 * A map that is type safe, i.e the pairs (key, value) are type-checked, and can contain multiple values, i.e. it's
 * associated to collections (key, collection of values). It's immutable.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface TypeSafeMultiMap extends Iterable<Map.Entry<Key<?>, Collection<?>>>
  {
    /*******************************************************************************************************************
     *
     * Returns a value given its key.
     *
     * @param   <T>   the type
     * @param   key   the key
     * @return        the value as a {@link Collection}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> get (@Nonnull Key<T> key);

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
     * @return        the keys as a mutable set
     *
     ******************************************************************************************************************/
    @Nonnull
    public Set<Key<?>> keySet();

    /*******************************************************************************************************************
     *
     * Returns a set of all the contained values.
     *
     * @return        the values as a mutable collection
     * @since         3.2-ALPHA-6
     *
     ******************************************************************************************************************/
    @Nonnull
    public Collection<Collection<?>> values();

    /*******************************************************************************************************************
     *
     * Returns a set of all the contained (key, value) pairs.
     *
     * @return        the pairs as a mutable collection
     * @since         3.2-ALPHA-6
     *
     ******************************************************************************************************************/
     @Nonnull
     public Set<Map.Entry<Key<?>, Collection<?>>> entrySet();

    /*******************************************************************************************************************
     *
     * Returns the size of this map.
     *
     * @return        the size
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
    public Map<Key<?>, Collection<?>> asMap();

    /*******************************************************************************************************************
     *
     * Creates a new instance with an additional pair (key, value).
     *
     * @param   <T>   the type
     * @param   key   the key
     * @param   value the value
     * @return        the new instance
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> TypeSafeMultiMap with (@Nonnull final Key<T> key, @Nonnull final T value);

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
    public static TypeSafeMultiMap ofCloned (@Nonnull final Map<? extends Key<?>, ? extends Collection<?>> map)
      {
        return new TypeSafeHashMultiMap(map);
      }

    /*******************************************************************************************************************
     *
     * Creates a new empty instance.
     *
     * @return        the new instance
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static TypeSafeMultiMap newInstance()
      {
        return new TypeSafeHashMultiMap(Collections.emptyMap());
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
