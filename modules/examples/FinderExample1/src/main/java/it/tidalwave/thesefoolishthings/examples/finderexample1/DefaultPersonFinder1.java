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
package it.tidalwave.thesefoolishthings.examples.finderexample1;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
/* package */ class DefaultPersonFinder1 extends SimpleFinderSupport<Person> implements Finder<Person>
  {
    private List<Person> persons;
    
    public DefaultPersonFinder1 (final @Nonnull List<Person> persons) 
      {
        this.persons = persons;
      }
    
    @Override @Nonnull
    public DefaultPersonFinder1 clone()
      {
        final DefaultPersonFinder1 clone = (DefaultPersonFinder1)super.clone();
        clone.persons = this.persons;
        
        return clone;
      }

    @Override @Nonnull
    protected List<? extends Person> computeResults() 
      {
        return new ArrayList<Person>(persons); // don't expose internal status
      }
  }