/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import it.tidalwave.role.Aggregate;
import lombok.ToString;

/***************************************************************************************************************************************************************
 *
 * A map-based implementation of {@link Aggregate}.
 * This is no more a public class; use {@link Aggregate#of(String, Object)} instead.
 * @stereotype Role
 *
 * @param <T>    the type of the aggregate
 * 
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Immutable @ToString
public class MapAggregate<T> implements Aggregate<T>
  {
    private final Map<String, T> mapByName;

    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    public MapAggregate()
      {
        this.mapByName = Collections.emptyMap();
      }

    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    public MapAggregate (@Nonnull final Map<String, T> mapByName)
      {
        this.mapByName = Map.copyOf(mapByName);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Optional<T> getByName (@Nonnull final String name)
      {
        return Optional.ofNullable(mapByName.get(name));
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Set<String> getNames()
      {
        return new CopyOnWriteArraySet<>(mapByName.keySet());
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public Aggregate<T> with (@Nonnull final String name, @Nonnull final T object)
      {
        final Map<String, T> clone = new HashMap<>(mapByName);
        clone.put(name, object);
        return new MapAggregate<>(clone);
      }
  }
