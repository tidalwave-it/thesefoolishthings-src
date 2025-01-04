/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/***************************************************************************************************************************************************************
 *
 * A specialisation of {@link ConcurrentHashMap} with {@link Optional} support.
 * 
 * @param <K>   the type of the key
 * @param <V>   the type of the value
 *
 * @since   3.1-ALPHA-2
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class ConcurrentHashMapWithOptionals<K, V> extends ConcurrentHashMap<K, V>
  {
    private static final long serialVersionUID = -1771492882183193296L;

    /***********************************************************************************************************************************************************
     * If the map doesn't contain the given key, put the new pair(key, value), and return the key itself. Otherwise,
     * do nothing and return an empty {@link Optional}. The map manipulation is atomically performed by calling
     * {@link #putIfAbsent(java.lang.Object, java.lang.Object)}.
     *
     * If {@code optionalKey} is empty, do nothing and return nothing.
     *
     * @param   optionalKey     the key
     * @param   value           the value
     * @return                  the new key, or nothing
     **********************************************************************************************************************************************************/
    @Nonnull
    public Optional<K> putIfAbsentAndGetNewKey (@Nonnull final Optional<? extends K> optionalKey, @Nonnull final V value)
      {
        return optionalKey.flatMap(key -> putIfAbsentAndGetNewKey(key, value));
      }

    /***********************************************************************************************************************************************************
     * If the map doesn't contain the given key, put the new pair(key, value), and return the key itself. Otherwise,
     * do nothing and return an empty {@link Optional}. The map manipulation is atomically performed by calling
     * {@link #putIfAbsent(java.lang.Object, java.lang.Object)}.
     *
     * @param   key     the key
     * @param   value   the value
     * @return          the new key, or nothing
     **********************************************************************************************************************************************************/
    @Nonnull
    public Optional<K> putIfAbsentAndGetNewKey (@Nonnull final K key, @Nonnull final V value)
      {
        return (putIfAbsent(key, value) == null) ? Optional.of(key) : Optional.empty();
      }
  }