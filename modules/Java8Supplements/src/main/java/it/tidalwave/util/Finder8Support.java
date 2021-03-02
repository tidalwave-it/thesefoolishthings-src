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

import javax.annotation.Nonnull;
import it.tidalwave.util.spi.FinderSupport;
import it.tidalwave.util.spi.ExtendedFinder8Support;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class Finder8Support<TYPE, EXTENDED_FINDER extends Finder8<TYPE>> 
                                extends FinderSupport<TYPE, EXTENDED_FINDER>
                                implements ExtendedFinder8Support<TYPE, EXTENDED_FINDER>, 
                                           Finder8<TYPE>
  {
    private static final long serialVersionUID = 1L;

    /*******************************************************************************************************************
     *
     * Default constructor.
     *
     ******************************************************************************************************************/
    public Finder8Support() 
      {
      }
    
    /*******************************************************************************************************************
     *
     * Default constructor.
     *
     ******************************************************************************************************************/
    public Finder8Support (final @Nonnull String name) 
      {
        super(name);
      }
    
    /*******************************************************************************************************************
     *
     * Clone constructor.
     *
     ******************************************************************************************************************/
    protected Finder8Support (final @Nonnull Finder8Support<TYPE, EXTENDED_FINDER> other, final @Nonnull Object override)
      {
        super(other, override);
      }
  }
