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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;
import it.tidalwave.util.Finder.SortDirection;

/***********************************************************************************************************************
 *
 * A default implementation of {@link Finder.FilterSortCriterion} which relies on sorting capabilities of the Java
 * runtime library and only needs a {@link Comparator} to be specified.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public class DefaultFilterSortCriterion<Type> implements Finder.FilterSortCriterion<Type>, Serializable
  {
    private static final long serialVersionUID = 76093596048395982L;

    @Nonnull
    private final Comparator<? super Type> comparator;

    @Nonnull
    private final String name;

    /*******************************************************************************************************************
     *
     * Creates an instance that will use the given {@link Comparator}, with the given name (used for diagnostics).
     *
     * @param  comparator   the comparator
     * @param  name         a name used for diagnostics
     *
     ******************************************************************************************************************/
    public DefaultFilterSortCriterion (@Nonnull final Comparator<? super Type> comparator, @Nonnull final String name)
      {
        this.comparator = comparator;
        this.name = name;
      }

    /*******************************************************************************************************************
     *
     * Creates an instance that will use the given {@link Comparator}.
     *
     * @param  comparator   the comparator
     *
     ******************************************************************************************************************/
    protected DefaultFilterSortCriterion (@Nonnull final Comparator<? super Type> comparator)
      {
        this.comparator = comparator;
        this.name = getClass().getSimpleName();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void sort (@Nonnull final List<? extends Type> results,
                      @Nonnull final SortDirection sortDirection)
      {
        results.sort((Comparator<Type>)(o1, o2) -> comparator.compare(o1, o2) * sortDirection.intValue());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return name;
      }
  }
