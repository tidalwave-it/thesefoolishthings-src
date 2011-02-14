/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
import java.util.List;
import java.io.Serializable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft
 *
 **********************************************************************************************************************/
public interface Finder<T> extends Cloneable, Serializable
  {
    /***************************************************************************
     *
     *
     **************************************************************************/
    public static interface SortCriterion
      {
      }

    /***************************************************************************
     *
     *
     **************************************************************************/
    public static enum SortDirection
      {
        ASCENDING, DESCENDING
      }

    /***************************************************************************
     *
     *
     **************************************************************************/
    public int count();

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public Finder<T> from (@Nonnegative int firstResult);

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public Finder<T> max (@Nonnegative int maxResults);

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public <X> Finder<X> ofType (@Nonnull Class<X> type);

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public T result()
      throws NotFoundException;

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public T firstResult()
      throws NotFoundException;

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public List<? extends T> results();

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public Finder<T> sort (@Nonnull SortCriterion criterion, @Nonnull SortDirection direction);

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public Finder<T> sort (@Nonnull SortCriterion criterion);
}
