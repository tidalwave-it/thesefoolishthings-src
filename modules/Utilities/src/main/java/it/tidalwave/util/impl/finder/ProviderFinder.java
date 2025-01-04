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
package it.tidalwave.util.impl.finder;

import javax.annotation.concurrent.Immutable;
import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;

/***************************************************************************************************************************************************************
 *
 * A {@link Finder} which retrieve results from a {@link Supplier}.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Immutable
public class ProviderFinder<T> extends SimpleFinderSupport<T>
  {
    private static final long serialVersionUID = 1344191036948400804L;

    @Nonnull
    private final BiFunction<Integer, Integer, ? extends Collection<? extends T>> supplier;

    @SuppressWarnings("BoundedWildcard")
    public ProviderFinder (@Nonnull final BiFunction<Integer, Integer, ? extends Collection<? extends T>> supplier)
      {
        this.supplier = supplier;
      }

    public ProviderFinder (@Nonnull final ProviderFinder<T> other, @Nonnull final Object override)
      {
        super(other, override);
        final ProviderFinder<T> source = getSource(ProviderFinder.class, other, override);
        this.supplier = source.supplier;
      }

    @Override @Nonnull
    protected List<T> computeNeededResults()
      {
        return new CopyOnWriteArrayList<>(supplier.apply(firstResult, maxResults));
      }
  }