/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import it.tidalwave.util.Id;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * An implementation of {@link FinderWithIdSupport} based on a {@link Map}.
 *
 * @param <T>     the product abstract type
 * @param <I>     the product concrete type
 * @param <F>     the {@code Finder} type
 * @since         3.2-ALPHA-15
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.experimental
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class FinderWithIdMapSupport<T, I extends T, F extends ExtendedFinderSupport<T, F>>
        extends FinderWithIdSupport<T, I, F>
  {
    private static final long serialVersionUID = 1L;

    @Nonnull
    private final Map<Id, I> mapById;

    public FinderWithIdMapSupport()
      {
        mapById = Collections.emptyMap();
      }

    public FinderWithIdMapSupport (@Nonnull final FinderWithIdMapSupport<T, I, F> other,
                                   @Nonnull final Object override)
      {
        super(other, override);
        final FinderWithIdMapSupport<T, I, F> source = getSource(FinderWithIdMapSupport.class, other, override);
        mapById = new HashMap<>(source.mapById);
      }

    @Override @Nonnull
    protected List<T> findAll()
      {
        return new ArrayList<>(mapById.values());
      }

    @Override @Nonnull
    protected Optional<T> findById (@Nonnull final Id id)
      {
        return Optional.ofNullable(mapById.get(id));
      }
  }

