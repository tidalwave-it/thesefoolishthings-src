/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
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
package it.tidalwave.thesefoolishthings.examples.extendedfinderexample;

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
public class PersonFinderImpl2a extends FinderSupport<Person, PersonFinder> implements PersonFinder
  {
    // START SNIPPET: public-constructor-and-fields
    @Nonnull
    private final List<Person> persons;

    @Nonnull
    private final Pattern firstNamePattern;

    @Nonnull
    private final Pattern lastNamePattern;

    // This is for public use
    public PersonFinderImpl2a (@Nonnull final List<Person> persons)
      {
        this(persons, Pattern.compile(".*"),  Pattern.compile(".*"));
      }
    // END SNIPPET: public-constructor-and-fields

    // This is for internal purposes
    // START SNIPPET: private-constructor
    // This could be generated by Lombok @RequiredArgsConstructor
    private PersonFinderImpl2a (@Nonnull final List<Person> persons,
                                @Nonnull final Pattern firstNamePattern,
                                @Nonnull final Pattern lastNamePattern)
      {
        this.persons = persons;
        this.firstNamePattern = firstNamePattern;
        this.lastNamePattern = lastNamePattern;
      }
    // END SNIPPET: private-constructor

    // START SNIPPET: clone-constructor
    public PersonFinderImpl2a (@Nonnull final PersonFinderImpl2a other, @Nonnull final Object override)
      {
        super(other, override);
        final PersonFinderImpl2a source = getSource(PersonFinderImpl2a.class, other, override);
        this.persons = source.persons;
        this.firstNamePattern = source.firstNamePattern;
        this.lastNamePattern = source.lastNamePattern;
      }
    // END SNIPPET: clone-constructor

    // START SNIPPET: new-methods
    @Override @Nonnull
    public PersonFinder withFirstName (@Nonnull final String regex)
      {
        return clonedWith(new PersonFinderImpl2a(persons, Pattern.compile(regex), lastNamePattern));
      }

    @Override @Nonnull
    public PersonFinder withLastName (@Nonnull final String regex)
      {
        return clonedWith(new PersonFinderImpl2a(persons, firstNamePattern, Pattern.compile(regex)));
      }
    // END SNIPPET: new-methods

    // START SNIPPET: computeResults
    @Override @Nonnull
    protected List<? extends Person> computeResults()
      {
        return persons.stream()
                      .filter(p -> firstNamePattern.matcher(p.getFirstName()).matches()
                                && lastNamePattern.matcher(p.getLastName()).matches())
                      .collect(Collectors.toList());
      }
    // END SNIPPET: computeResults
  }