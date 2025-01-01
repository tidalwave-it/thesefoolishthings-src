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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.UUID;
import java.io.Serializable;
import it.tidalwave.util.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/***************************************************************************************************************************************************************
 *
 * A simple POJO for examples.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
// START SNIPPET: person
@Immutable @AllArgsConstructor @Getter @EqualsAndHashCode
public class Person implements Serializable // Serializable is not a requirement anyway
  {
    @Nonnull
    public static Person prototype()
      {
        return new Person("", "");
      }

    public Person (@Nonnull final String firstName, @Nonnull final String lastName)
      {
        this(Id.of(UUID.randomUUID().toString()), firstName, lastName);
      }

    final Id id;

    @Nonnull
    final String firstName;

    @Nonnull
    final String lastName;

    @Override @Nonnull
    public String toString()
      {
        return firstName + " " + lastName;
      }
  }
// END SNIPPET: person
