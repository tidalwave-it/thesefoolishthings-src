/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.spi;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.FilterSortCriterion;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.util.NotFoundException;

/***********************************************************************************************************************
 *
 * A support class for implementing a {@link Finder}. Subclasses only need to implement the {@link #doCompute()} method
 * where <i>raw</i> results are retrieved. With raw we mean that they shouldn't be filtered or sorted, as this 
 * post-processing will be performed by this class.
 * 
 * @author Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public abstract class FinderSupport<Type, SpecializedFinder extends Finder<Type>> implements Finder<Type>, Cloneable
  {
    @Nonnull
    private final String name;

    @Nonnegative
    protected int firstResult = 0;

    @Nonnegative
    protected int maxResults = 9999999; // gets inolved in some math, so can't use Integer.MAX_VALUE

    /*******************************************************************************************************************
     *
     * Creates an instance with the given name (that will be used for diagnostics).
     * 
     * @param  name   the name
     *
     ******************************************************************************************************************/
    public FinderSupport (final @Nonnull String name)
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
     * style. Subclasses must first call {@code super.clone()} and then copy their part of state variables.
     * 
     * @return  the cloned object
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected SpecializedFinder clone()
      {
        try 
          {
            final SpecializedFinder clone = (SpecializedFinder)getClass().newInstance();
            ((FinderSupport<Type, SpecializedFinder>)clone).firstResult = this.firstResult;
            ((FinderSupport<Type, SpecializedFinder>)clone).maxResults = this.maxResults;
            return clone;
          }
        catch (InstantiationException e) 
          {
            throw new RuntimeException(e);
          } 
        catch (IllegalAccessException e) 
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
    public SpecializedFinder from (final @Nonnegative int firstResult)
      {
        final SpecializedFinder clone = clone();
        ((FinderSupport<Type, SpecializedFinder>)clone).firstResult = firstResult;
        return clone;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public SpecializedFinder max (final @Nonnegative int maxResults)
      {
        final SpecializedFinder clone = clone();
        ((FinderSupport<Type, SpecializedFinder>)clone).maxResults = maxResults;
        return clone;
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
    public SpecializedFinder sort (final @Nonnull SortCriterion criterion,
                                   final @Nonnull SortDirection direction)
      {
        if (criterion instanceof FilterSortCriterion)
          {
            final FilterSortCriterion<Type> filterSortCriterion = (FilterSortCriterion<Type>)criterion;
            return (SpecializedFinder)filterSortCriterion.sort(this, direction);
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
    public final SpecializedFinder sort (final @Nonnull SortCriterion criterion) 
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
        final List<? extends Type> result = computeResults();

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
        return NotFoundException.throwWhenEmpty(computeResults(), "Empty result").get(0);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public List<? extends Type> results()
      {
        final List<? extends Type> result = computeResults();
        return result.subList(firstResult, Math.min(result.size(), firstResult + maxResults));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public int count()
      {
        return computeResults().size();
      }

    /*******************************************************************************************************************
     *
     * Subclasses must implement this method where raw results are actually retrieved.
     * 
     * @return  the unprocessed results  
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract List<? extends Type> computeResults();
  }
