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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;

/***********************************************************************************************************************
 * 
 * The role of an object that has contents that can be sorted.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface Sortable 
  {
    //@bluebook-begin others
    public final static Class<Sortable> Sortable = Sortable.class; 
    
    /*******************************************************************************************************************
     *
     * A default {@code Sortable} which does nothing (useful for implementing the NullObject pattern). This object 
     * always returns {@link Finder.SortCriterion.UNSORTED} as {@code sortCriterion} and 
     * {@link SortDirection.ASCENDING} as {@code sortDirection}.
     * 
     ******************************************************************************************************************/
    public final static Sortable DEFAULT = new Sortable()
      {
        @Override
        public void setSortCriterion (final @Nonnull SortCriterion sortCriterion)
          {
          }

        @Override @Nonnull 
        public SortCriterion getSortCriterion() 
          {
            return Finder.SortCriterion.UNSORTED;
          }

        @Override
        public void setSortDirection (final @Nonnull SortDirection sortDirection) 
          {
          }

        @Override @Nonnull 
        public SortDirection getSortDirection() 
          {
            return SortDirection.ASCENDING;
          }
      };
    
    //@bluebook-end others
    /*******************************************************************************************************************
     *
     * Sets the sort criterion.
     * 
     * @param sortCriterion  the sort criterion
     * 
     ******************************************************************************************************************/
    public void setSortCriterion (@Nonnull SortCriterion sortCriterion);
    
    /*******************************************************************************************************************
     * 
     * Sets the sort direction.
     *
     * @param sortDirection  the sort direction
     * 
     ******************************************************************************************************************/
    public void setSortDirection (@Nonnull SortDirection sortDirection);
    
    /*******************************************************************************************************************
     *
     * Returns the current sort criterion.
     * 
     * @return  the sort criterion
     * 
     ******************************************************************************************************************/
    @Nonnull
    public SortCriterion getSortCriterion();
    
    /*******************************************************************************************************************
     *
     * Returns the current sort direction.
     * 
     * @return  the sort direction
     * 
     ******************************************************************************************************************/
    @Nonnull
    public SortDirection getSortDirection();
  }
