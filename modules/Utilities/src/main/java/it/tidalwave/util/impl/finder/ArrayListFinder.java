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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.util.spi.SimpleFinderSupport;

/***************************************************************************************************************************************************************
 *
 * An implementation of {@link it.tidalwave.util.Finder} which holds an immutable list of items.
 *
 * This class is now implementation only; please use {@link it.tidalwave.util.Finder#ofCloned} instead.
 *
 * @param  <T>   the type of contained items
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class ArrayListFinder<T> extends SimpleFinderSupport<T>
  {
    private static final long serialVersionUID = -3529114277448372453L;

    @Nonnull
    private final Collection<T> items;

    public ArrayListFinder (@Nonnull final Collection<? extends T> items)
      {
        this.items = Collections.unmodifiableCollection(new ArrayList<>(items));
      }

    public ArrayListFinder (@Nonnull final ArrayListFinder<T> other, @Nonnull final Object override)
      {
        super(other, override);
        final var source = getSource(ArrayListFinder.class, other, override);
        this.items = source.items;
      }

    @Override @Nonnull
    protected List<T> computeResults()
      {
        return new CopyOnWriteArrayList<>(items);
      }
  }
