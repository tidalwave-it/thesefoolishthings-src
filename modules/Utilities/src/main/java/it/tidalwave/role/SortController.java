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
public interface SortController 
  {
    public final static Class<SortController> SortController = SortController.class; 
    
    public final static SortController DEFAULT = new SortController()
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
    
    public void setSortCriterion (@Nonnull SortCriterion sortCriterion);
    
    @Nonnull
    public SortCriterion getSortCriterion();
    
    public void setSortDirection (@Nonnull SortDirection sortDirection);
    
    @Nonnull
    public SortDirection getSortDirection();
  }
