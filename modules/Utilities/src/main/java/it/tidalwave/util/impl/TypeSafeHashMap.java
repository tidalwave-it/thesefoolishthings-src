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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.TypeSafeMap;

/***********************************************************************************************************************
 *
 * An implementation of {@link TypeSafeMap}. This class is not part of the public API.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
public class TypeSafeHashMap implements TypeSafeMap, Serializable
  {
    private final static long serialVersionUID = 564564576856746L;

    @Nonnull
    private final Map<Key<?>, Object> map;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public TypeSafeHashMap (final @Nonnull Map<Key<?>, Object> map)
      {
        this(new HashMap<Key<?>, Object>(), false);
        this.map.putAll(map);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMap (final @Nonnull Map<Key<?>, Object> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T get (final @Nonnull Key<T> key)
      throws NotFoundException
      {
        return NotFoundException.throwWhenNull((T)map.get(key), "not found: %s", key);
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
    public <T> TypeSafeMap with (final @Nonnull Key<T> key, final @Nonnull T value)
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
    public Iterator<Object> iterator()
      {
        return new CopyOnWriteArraySet<>(map.values()).iterator();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Object> asMap()
      {
        return new HashMap<Key<?>, Object>(map);
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