/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.util.spi.SimpleFinderSupport;

/***********************************************************************************************************************
 *
 * An implementation of {@link Composite} which holds an immutable list of items.
 * 
 * @param  <TYPE>   the type of contained items
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * 
 **********************************************************************************************************************/
public class ArrayListSimpleComposite<TYPE> extends DefaultSimpleComposite<TYPE>
  {
    private static final long serialVersionUID = 1L;
    
    public ArrayListSimpleComposite (final @Nonnull List<TYPE> items)
      {
        super(new SimpleFinderSupport<TYPE>()
          {
            @Override @Nonnull
            protected List<? extends TYPE> computeResults()
              {
                return new CopyOnWriteArrayList<TYPE>(items);
              }
          });
      }
  }