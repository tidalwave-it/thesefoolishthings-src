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

import it.tidalwave.util.Key;
import it.tidalwave.util.TypeSafeMultiMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
public class TypeSafeHashMultiMap implements TypeSafeMultiMap, Serializable
  {
    private final static long serialVersionUID = 759233572056L;

    @Nonnull
    private final Map<Key<?>, Collection<?>> map;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public TypeSafeHashMultiMap (final @Nonnull Map<Key<?>, Collection<?>> map)
      {
        this(new HashMap<Key<?>, Collection<? extends Object>>(), true);
        this.map.putAll(map);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMultiMap (final @Nonnull Map<Key<?>, Collection<?>> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull @SuppressWarnings("unchecked")
    public <T> Collection<T> get (final @Nonnull Key<T> key)
      {
        return containsKey(key) ? new CopyOnWriteArrayList<>((Collection<T>)map.get(key))
                                : new ArrayList<>();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public boolean containsKey (final @Nonnull Key<?> key)
      {
        return map.containsKey(key);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> TypeSafeHashMultiMap with (final @Nonnull Key<T> key, final @Nonnull T value)
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
    @Nonnull
    public Set<Key<?>> getKeys()
      {
        return new CopyOnWriteArraySet<>(map.keySet());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnegative
    public int getSize()
      {
        return map.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Iterator<Collection<?>> iterator()
      {
        return Collections.unmodifiableCollection(map.values()).iterator();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Collection<?>> asMap()
      {
        return new HashMap<Key<?>, Collection<?>>(map);
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