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
import java.util.List;
import java.util.function.Function;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;
import static java.util.stream.Collectors.*;

/***************************************************************************************************************************************************************
 *
 * A {@link Finder} which retrieve results from another instance applying a {@link Function}.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Immutable
public final class MappingFinder<T, U> extends SimpleFinderSupport<T>
  {
    private static final long serialVersionUID = -6359683808082070089L;

    @Nonnull
    private final Finder<? extends U> delegate;

    @Nonnull
    private final Function<? super U, ? extends T> decorator;

    public MappingFinder (@Nonnull final Finder<? extends U> delegate,
                          @Nonnull final Function<? super U, ? extends T> decorator)
      {
        this.delegate = delegate;
        this.decorator = decorator;
      }

    public MappingFinder (@Nonnull final MappingFinder<T, ?> other, @Nonnull final Object override)
      {
        super(other, override);
        final MappingFinder<T, U> source = getSource(MappingFinder.class, other, override);
        this.delegate = source.delegate;
        this.decorator = source.decorator;
      }

    @Override @Nonnull
    protected List<T> computeResults()
      {
        return delegate.results().stream().map(decorator).collect(toList());
      }
  }
