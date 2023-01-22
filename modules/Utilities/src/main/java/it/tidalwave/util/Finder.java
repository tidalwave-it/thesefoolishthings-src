/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.io.Serializable;
import it.tidalwave.util.impl.ArrayListFinder;
import it.tidalwave.util.impl.MappingFinder;
import it.tidalwave.util.impl.SupplierFinder;
import it.tidalwave.util.impl.ProviderFinder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A factory for providing results of a search. {@code Finder} implementations must be <em>immutable</em>.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public interface Finder<T> extends Cloneable, Serializable
  {
    /*******************************************************************************************************************
     *
     * A tag interface to mark objects which are meaningful sort criteria that can be passed to
     * {@link Finder#sort(it.tidalwave.util.Finder.SortCriterion)}. In general, a {@code SortCriterion} is just a
     * behaviourless and methodless object, that should be specifically handled by concrete implementations of
     * {@link Finder}. The only exceptions are {@link InMemorySortCriterion} objects.
     *
     ******************************************************************************************************************/
    public static interface SortCriterion
      {
        public static final Class<SortCriterion> _SortCriterion_ = SortCriterion.class;

        /** A special {@link SortCriterion} which indicates that no sort has been performed. */
        public static final SortCriterion UNSORTED = (InMemorySortCriterion<Object>)(results, sortDirection) -> {};

        public static final SortCriterion DEFAULT = UNSORTED;
      }

    /*******************************************************************************************************************
     *
     * An interface that should be implemented by specific {@link SortCriterion} objects which are capable to implement
     * by themselves the sorting of objects, by post-processing an existing collection of objects. While this is often
     * convenient, it is possible for it to be inefficient in cases in which the original source of objects is capable
     * to perform the sort in an optimized way (e.g. an SQL database by means of {@code ORDER BY}). The facility class
       * {@link it.tidalwave.util.spi.HierarchicFinderSupport} supports {@code FilterSortCriterion} objects out of the box.
     *
     ******************************************************************************************************************/
    public static interface InMemorySortCriterion<U> extends SortCriterion
      {
        /***************************************************************************************************************
         *
         * Performs the sort of results.
         *
         * @param  results        the list of objects to be sorted in place
         *
         **************************************************************************************************************/
        public default void sort (@Nonnull final List<? extends U> results)
          {
            sort(results, SortDirection.ASCENDING);
          }

        /***************************************************************************************************************
         *
         * Performs the sort of results.
         *
         * @param  results        the list of objects to be sorted in place
         * @param  sortDirection  the sort direction
         *
         **************************************************************************************************************/
        // START SNIPPET: sort
        public void sort (@Nonnull List<? extends U> results, @Nonnull SortDirection sortDirection);
        // END SNIPPET: sort

        /***************************************************************************************************************
         *
         * Creates a new in-memory {@code SortCriterion} based on a {@link Comparator}.
         *
         * @param <U>           the type of the objects to compare
         * @param comparator    the {@code Comparator}
         * @return              the new {@code SortCriterion}
         *
         **************************************************************************************************************/
        @Nonnull
        public static <U> InMemorySortCriterion<U> of (@Nonnull final Comparator<? super U> comparator)
          {
            return of(comparator, comparator.getClass().getSimpleName());
          }

        /***************************************************************************************************************
         *
         * Creates a new in-memory {@code SortCriterion} based on a {@link Comparator}.
         *
         * @param <U>           the type of the objects to compare
         * @param comparator    the {@code Comparator}
         * @param name          a name
         * @return              the new {@code SortCriterion}
         *
         **************************************************************************************************************/
        @Nonnull
        public static <U> InMemorySortCriterion<U> of (@Nonnull final Comparator<? super U> comparator,
                                                       @Nonnull final String name)
          {
            return new DefaultInMemorySortCriterion<>(comparator, name);
          }

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @AllArgsConstructor @ToString @EqualsAndHashCode
        static class DefaultInMemorySortCriterion<U> implements Finder.InMemorySortCriterion<U>, Serializable
          {
            private static final long serialVersionUID = 76093596048395982L;

            @Nonnull
            private final Comparator<? super U> comparator;

            @Nonnull
            private final String name;

            @Override
            public void sort (@Nonnull final List<? extends U> results, @Nonnull final SortDirection sortDirection)
              {
                results.sort((Comparator<U>)(o1, o2) -> comparator.compare(o1, o2) * sortDirection.intValue());
              }
          }
      }

    /*******************************************************************************************************************
     *
     * An enumeration to define the direction of a sort (ascending or descending).
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static enum SortDirection
      {
        ASCENDING(+1), DESCENDING(-1);

        private final int intValue;

        /** @return  +1 for ascending direction, -1 for descending */
        public int intValue()
          {
            return intValue;
          }
      }

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that only a subset of found items will be returned, starting from the given position.
     *
     * @param   firstResult    the index of the first result to return
     * @return                 the {@code Finder}
     *
     ******************************************************************************************************************/
    // START SNIPPET: from
    @Nonnull
      public Finder<T> from (@Nonnegative int firstResult);
    // END SNIPPET: from

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that only a maximum number of found items will be returned.
     *
     * @param   maxResults    the max number of results to return
     * @return                the {@code Finder}
     *
     ******************************************************************************************************************/
    // START SNIPPET: max
    @Nonnull
    public Finder<T> max (@Nonnegative int maxResults);
    // END SNIPPET: max

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
    public default Finder<T> withContext (@Nonnull final Object context)
      {
        throw new UnsupportedOperationException("Not implemented yet.");
      }

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that the specified type of results is expected.
     *
     * @param <U>  the static type
     * @param   type          the dynamic type
     * @return                the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <U> Finder<U> ofType (@Nonnull final Class<U> type)
      {
        throw new UnsupportedOperationException("Not implemented yet.");
      }

    /*******************************************************************************************************************
     *
     * Tells the {@code Finder} that results will be sorted according to the given criterion, in ascending direction.
     *
     * @param  criterion  the sort criterion
     * @return            the {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Finder<T> sort (@Nonnull final SortCriterion criterion)
      {
        return sort(criterion, SortDirection.ASCENDING);
      }


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
    public Finder<T> sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);

    /*******************************************************************************************************************
     *
     * Performs the search and returns the found items.
     *
     * @return            the searched items
     *
     ******************************************************************************************************************/
    // START SNIPPET: results
    @Nonnull
    public List<T> results();
    // END SNIPPET: results

    /*******************************************************************************************************************
     *
     * Performs the search and returns the count of found items.
     *
     * @return            the count of found items
     *
     ******************************************************************************************************************/
    // START SNIPPET: count
    @Nonnegative
    public int count();
    // END SNIPPET: count

    /*******************************************************************************************************************
     *
     * Performs the search assuming that it will return a single item and returns it. This method fails if the search
     * returns more than one single item.
     *
     * @return            the optional result
     * @throws RuntimeException   if the search returned more than one single item
     *
     * @since 3.2-ALPHA-1 (previously in Finder8)
     *
     ******************************************************************************************************************/
    // START SNIPPET: optionalResult
    @Nonnull
    public default Optional<T> optionalResult()
    // END SNIPPET: optionalResult
      {
        final var results = (List<T>)results();

        if (results.size() > 1)
          {
            throw new RuntimeException("" + results.size() + " results, expected only one");
          }

        return results.stream().findFirst();
      }

    /*******************************************************************************************************************
     *
     * Performs the search and returns only the first found item.
     *
     * @return            the first result
     * @since 3.2-ALPHA-1 (previously in Finder8)
     *
     ******************************************************************************************************************/
    // START SNIPPET: optionalFirstResult
    @Nonnull
    public default Optional<T> optionalFirstResult()
    // END SNIPPET: optionalFirstResult
      {
        return stream().findFirst();
      }

    /*******************************************************************************************************************
     *
     * Returns a stream of results.
     *
     * @return    the stream
     * @since 3.2-ALPHA-1 (previously in Finder8)
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Stream<T> stream()
      {
        return results().stream();
      }

    /*******************************************************************************************************************
     *
     * Returns am iterator of results.
     *
     * @return    the iterator
     * @since 3.2-ALPHA-1 (previously in Finder8)
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Iterator<T> iterator()
      {
        return results().iterator();
      }

    /*******************************************************************************************************************
     *
     * Performs the search assuming that it will return a single item and returns it. This method fails if the search
     * returns more than one single item.
     *
     * @return                    the found item
     * @throws NotFoundException  if the search didn't find anything
     * @throws RuntimeException   if the search returned more than one single item
     * @deprecated                Use {@link #optionalResult()} instead
     *
     ******************************************************************************************************************/
    @Nonnull @Deprecated
    public default T result()
            throws NotFoundException, RuntimeException
      {
        return optionalResult().orElseThrow(NotFoundException::new);
      }

    /*******************************************************************************************************************
     *
     * Performs the search and returns only the first found item.
     *
     * @return                    the first found item
     * @throws NotFoundException  if the search didn't find anything
     * @deprecated                Use {@link #optionalFirstResult()} instead
     *
     ******************************************************************************************************************/
    @Nonnull @Deprecated
    public default T firstResult()
            throws NotFoundException
      {
        return optionalFirstResult().orElseThrow(NotFoundException::new);
      }

    /*******************************************************************************************************************
     *
     * Returns an empty {@code Finder}.
     *
     * @param   <U>     the type of the {@code Finder}
     * @return          the empty {@code Finder}
     * @since 3.2-ALPHA-1 (previously in HierarchicFinderSupport.emptyFinder())
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <U> Finder<U> empty()
      {
        return ofCloned(Collections.emptyList());
      }

    /*******************************************************************************************************************
     *
     * Returns a wrapped {@code Finder} on a given collection of elements. The collection is cloned and will be
     * immutable.
     * If you need to compute the collection on demand, use {@link #ofSupplier(Supplier)}.
     * This method retrieves the full range of results that will be later segmented in compliance with the values
     * specified by {@link #from(int)} and {@link #max(int)}; this is ok if the whole list of results is already
     * available of if it is not expensive to compute. The alternate method {@link #ofProvider(BiFunction)} allows
     * to access the 'from' and 'max' parameter, so only the required items need to be provided.
     *
     * @param   <U>     the type of the {@code Finder}
     * @param   items   the objects to wrap
     * @return          the wrapped {@code Finder}
     * @see             #ofSupplier(Supplier)
     * @see             #ofProvider(BiFunction)
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    // START SNIPPET: ofCloned
    @Nonnull
    public static <U> Finder<U> ofCloned (@Nonnull final Collection<? extends U> items)
    // END SNIPPET: ofCloned
      {
        return new ArrayListFinder<>(items);
      }

    /*******************************************************************************************************************
     *
     * Returns a wrapped {@code Finder} on a given supplier. The collection will be cloned after being supplied.
     * This method retrieves the full range of results that will be later segmented in compliance with the values
     * specified by {@link #from(int)} and {@link #max(int)}; this is ok if the whole list of results is already
     * available of if it is not expensive to compute. The alternate method {@link #ofProvider(BiFunction)} allows
     * to access the 'from' and 'max' parameter, so only the required items need to be provided.
     *
     * @param   <U>       the type of the {@code Finder}
     * @param   supplier  the supplier
     * @return            the wrapped {@code Finder}
     * @see               #ofCloned(Collection) 
     * @see               #ofProvider(BiFunction)
     * @since 3.2-ALPHA-15
     *
     ******************************************************************************************************************/
    // START SNIPPET: ofsupplier
    @Nonnull
    public static <U> Finder<U> ofSupplier (@Nonnull final Supplier<? extends Collection<? extends U>> supplier)
    // END SNIPPET: ofsupplier
      {
        return new SupplierFinder<>(supplier);
      }

    /*******************************************************************************************************************
     *
     * Returns a wrapped {@code Finder} on a given function to provide results. The function receives the 'from' and
     * 'max' arguments to select a subrange of the results. The collection will be cloned after being supplied.
     *
     * @param   <U>       the type of the {@code Finder}
     * @param   provider  the function providing results
     * @return            the wrapped {@code Finder}
     * @see               #ofCloned(Collection)
     * @see               #ofSupplier(Supplier) 
     * @since 3.2-ALPHA-15
     *
     ******************************************************************************************************************/
    // START SNIPPET: ofProvider
    @Nonnull
    public static <U> Finder<U> ofProvider (
            @Nonnull final BiFunction<Integer, Integer, ? extends Collection<? extends U>> provider)
    // END SNIPPET: ofProvider
      {
        return new ProviderFinder<>(provider);
      }

    /*******************************************************************************************************************
     *
     * Returns a mapping {@code Finder} on a given delegate {@code Finder}. The mapper finder provides the same
     * results as the delegate, transformed by a mapper function.
     *
     * @param   <U>       the type of the {@code Finder}
     * @param   <V>       the type of the delegate {@code Finder}
     * @param   delegate  the delegate finder
     * @param   mapper the mapper function
     * @return            the wrapped {@code Finder}
     * @since 3.2-ALPHA-15
     *
     ******************************************************************************************************************/
    // START SNIPPET: mapping
    @Nonnull
    public static <U, V> Finder<U> mapping (@Nonnull final Finder<V> delegate,
                                            @Nonnull final Function<? super V, ? extends U> mapper)
    // END SNIPPET: mapping
      {
        return new MappingFinder<>(delegate, mapper);
      }
  }
