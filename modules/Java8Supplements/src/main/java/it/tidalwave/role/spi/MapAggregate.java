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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.role.Aggregate;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A map-based implementation of {@link Aggregate}.
 *
 * @stereotype Role
 *
 * @param <TYPE>    the type of the aggregate
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @ToString
public class MapAggregate<TYPE> implements Aggregate<TYPE>
  {
    private final Map<String, TYPE> mapByName;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public MapAggregate (final @Nonnull Map<String, TYPE> mapByName)
      {
        this.mapByName = Collections.unmodifiableMap(new HashMap<>(mapByName));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Optional<TYPE> getByName (final @Nonnull String name)
      {
        return Optional.ofNullable(mapByName.get(name));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Collection<String> getNames()
      {
        return new CopyOnWriteArrayList<>(mapByName.keySet());
      }
  }
