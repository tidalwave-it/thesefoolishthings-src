/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - Utilities
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Immutable
public class TypeSafeHashMap implements TypeSafeMap, Serializable
  {
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
     * For testing only.
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMap (final @Nonnull Map<Key<?>, Object> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
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
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    public boolean containsKey (final @Nonnull Key<?> key)
      {
        return map.containsKey(key);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Set<Key<?>> getKeys()
      {
        return Collections.unmodifiableSet(map.keySet());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnegative
    public int getSize()
      {
        return map.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Iterator<Object> iterator()
      {
        return Collections.unmodifiableCollection(map.values()).iterator();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Object> asMap()
      {
        return new HashMap<Key<?>, Object>(map);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return map.toString();
      }
  }
