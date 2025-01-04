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
package it.tidalwave.util.spi;

import jakarta.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Id;
import lombok.RequiredArgsConstructor;
import static java.util.Collections.emptyList;

/***************************************************************************************************************************************************************
 *
 * A support class for implementing a {@link Finder} that provides filtering by id.
 *
 * @param <T>     the product abstract type
 * @param <I>     the product concrete type
 * @param <F>     the {@code Finder} type
 * @since         3.2-ALPHA-15
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.experimental
 *
 **************************************************************************************************************************************************************/
@RequiredArgsConstructor
public class FinderWithIdSupport<T, I extends T, F extends ExtendedFinderSupport<T, F>>
        extends HierarchicFinderSupport<T, F> implements FinderWithId<T, F>
  {
    private static final long serialVersionUID = 2L;

    @Nonnull
    /* package */ final Optional<Id> id;

    public FinderWithIdSupport()
      {
        id = Optional.empty();
      }

    public FinderWithIdSupport (@Nonnull final FinderWithIdSupport<T, I, F> other, @Nonnull final Object override)
      {
        super(other, override);
        final FinderWithIdSupport<T, I, F> source = getSource(FinderWithIdSupport.class, other, override);
        this.id = source.id;
      }

    @Override @Nonnull
    public F withId (@Nonnull final Id id)
      {
        return clonedWith(new FinderWithIdSupport<>(Optional.of(id)));
      }

    @Override @Nonnull
    protected List<T> computeResults()
      {
        return id.map(id -> findById(id).map(Collections::singletonList).orElse(emptyList())).orElse(findAll());
      }

    @Nonnull
    protected Optional<T> findById (@Nonnull final Id id)
      {
        throw new UnsupportedOperationException("Must be overridden");
      }

    @Nonnull
    protected List<T> findAll()
      {
        throw new UnsupportedOperationException("Must be overridden");
      }

    @Nonnull
    protected Stream<I> streamImpl()
      {
        return (Stream<I>)stream();
      }
  }

