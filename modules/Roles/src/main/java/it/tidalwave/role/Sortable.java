/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;

/***************************************************************************************************************************************************************
 *
 * The role of an object that has contents that can be sorted.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **************************************************************************************************************************************************************/
public interface Sortable
  {
    public static final Class<Sortable> _Sortable_ = Sortable.class;

    /***********************************************************************************************************************************************************
     * A default {@code Sortable} which does nothing (useful for implementing the NullObject pattern). This object always returns
     * {@link SortCriterion#UNSORTED} as {@code sortCriterion} and {@link SortDirection#ASCENDING} as {@code sortDirection}.
     **********************************************************************************************************************************************************/
    public static final Sortable DEFAULT = new Sortable()
      {
        @Override
        public void setSortCriterion (@Nonnull final SortCriterion sortCriterion)
          {
          }

        @Override @Nonnull
        public SortCriterion getSortCriterion()
          {
            return Finder.SortCriterion.UNSORTED;
          }

        @Override
        public void setSortDirection (@Nonnull final SortDirection sortDirection)
          {
          }

        @Override @Nonnull
        public SortDirection getSortDirection()
          {
            return SortDirection.ASCENDING;
          }
      };

    /***********************************************************************************************************************************************************
     * Sets the sort criterion.
     * @param sortCriterion  the sort criterion
     **********************************************************************************************************************************************************/
    @SuppressWarnings("EmptyMethod")
    public void setSortCriterion (@Nonnull SortCriterion sortCriterion);

    /***********************************************************************************************************************************************************
     * Sets the sort direction.
     * @param sortDirection  the sort direction
     **********************************************************************************************************************************************************/
    @SuppressWarnings("EmptyMethod")
    public void setSortDirection (@Nonnull SortDirection sortDirection);

    /***********************************************************************************************************************************************************
     * Returns the current sort criterion.
     * @return  the sort criterion
     **********************************************************************************************************************************************************/
    @Nonnull @SuppressWarnings("SameReturnValue")
    public SortCriterion getSortCriterion();

    /***********************************************************************************************************************************************************
     * Returns the current sort direction.
     * @return  the sort direction
     **********************************************************************************************************************************************************/
    @Nonnull @SuppressWarnings("SameReturnValue")
    public SortDirection getSortDirection();
  }
