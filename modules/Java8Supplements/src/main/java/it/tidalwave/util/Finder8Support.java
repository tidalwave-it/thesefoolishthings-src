/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import it.tidalwave.util.spi.ExtendedFinderSupport;
import it.tidalwave.util.spi.FinderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class Finder8Support<TYPE, EXTENDED_FINDER extends Finder<TYPE>> 
                                extends FinderSupport<TYPE, EXTENDED_FINDER>
                                implements ExtendedFinderSupport<TYPE, EXTENDED_FINDER>, 
                                           Finder8<TYPE>
  {
    private static final long serialVersionUID = 1L;

    public Finder8Support() 
      {
      }
    
    public Finder8Support (final @Nonnull String name) 
      {
        super(name);
      }

    @Override @Nonnull
    public Optional<TYPE> optionalResult()
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
    
    @Override @Nonnull
    public Optional<TYPE> optionalFirstResult()
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
    
    @Override @Nonnull
    public Stream<TYPE> stream() 
      {
        return ((List<TYPE>)results()).stream();
      }    
  }
