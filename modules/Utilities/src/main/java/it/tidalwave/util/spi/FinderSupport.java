/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.util.spi;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.FilterSortCriterion;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.util.NotFoundException;
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
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
@Slf4j @ToString
public abstract class FinderSupport<Type, ExtendedFinder extends Finder<Type>> implements Finder<Type>, Cloneable
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
    
    @Nonnull
    private String name;

    @Nonnegative
    protected int firstResult = 0;

    @Nonnegative
    protected int maxResults = 9999999; // gets inolved in some math, so can't use Integer.MAX_VALUE

    @Nonnull
    protected List<Sorter<Type>> sorters = new ArrayList<Sorter<Type>>();
    
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
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    protected FinderSupport()
      {
        this.name = getClass().getName(); 
      }

    /*******************************************************************************************************************
     *
     * Clones this object. This operation is called whenever a parameter-setting method is called in fluent-interface
     * style. 
     * 
     * @return  the cloned object
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public FinderSupport<Type, ExtendedFinder> clone() 
      {
        try 
          {
            final FinderSupport<Type, ExtendedFinder> clone = (FinderSupport<Type, ExtendedFinder>)super.clone();
            clone.name        = this.name;
            clone.firstResult = this.firstResult;
            clone.maxResults  = this.maxResults;
            clone.sorters     = new ArrayList<Sorter<Type>>(this.sorters);
        
            return clone;
          }
        catch (CloneNotSupportedException e)
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
    public ExtendedFinder from (final @Nonnegative int firstResult)
      {
        final FinderSupport<Type, ExtendedFinder> clone = clone();
        clone.firstResult = firstResult;
        return (ExtendedFinder)clone;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public ExtendedFinder max (final @Nonnegative int maxResults)
      {
        final FinderSupport<Type, ExtendedFinder> clone = clone();
        clone.maxResults = maxResults;
        return (ExtendedFinder)clone;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <AnotherType> Finder<AnotherType> ofType (final @Nonnull Class<AnotherType> type)
      {
        throw new UnsupportedOperationException("Must be eventually implemented by subclasses.");
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public ExtendedFinder sort (final @Nonnull SortCriterion criterion,
                                final @Nonnull SortDirection direction)
      {
        if (criterion instanceof FilterSortCriterion)
          {
            final FinderSupport<Type, ExtendedFinder> clone = clone();
            clone.sorters.add(new Sorter<Type>((FilterSortCriterion<Type>)criterion, direction));
            return (ExtendedFinder)clone;
          }
        
        final String message = String.format("%s does not implement %s - you need to subclass Finder and override sort()",
                                             criterion, FilterSortCriterion.class);
        throw new UnsupportedOperationException(message); 
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public final ExtendedFinder sort (final @Nonnull SortCriterion criterion) 
      {
        return sort(criterion, SortDirection.ASCENDING);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Type result()
      throws NotFoundException
      {
        final List<? extends Type> result = computeNeededResults();

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
    public Type firstResult()
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
    public List<? extends Type> results()
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
    protected List<? extends Type> computeResults()
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
    protected List<? extends Type> computeNeededResults()
      {
        log.debug("computeNeededResults() - {}", this);
        List<? extends Type> results = computeResults();
        
        // First sort and then extract the sublist
        for (final Sorter<Type> sorter : sorters)
          {
            log.trace(">>>> sorting with {}...", sorter);
            sorter.sort(results);                        
          }
        
        results = results.subList(firstResult, Math.min(results.size(), firstResult + maxResults));
        
        return results;
      }
  }
