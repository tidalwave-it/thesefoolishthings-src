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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.finderexample1;

import it.tidalwave.thesefoolishthings.examples.person.Person;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@AllArgsConstructor
/* package */ class DefaultPersonFinder1 extends SimpleFinderSupport<Person> implements Finder<Person>
  {
    private List<Person> persons;

    public DefaultPersonFinder1 (final @Nonnull DefaultPersonFinder1 clone, final @Nonnull Object override)
      {
        super(clone, override);
        this.persons = new ArrayList<Person>(clone.persons);
      }

    @Override @Nonnull
    protected List<? extends Person> computeResults()
      {
        return new ArrayList<Person>(persons); // don't expose internal status
      }
  }
