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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder8;
import it.tidalwave.util.Finder8Support;

/***********************************************************************************************************************
 * 
 * An extension of {@link SimpleFinderSupport} that uses {@link Finder8}.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @since 1.38
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public abstract class SimpleFinder8Support<TYPE> extends Finder8Support<TYPE, Finder8<TYPE>>
  {
    protected SimpleFinder8Support()
      {
        super();
      }

    protected SimpleFinder8Support (final @Nonnull String name)
      {
        super(name);
      }
  }