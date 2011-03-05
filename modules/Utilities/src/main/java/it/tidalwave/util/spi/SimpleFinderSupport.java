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

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public abstract class SimpleFinderSupport<Type> extends FinderSupport<Type, SimpleFinderSupport<Type>> 
  {
    public SimpleFinderSupport (final @Nonnull String name) 
      {
        super(name);
      }
    
    protected SimpleFinderSupport()
      {
        super();   
      }
  }