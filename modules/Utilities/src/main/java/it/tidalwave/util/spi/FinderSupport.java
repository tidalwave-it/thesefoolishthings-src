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
package it.tidalwave.util.spi;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.FilterSortCriterion;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.util.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A support class for implementing a {@link Finder}. Subclasses only need to implement the {@link #computeResults()}
 * method where <i>raw</i> results are retrieved (with raw we mean that they shouldn't be filtered or sorted, as
 * post-processing will be performed by this class) and a clone constructor.
 *
 * If you don't need to extend the {@link Finder} with extra methods, please use the simplified
 * {@link SimpleFinderSupport}.
 *
 * @author Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Slf4j @AllArgsConstructor(access = AccessLevel.PRIVATE) @ToString
public class FinderSupport<TYPE, EXTENDED_FINDER extends Finder<TYPE>> implements Finder<TYPE>, Cloneable
  {
    static class Sorter<Type>
      {
        private final FilterSortCriterion<Type> sortCriterion;
        private final SortDirection sortDirection;

        public Sorter (final @Nonnull FilterSortCriterion<Type> sortCriterion,
                       final @Nonnull SortDirection sortDirection)
          {
            this.sortCriterion = sortCriterion;
            this.sortDirection = sortDirection;
          }

        public void sort (final @Nonnull List<? extends Type> results)
          {
            sortCriterion.sort(results, sortDirection);
          }
      }

    private final static String MESSAGE =
          "Since version 2.0, Implementations of Finder must have a clone constructor such as "
        + "MyFinder(MyFinder other, Object override). This means that they can't be implemented by anonymous or inner, "
        + "non static classes. See the javadoc for further information. Could not find constructor: ";

    @Nonnull
    private final String name;

    @Nonnegative
    private final int firstResult;

    @Nonnegative
    private final int maxResults;

    @Nonnull @Getter(AccessLevel.PROTECTED)
    private final List<Object> contexts;

    @Nonnull
    private final List<Sorter<TYPE>> sorters;

    private static final int DEFAULT_MAX_RESULTS = Integer.MAX_VALUE;

    /*******************************************************************************************************************
     *
     * Returns an empty {@code Finder}.
     *
     * @param   <T>     the type of the {@code Finder}
     * @return          the empty {@code Finder}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Finder<T> emptyFinder()
      {
        return new ArrayListFinder<>(Collections.<T>emptyList());
      }

    /*******************************************************************************************************************
     *
     * Creates an instance with the given name (that will be used for diagnostics).
     *
     * @param  name   the name
     *
     ******************************************************************************************************************/
    protected FinderSupport (final @Nonnull String name)
      {
        this.name = name;
        this.firstResult = 0;
        this.maxResults = DEFAULT_MAX_RESULTS;
        this.sorters = new ArrayList<Sorter<TYPE>>();
        this.contexts = Collections.emptyList();
        checkSubClass();
      }

    /*******************************************************************************************************************
     *
     * Default constructor.
     *
     ******************************************************************************************************************/
    protected FinderSupport()
      {
        this.name = getClass().getName();
        this.firstResult = 0;
        this.maxResults = DEFAULT_MAX_RESULTS;
        this.sorters = new ArrayList<Sorter<TYPE>>();
        this.contexts = Collections.emptyList();
        checkSubClass();
      }

    /*******************************************************************************************************************
     *
     * Clone constructor for subclasses.
     *
     ******************************************************************************************************************/
    protected FinderSupport (final @Nonnull FinderSupport<TYPE, EXTENDED_FINDER> other, final @Nonnull Object override)
      {
        log.trace("FinderSupport({}, {})", other, override);
        final FinderSupport<TYPE, EXTENDED_FINDER> source = getSource(FinderSupport.class, other, override);
        this.name = source.name;
        this.firstResult = source.firstResult;
        this.maxResults = source.maxResults;
        this.sorters = source.sorters;
        this.contexts = source.contexts; // it's always unmodifiable
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    protected static <T> T getSource (final Class<T> clazz, final @Nonnull T other, final @Nonnull Object override)
      {
        return override.getClass().equals(clazz) ? (T)override : other;
      }

    /*******************************************************************************************************************
     *
     * @deprecated
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public final FinderSupport<TYPE, EXTENDED_FINDER> clone()
      {
        throw new UnsupportedOperationException("\"FinderSupport.clone() no more supported");
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    protected EXTENDED_FINDER clone (final @Nonnull Object override)
      {
        try
          {
            final Constructor<? extends FinderSupport> constructor = getCloneConstructor();
            constructor.setAccessible(true);
            return (EXTENDED_FINDER)constructor.newInstance(this, override);
          }
        catch (Exception e)
          {
            throw new RuntimeException(e);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EXTENDED_FINDER from (final @Nonnegative int firstResult)
      {
        return clone(new FinderSupport<TYPE, EXTENDED_FINDER>(name, firstResult, maxResults, contexts, sorters));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EXTENDED_FINDER max (final @Nonnegative int maxResults)
      {
        return clone(new FinderSupport<TYPE, EXTENDED_FINDER>(name, firstResult, maxResults, contexts, sorters));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EXTENDED_FINDER withContext (final @Nonnull Object context)
      {
        final List<Object> contexts = concat(this.contexts, context);
        return clone(new FinderSupport<TYPE, EXTENDED_FINDER>(name, firstResult, maxResults, contexts, sorters));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <ANOTHER_TYPE> Finder<ANOTHER_TYPE> ofType (final @Nonnull Class<ANOTHER_TYPE> type)
      {
        throw new UnsupportedOperationException("Must be eventually implemented by subclasses.");
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EXTENDED_FINDER sort (final @Nonnull SortCriterion criterion,
                                 final @Nonnull SortDirection direction)
      {
        if (criterion instanceof FilterSortCriterion)
          {
            final List<Sorter<TYPE>> sorters = concat(this.sorters,
                    new Sorter<TYPE>((FilterSortCriterion<TYPE>)criterion, direction));
            return clone(new FinderSupport<TYPE, EXTENDED_FINDER>(name, firstResult, maxResults, contexts, sorters));
          }

        final String template = "%s does not implement %s - you need to subclass Finder and override sort()";
        final String message = String.format(template, criterion, FilterSortCriterion.class);
        throw new UnsupportedOperationException(message);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public final EXTENDED_FINDER sort (final @Nonnull SortCriterion criterion)
      {
        return sort(criterion, SortDirection.ASCENDING);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public TYPE result()
      throws NotFoundException
      {
        final List<? extends TYPE> result = computeNeededResults();

        switch (result.size())
          {
            case 0:
              throw new NotFoundException(name);

            case 1:
              return result.get(0);

            default:
              throw new RuntimeException("More than one result, " + name + ": " + result);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public TYPE firstResult()
      throws NotFoundException
      {
        return NotFoundException.throwWhenEmpty(computeNeededResults(), "Empty result").get(0);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public List<? extends TYPE> results()
      {
        return computeNeededResults();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public int count()
      {
        return computeNeededResults().size();
      }

    /*******************************************************************************************************************
     *
     * Subclasses can implement this method where *all* the raw results must be actually retrieved.
     *
     * @return  the unprocessed results
     *
     ******************************************************************************************************************/
    @Nonnull
    protected List<? extends TYPE> computeResults()
      {
        throw new UnsupportedOperationException("You must implement me!");
      }

    /*******************************************************************************************************************
     *
     * Subclasses can implement this method where *only the requested* raw results must be retrieved.
     *
     * @return  the unprocessed results
     *
     ******************************************************************************************************************/
    @Nonnull
    protected List<? extends TYPE> computeNeededResults()
      {
        log.trace("computeNeededResults() - {}", this);
        List<? extends TYPE> results = computeResults();

        // First sort and then extract the sublist
        for (final Sorter<TYPE> sorter : sorters)
          {
            log.trace(">>>> sorting with {}...", sorter);
            sorter.sort(results);
          }

        final int toIndex = (int)Math.min(results.size(), (long)firstResult + (long)maxResults);

        if (firstResult > toIndex)
          {
            return new CopyOnWriteArrayList();
          }

        results = results.subList(firstResult, toIndex);

        return results;
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private static <T> List<T> concat (final @Nonnull List<T> list, final @Nonnull T item)
      {
        final List<T> result = new ArrayList<T>(list);
        result.add(item);
        return Collections.unmodifiableList(result);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private Constructor<? extends FinderSupport> getCloneConstructor()
      throws SecurityException, NoSuchMethodException
      {
        return getClass().getConstructor(new Class<?>[] { getClass(), Object.class });
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void checkSubClass()
      {
        try
          {
            getCloneConstructor();
          }
        catch (SecurityException | NoSuchMethodException e)
          {
            throw new ExceptionInInitializerError(MESSAGE + e.getMessage());
          }
      }
  }
