/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class FinderSupport<T> implements Finder<T>
  {
    @Nonnull
    private final String description;

    @Nonnegative
    protected int firstResult = 0;

    @Nonnegative
    protected int maxResults = 9999999; // gets inolved in some math, so can't use Integer.MAX_VALUE

    @Nonnull
    private final List<T> result = new ArrayList<T>();

    private boolean computed = false;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public FinderSupport (final @Nonnull String description)
      {
        this.description = description;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnegative
    public int count()
      {
        compute();
        return result.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public Finder from (final @Nonnegative int firstResult)
      {
        this.firstResult = firstResult;
        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public Finder max (final @Nonnegative int maxResults)
      {
        this.maxResults = maxResults;
        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public <X> it.tidalwave.util.Finder<X> ofType (final @Nonnull Class<X> type)
      {
        throw new UnsupportedOperationException("Not supported.");
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public T result()
      throws NotFoundException
      {
        compute();
        final String message = String.format("%s", description);

        switch (result.size())
          {
            case 0:
              throw new NotFoundException(message);

            case 1:
              return result.get(0);

            default:
              throw new RuntimeException("More than one result, " + message + ": " + result);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public T firstResult()
      throws NotFoundException
      {
        compute();

        if (result.isEmpty())
          {
            final String message = String.format("%s", description);
            throw new NotFoundException(message);
          }

        return result.get(0);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public List<? extends T> results()
      {
        compute();
        return result.subList(firstResult, Math.min(result.size(), firstResult + maxResults));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public Finder<T> sort (final @Nonnull SortCriterion criterion,
                           final @Nonnull SortDirection direction)
      {
        throw new UnsupportedOperationException("Not supported yet."); // TODO
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    @Nonnull
    public Finder<T> sort (final @Nonnull SortCriterion criterion)
      {
        return sort(criterion, SortDirection.ASCENDING);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract List<T> doCompute();

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private final synchronized void compute()
      {
        if (!computed)
          {
            result.clear();
            result.addAll(doCompute());
            computed = true;
          }
      }
  }
