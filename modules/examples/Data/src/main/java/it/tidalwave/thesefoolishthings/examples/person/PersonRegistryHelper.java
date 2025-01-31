/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.thesefoolishthings.examples.person;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@UtilityClass
public class PersonRegistryHelper
  {
    private static final List<Person> PEOPLE = List.of(
        new Person("Michelangelo", "Buonarroti"),
        new Person("Lorenzo", "Bernini"),
        new Person("Leonardo", "da Vinci"),
        new Person("Pietro", "Perugino"),
        new Person("Paolo", "Uccello"),
        new Person("Andrea", "Mantegna"),
        new Person("Ambrogio", "Lorenzetti"),
        new Person("Piero", "della Francesca"),
        new Person("Giotto", "da Bondone")
      );

    public static void populate (@Nonnull final Collection<? super Person> collection)
      {
        collection.addAll(PEOPLE);
      }

    public static void populate (@Nonnull final PersonRegistry registry)
      {
        PEOPLE.forEach(registry::add);
      }
  }
