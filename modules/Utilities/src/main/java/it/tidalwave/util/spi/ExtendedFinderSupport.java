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
package it.tidalwave.util.spi;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;

/***************************************************************************************************************************************************************
 *
 * A utility interface for creating extended {@link Finder}s, it provides automatic covariant return types. Make your
 * extended {@code Finder} interface to extend from this. For instance, a custom {@code Date} finder can be declared as:
 *
 * <pre>
 * public interface DateFinder extends ExtendedFinderSupport&lt;SomeClass, DateFinder&gt;
 *   {
 *     public DateFinder before (Date date);
 *
 *     public DateFinder after (Date date);
 *   }
 * </pre>
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **************************************************************************************************************************************************************/
// START SNIPPET: declaration
public interface ExtendedFinderSupport<T, F extends Finder<T>> extends Finder<T>
  {
    /** {@inheritDoc} */
    @Override @Nonnull
    public F from (@Nonnegative int firstResult);

    /** {@inheritDoc} */
    @Override @Nonnull
    public F max (@Nonnegative int maxResults);

    /** {@inheritDoc} */
    @Override @Nonnull
    public F sort (@Nonnull SortCriterion criterion);

    /** {@inheritDoc} */
    @Override @Nonnull
    public F sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);

    /** {@inheritDoc} */
    @Override @Nonnull
    public F withContext (@Nonnull Object context);
  }
// END SNIPPET: declaration

