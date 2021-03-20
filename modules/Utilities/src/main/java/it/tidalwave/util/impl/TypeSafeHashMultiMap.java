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
package it.tidalwave.util.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.io.Serializable;
import it.tidalwave.util.Key;
import it.tidalwave.util.TypeSafeMultiMap;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
public class TypeSafeHashMultiMap implements TypeSafeMultiMap, Serializable
  {
    private static final long serialVersionUID = 759233572056L;

    @Nonnull
    private final Map<Key<?>, Collection<?>> map;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public TypeSafeHashMultiMap (@Nonnull final Map<Key<?>, Collection<?>> map)
      {
        this(new HashMap<>(), true);
        this.map.putAll(map);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMultiMap (@Nonnull final Map<Key<?>, Collection<?>> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull @SuppressWarnings("unchecked")
    public <T> Collection<T> get (@Nonnull final Key<T> key)
      {
        return containsKey(key) ? new CopyOnWriteArrayList<>((Collection<T>)map.get(key))
                                : new ArrayList<>();
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
    public <T> TypeSafeHashMultiMap with (@Nonnull final Key<T> key, @Nonnull final T value)
      {
        final Map<Key<?>, Collection<?>> map = asMap();
        final Collection<T> values = get(key);
        values.add(value);
        map.put(key, values);
        return new TypeSafeHashMultiMap(map);
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
    public Collection<Collection<?>> values()
      {
        return map.values();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Set<Map.Entry<Key<?>, Collection<?>>> entrySet()
      {
        return map.entrySet();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Iterator<Map.Entry<Key<?>, Collection<?>>> iterator()
      {
        return map.entrySet().iterator();
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
    public Map<Key<?>, Collection<?>> asMap()
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
