/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Collections;
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
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public class DefaultFilterSortCriterion<Type> implements Finder.FilterSortCriterion<Type>, Serializable 
  {
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
    public DefaultFilterSortCriterion (final @Nonnull Comparator<? super Type> comparator, final @Nonnull String name) 
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
    protected DefaultFilterSortCriterion (final @Nonnull Comparator<? super Type> comparator) 
      {
        this.comparator = comparator;
        this.name = getClass().getSimpleName();
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public void sort (final @Nonnull List<? extends Type> results,
                      final @Nonnull SortDirection sortDirection)
      {
        Collections.sort(results, new Comparator<Type>()
          {
            public int compare (final @Nonnull Type o1, final @Nonnull Type o2) 
              {
                return comparator.compare(o1, o2) * sortDirection.intValue();
              }
          });
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
