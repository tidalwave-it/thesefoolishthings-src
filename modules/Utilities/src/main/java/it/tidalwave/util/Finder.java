/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.io.Serializable;

/***********************************************************************************************************************
 *
 * A factory for providing results of a search. {@code Finder} implementations must be <em>immutable</em>.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public interface Finder<TYPE> extends Cloneable, Serializable
  {
    /*******************************************************************************************************************
     *
     * A tag interface to mark objects which are meaningful sort criteria that can be passed to
     * {@link Finder#sort(it.tidalwave.util.Finder.SortCriterion)}. In general, a {@code SortCriterion} is just a
     * behaviourless and methodless object, that should be specifically handled by concrete implementations of
     * {@link Finder}. The only exceptions are {@link FilterSortCriterion} objects.
     *
     ******************************************************************************************************************/
    public static interface SortCriterion
      {
        //@bluebook-ignore-begin
        public static final Class<SortCriterion> SortCriterion = SortCriterion.class;

        /** A special {@link SortCriterion} which indicates that no sort has been performed. */
        public static final SortCriterion UNSORTED = new FilterSortCriterion<Object>()
          {
            public void sort (final @Nonnull List<?> results, final @Nonnull SortDirection sortDirection)
              {
              }
          };

        public static final SortCriterion DEFAULT = UNSORTED;
        //@bluebook-ignore-end
      }

    /*******************************************************************************************************************
     *
     * An interface that should be implemented by specific {@link SortCriterion} objects which are capable to implement
     * by themselves the sorting of objects, by post-processing an existing collection of objects. While this is often
     * convenient, it is possible for it to be inefficient in cases in which the original source of objects is capable
     * to perform the sort in an optimized way (e.g. a SQL database by means of {@code ORDER BY}}. The facility class
     * {@link FinderSupport} supports {@code FilterSortCriterion} objects out of the box. A convenient partial
     * implementation of {@code FilterSortCriterion} is {@link DefaultFilterSortCriterion}.
     *
     ******************************************************************************************************************/
    public static interface FilterSortCriterion<TYPE> extends SortCriterion
      {
        /***************************************************************************************************************
         *
         * Performs the sort of results.
         *
         * @param  results        the list of objects to be sorted in place
         * @param  sortDirection  the sort direction
         *
         **************************************************************************************************************/
        @Nonnull
        public void sort (@Nonnull List<? extends TYPE> results, @Nonnull SortDirection sortDirection);
      }

    /*******************************************************************************************************************
     *
     * An enumeration to define the direction of a sort (ascending or descending).
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    public static enum SortDirection
      {
        ASCENDING(+1), DESCENDING(-1);
        //@bluebook-ignore-begin

        private final int intValue;

        private SortDirection (final int intValue)
          {
            this.intValue = intValue;
          }

        /** @return  +1 for ascending direction, -1 for descending */
        public int intValue()
          {
            return intValue;
          }
        //@bluebook-ignore-end
      }

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that only a subset of found items will be returned, starting from the given position.
     *
     * @param   firstResult    the index of the first result to return
     * @return                 the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Finder<TYPE> from (@Nonnegative int firstResult);

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that only a maximum number of found items will be returned.
     *
     * @param   maxResults    the max number of results to return
     * @return                the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Finder<TYPE> max (@Nonnegative int maxResults);
    
    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that results should be created with the given context. This method can be called 
     * multiple times; contexts are accumulated.
     *
     * @param  context    the context
     * @return            the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Finder<TYPE> withContext (@Nonnull Object context);

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that the specified type of results is expected.
     *
     * @param   type      the expected type of results
     * @return            the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <AnotherType> Finder<AnotherType> ofType (@Nonnull Class<AnotherType> type);

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that results will be sorted according to the given criterion, in ascending direction.
     *
     * @param  criterion  the sort criterion
     * @return            the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Finder<TYPE> sort (@Nonnull SortCriterion criterion);

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that results will be sorted according to the given criterion and direction.
     *
     * @param  criterion  the sort criterion
     * @param  direction  the sort direction
     * @return            the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public Finder<TYPE> sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);

    /*******************************************************************************************************************
     *
     * Performs the search assuming that it will return a single item and returns it. This method fails if the search
     * returns more than one single item.
     *
     * @return                    the found item
     * @throws NotFoundException  if the search didn't find anything
     * @throws RuntimeException   if the search returned more than one single item
     *
     ******************************************************************************************************************/
    @Nonnull
    public TYPE result()
      throws NotFoundException, RuntimeException;

    /*******************************************************************************************************************
     *
     * Performs the search and returns only the first found item.
     *
     * @return                    the first found item
     * @throws NotFoundException  if the search didn't find anything
     *
     ******************************************************************************************************************/
    @Nonnull
    public TYPE firstResult()
      throws NotFoundException;

    /*******************************************************************************************************************
     *
     * Performs the search and returns the found items.
     *
     * @return            the searched items
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<? extends TYPE> results();

    /*******************************************************************************************************************
     *
     * Performs the search and returns the count of found items.
     *
     * @return            the count of found items
     *
     ******************************************************************************************************************/
    @Nonnegative
    public int count();
  }
