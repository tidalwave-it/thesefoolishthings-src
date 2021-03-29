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
package it.tidalwave.util.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.TypeSafeMap;
import lombok.EqualsAndHashCode;

/***********************************************************************************************************************
 *
 * An implementation of {@link TypeSafeMap}. This class is not part of the public API.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable @EqualsAndHashCode
public class TypeSafeHashMap implements TypeSafeMap, Serializable
  {
    private static final long serialVersionUID = 564564576856746L;

    @Nonnull
    private final Map<Key<?>, Object> map;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public TypeSafeHashMap (@Nonnull final Map<? extends Key<?>, Object> map)
      {
        this(new HashMap<>(), false);
        this.map.putAll(map);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMap (@Nonnull final Map<Key<?>, Object> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T get (@Nonnull final Key<T> key)
      throws NotFoundException
      {
        return NotFoundException.throwWhenNull(key.getType().cast(map.get(key)), "not found: %s", key);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public boolean containsKey (@Nonnull final Key<?> key)
      {
        return map.containsKey(key);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> TypeSafeMap with (@Nonnull final Key<T> key, @Nonnull final T value)
      {
        final Map<Key<?>, Object> map = asMap();
        map.put(key, value);
        return new TypeSafeHashMap(map, true);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Set<Key<?>> keySet()
      {
        return new CopyOnWriteArraySet<>(map.keySet());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Collection<Object> values()
      {
        return map.values();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Set<Map.Entry<Key<?>, Object>> entrySet()
      {
        return map.entrySet();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public int size()
      {
        return map.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Iterator<Map.Entry<Key<?>, Object>> iterator()
      {
        return map.entrySet().iterator();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Map<Key<?>, Object> asMap()
      {
        return new HashMap<>(map);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return map.toString();
      }
  }
