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

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class MultiMap<K, V> extends HashMap<K, Set<V>>
  {
    private static final long serialVersionUID = 8834342771135005212L;

    public synchronized void add (@Nonnull final K key, @Nonnull final V value)
      {
        internalGetValues(key).add(value);
      }

    public synchronized void addAll (@Nonnull final K key, @Nonnull final Collection<? extends V> values)
      {
        if (!values.isEmpty())
          {
            internalGetValues(key).addAll(values);
          }
      }

    @Nonnull
    public synchronized Set<V> getValues (@Nonnull final K key)
      {
        final var values = get(key);
        return (values == null) ? Collections.emptySet() : Collections.unmodifiableSet(values);
      }

    @Nonnull
    private Set<V> internalGetValues (@Nonnull final K key)
      {
        var values = get(key);

        if (values == null)
          {
            values = new HashSet<>();
            put(key, values);
          }

        return values;
      }
  }
