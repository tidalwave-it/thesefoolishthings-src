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
import it.tidalwave.util.Finder;

/***********************************************************************************************************************
 *
 * A utility interface for creating extended {@link Finder}s, it provides automatic covariant return types. MAke your
 * extended {@code Finder} interface to extend from this. For instance, a custom {@code Date} finder can be declared as:
 * 
 * <pre>
 * public class DateFinder extends ExtendedFinderSupport<Date, DateFinder>
 *   {
 *     public DateFinder before (Date date);
 * 
 *     public DateFinder after (Date date);
 *   }
 * </pre>
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public interface ExtendedFinderSupport<Type, ExtendedFinder extends Finder<Type>> extends Finder<Type>
  {
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ExtendedFinder from (@Nonnegative int firstResult);

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ExtendedFinder max (@Nonnegative int maxResults);

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ExtendedFinder sort (@Nonnull SortCriterion criterion);

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ExtendedFinder sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);
  }