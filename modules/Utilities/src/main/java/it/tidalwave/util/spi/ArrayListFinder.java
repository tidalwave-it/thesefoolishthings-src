/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***********************************************************************************************************************
 *
 * An implementation of {@link Finder} which holds an immutable list of items.
 *
 * @param  <T>   the type of contained items
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class ArrayListFinder<T> extends SimpleFinderSupport<T>
  {
    private static final long serialVersionUID = -3529114277448372453L;

    @Nonnull
    private final Collection<T> items;

    public ArrayListFinder (final @Nonnull Collection<T> items)
      {
        this.items = Collections.unmodifiableCollection(new ArrayList<>(items));
      }

    public ArrayListFinder (final @Nonnull ArrayListFinder<T> other, @Nonnull Object override)
      {
        super(other, override);
        final ArrayListFinder<T> source = getSource(ArrayListFinder.class, other, override);
        this.items = source.items;
      }

    @Override @Nonnull
    protected List<? extends T> computeResults()
      {
        return new CopyOnWriteArrayList<>(items);
      }
  }
