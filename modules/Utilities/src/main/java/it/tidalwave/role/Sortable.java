/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Sortable 
  {
    //@bluebook-begin others
    public final static Class<Sortable> Sortable = Sortable.class; 
    
    public final static Sortable DEFAULT = new Sortable()
      {
        @Override
        public void setSortCriterion (final @Nonnull SortCriterion sortCriterion)
          {
          }

        @Override @Nonnull 
        public SortCriterion getSortCriterion() 
          {
            throw new UnsupportedOperationException("Not supported.");
          }

        @Override
        public void setSortDirection (final @Nonnull SortDirection sortDirection) 
          {
          }

        @Override @Nonnull 
        public SortDirection getSortDirection() 
          {
            throw new UnsupportedOperationException("Not supported.");
          }
      };
    
    //@bluebook-end others
    public void setSortCriterion (@Nonnull SortCriterion sortCriterion);
    
    public void setSortDirection (@Nonnull SortDirection sortDirection);
    
    @Nonnull
    public SortCriterion getSortCriterion();
    
    @Nonnull
    public SortDirection getSortDirection();
  }
