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
package it.tidalwave.util.impl;

// import javax.annotation.Nonnegative;
// import javax.annotation.concurrent.Immutable;
import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.io.Serializable;
import it.tidalwave.util.Key;
import it.tidalwave.util.TypeSafeMultiMap;
import lombok.EqualsAndHashCode;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
/* @Immutable */ @EqualsAndHashCode
public class TypeSafeHashMultiMap implements TypeSafeMultiMap, Serializable
  {
    private static final long serialVersionUID = 759233572056L;

    @Nonnull
    private final Map<Key<?>, Collection<?>> map;

    /***********************************************************************************************************************************************************
     * Creates a new instance from the given contents.
     *
     * @param   map   the contents
     **********************************************************************************************************************************************************/
    public TypeSafeHashMultiMap (@Nonnull final Map<? extends Key<?>, ? extends Collection<?>> map)
      {
        this(new HashMap<>(), true);
        this.map.putAll(map);
      }

    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    /* package */ TypeSafeHashMultiMap (@Nonnull final Map<Key<?>, Collection<?>> map, final boolean ignored)
      {
        this.map = map;
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull @SuppressWarnings("unchecked")
    public <T> Collection<T> get (@Nonnull final Key<T> key)
      {
        return containsKey(key) ? new CopyOnWriteArrayList<>((Collection<T>)map.get(key))
                                : new ArrayList<>();
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override
    public boolean containsKey (@Nonnull final Key<?> key)
      {
        return map.containsKey(key);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public <T> TypeSafeHashMultiMap with (@Nonnull final Key<T> key, @Nonnull final T value)
      {
        final var map = asMap();
        final var values = get(key);
        values.add(value);
        map.put(key, values);
        return new TypeSafeHashMultiMap(map);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Set<Key<?>> keySet()
      {
        return new CopyOnWriteArraySet<>(map.keySet());
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Collection<Collection<?>> values()
      {
        return map.values();
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Set<Map.Entry<Key<?>, Collection<?>>> entrySet()
      {
        return map.entrySet();
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Iterator<Map.Entry<Key<?>, Collection<?>>> iterator()
      {
        return map.entrySet().iterator();
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override /* @Nonnegative */
    public int size()
      {
        return map.size();
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Map<Key<?>, Collection<?>> asMap()
      {
        return new HashMap<>(map);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @SuppressWarnings("unchecked")
    public <T> void forEach (@Nonnull final BiConsumer<? super Key<T>, ? super Collection<T>> action)
      {
        map.forEach((BiConsumer<? super Key<?>, ? super Collection<?>>)action);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return map.toString();
      }
  }
