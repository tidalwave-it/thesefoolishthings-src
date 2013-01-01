/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role;

import it.tidalwave.role.spi.DefaultDisplayable;
import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * The role of an object which can provide its own display name.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface Displayable
  {
    //@bluebook-begin other
    public final static Class<Displayable> Displayable = Displayable.class;
    
    /*******************************************************************************************************************
     *
     * A default {@code Displayable} with a empty display name.
     * 
     ******************************************************************************************************************/
    public final static Displayable DEFAULT = new DefaultDisplayable("", "DEFAULT"); 
    
    //@bluebook-end other
    /*******************************************************************************************************************
     *
     * Returns the display name in the current {@link Locale}.
     *
     * @returns  the display name
     *
     ******************************************************************************************************************/
    @Nonnull
    public String getDisplayName();
  }
