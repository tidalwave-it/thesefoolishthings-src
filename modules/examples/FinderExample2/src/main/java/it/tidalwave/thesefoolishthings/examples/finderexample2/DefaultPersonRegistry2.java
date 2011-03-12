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
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.thesefoolishthings.examples.finderexample1.Person;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultPersonRegistry2 implements PersonRegistry2 
  {
    private final List<Person> persons = new ArrayList<Person>();
    
    @Override
    public void add (final @Nonnull Person person)
      {
        persons.add(person);    
      }
    
    @Override @Nonnull
    public PersonFinder findPersons()
      {
        return new DefaultPersonFinder2(persons);
      }
  }