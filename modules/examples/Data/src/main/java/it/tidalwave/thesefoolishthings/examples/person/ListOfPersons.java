/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

/***********************************************************************************************************************
 *
 * A simple implementation of a list of {@code Persons}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// START SNIPPET: listofpersons
@NoArgsConstructor @EqualsAndHashCode
public class ListOfPersons implements List<Person>
  {
    @Delegate
    private final List<Person> persons = new ArrayList<>();

    public static ListOfPersons empty ()
      {
        return new ListOfPersons();
      }

    @Nonnull
    public static ListOfPersons of (@Nonnull final Person ... persons)
      {
        return new ListOfPersons(List.of(persons));
      }

    public ListOfPersons (@Nonnull final List<? extends Person> persons)
      {
        this.persons.addAll(persons);
      }

    @Override @Nonnull
    public String toString()
      {
        return persons.toString();
      }
  }
// END SNIPPET: listofpersons
