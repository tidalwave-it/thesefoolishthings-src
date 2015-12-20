/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import it.tidalwave.role.Aggregate;
import it.tidalwave.util.NotFoundException;

/***********************************************************************************************************************
 *
 * A map-based implementation of {@link Aggregate}. 
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class MapAggregate<TYPE> implements Aggregate<TYPE>
  {
    private final Map<String, TYPE> mapByName = new HashMap<>();

    public MapAggregate (final Map<String, TYPE> mapByName) 
      {
        this.mapByName.putAll(mapByName);
      }
    
    @Override @Nonnull
    public TYPE getByName (final @Nonnull String name)
      throws NotFoundException
      {
        return NotFoundException.throwWhenNull(mapByName.get(name), "Item not found: " + name);
      }
  }
