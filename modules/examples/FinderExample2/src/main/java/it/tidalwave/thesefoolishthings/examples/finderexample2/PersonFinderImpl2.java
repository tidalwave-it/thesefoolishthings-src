/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/thesefoolishthings-examples/it-tidalwave-thesefoolishthings-examples-finderexample2
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.thesefoolishthings.examples.finderexample2;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import it.tidalwave.util.spi.FinderSupport;
import it.tidalwave.thesefoolishthings.examples.person.Person;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PersonFinderImpl2 extends FinderSupport<Person, PersonFinder> implements PersonFinder
  {
    // START SNIPPET: public-constructor-and-fields
    @Nonnull
    private final List<Person> persons;

    @Nonnull
    private final String firstNameRegex;

    @Nonnull
    private final String lastNameRegex;

    // This is for public use
    public PersonFinderImpl2 (@Nonnull final List<Person> persons)
      {
        this(persons, ".*",  ".*");
      }
    // END SNIPPET: public-constructor-and-fields

    // This is for internal purposes
    // START SNIPPET: private-constructor
    // This could be generated by Lombok's @RequiredArgsConstructor
    private PersonFinderImpl2 (@Nonnull final List<Person> persons,
                               @Nonnull final String firstNameRegex,
                               @Nonnull final String lastNameRegex)
      {
        this.persons = persons;
        this.firstNameRegex = firstNameRegex;
        this.lastNameRegex = lastNameRegex;
      }
    // END SNIPPET: private-constructor

    // START SNIPPET: clone-constructor
    public PersonFinderImpl2 (@Nonnull final PersonFinderImpl2 other, @Nonnull final Object override)
      {
        super(other, override);
        final PersonFinderImpl2 source = getSource(PersonFinderImpl2.class, other, override);
        this.persons = source.persons;
        this.firstNameRegex = source.firstNameRegex;
        this.lastNameRegex = source.lastNameRegex;
      }
    // END SNIPPET: clone-constructor

    // START SNIPPET: new-methods
    @Override @Nonnull
    public PersonFinder withFirstName (@Nonnull final String regex)
      {
        return clonedWith(new PersonFinderImpl2(persons, regex, lastNameRegex));
      }

    @Override @Nonnull
    public PersonFinder withLastName (@Nonnull final String regex)
      {
        return clonedWith(new PersonFinderImpl2(persons, firstNameRegex, regex));
      }
    // END SNIPPET: new-methods

    // START SNIPPET: computeResults
    @Override @Nonnull
    protected List<? extends Person> computeResults()
      {
        final Pattern firstNamePattern = Pattern.compile(firstNameRegex);
        final Pattern lastNamePattern = Pattern.compile(lastNameRegex);

        return persons.stream()
                      .filter(p -> firstNamePattern.matcher(p.getFirstName()).matches()
                                && lastNamePattern.matcher(p.getLastName()).matches())
                      .collect(Collectors.toList());
      }
    // END SNIPPET: computeResults
  }
