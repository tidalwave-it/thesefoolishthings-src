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
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import it.tidalwave.util.spi.FinderSupport;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
/* package */ class DefaultPersonFinder2 extends FinderSupport<Person, PersonFinder> implements PersonFinder
  {
    @Nonnull
    private final List<Person> persons;

    @Nonnull
    private final String firstName; 

    @Nonnull
    private final String lastName; 

    public DefaultPersonFinder2 (final @Nonnull List<Person> persons)
      {
        this.persons = persons;
        this.firstName = ".*";
        this.lastName = ".*";
      }
    
    public DefaultPersonFinder2 (final @Nonnull DefaultPersonFinder2 other, final @Nonnull Object override)
      {
        super(other, override);
        this.persons = new ArrayList<Person>(other.persons);
        final DefaultPersonFinder2 source = getSource(DefaultPersonFinder2.class, other, override);
        this.firstName = source.firstName;
        this.lastName = source.lastName;
      }

    @Override @Nonnull
    public PersonFinder withFirstName (final @Nonnull String firstName)
      {
        return clone(new DefaultPersonFinder2(persons, firstName, lastName));
      }

    @Override @Nonnull
    public PersonFinder withLastName (final @Nonnull String lastName)
      {
        return clone(new DefaultPersonFinder2(persons, firstName, lastName));
      }

    @Override @Nonnull
    protected List<? extends Person> computeResults()
      {
        final List<Person> result = new ArrayList<Person>();
        final Pattern firstNameRegEx = Pattern.compile(firstName);
        final Pattern lastNameRegEx = Pattern.compile(lastName);

        for (final Person person : persons)
          {
            if (firstNameRegEx.matcher(person.getFirstName()).matches()
                && lastNameRegEx.matcher(person.getLastName()).matches())
              {
                result.add(person);
              }
          }

        return result;
      }
  }
