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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Finder8<TYPE> extends Finder<TYPE>, Iterable<TYPE>
  {
    @Nonnull
    default public Optional<TYPE> optionalResult()
      {
        try
          {
            return Optional.of(result());
          }
        catch (NotFoundException e)
          {
            return Optional.empty();
          }
      }

    @Nonnull
    default public Optional<TYPE> optionalFirstResult()
      {
        try
          {
            return Optional.of(firstResult());
          }
        catch (NotFoundException e)
          {
            return Optional.empty();
          }
      }

    @Nonnull
    default public Stream<TYPE> stream()
      {
        return ((List<TYPE>)results()).stream();
      }

    @Override @Nonnull
    default public Iterator<TYPE> iterator()
      {
        return ((List<TYPE>)results()).iterator();
      }

    // TODO: this should come by means of ExtendedFinder
    @Override @Nonnull
    public Finder8<TYPE> from (@Nonnegative int firstResult);

    @Override @Nonnull
    public Finder8<TYPE> max (@Nonnegative int maxResults);

    @Override @Nonnull
    public Finder8<TYPE> withContext (@Nonnull Object context);

    @Override @Nonnull
    public Finder8<TYPE> sort (@Nonnull SortCriterion criterion);

    @Override @Nonnull
    public Finder8<TYPE> sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);
  }
