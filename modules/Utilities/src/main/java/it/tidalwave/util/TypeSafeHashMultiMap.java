/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Immutable
public class TypeSafeHashMultiMap implements TypeSafeMultiMap
  {
    @Nonnull
    private final Map<Key<?>, Collection<? extends Object>> map;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public TypeSafeHashMultiMap (final @Nonnull Map<Key<?>, Collection<? extends Object>> map)
      {
        this(new HashMap<Key<?>, Collection<? extends Object>>(), true);
        this.map.putAll(map);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    /* package */ TypeSafeHashMultiMap (final @Nonnull Map<Key<?>, Collection<? extends Object>> map, final boolean dummy)
      {
        this.map = map;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> get (final @Nonnull Key<T> key)
      {
        return containsKey(key) ? Collections.unmodifiableCollection((Collection<T>)map.get(key)) :
                                  Collections.<T>emptyList();
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
    public Iterator <Collection<? extends Object>> iterator()
      {
        return Collections.unmodifiableCollection(map.values()).iterator();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDocs}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Map<Key<?>, Collection<? extends Object>> asMap()
      {
        return new HashMap<Key<?>, Collection<? extends Object>>(map);
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
