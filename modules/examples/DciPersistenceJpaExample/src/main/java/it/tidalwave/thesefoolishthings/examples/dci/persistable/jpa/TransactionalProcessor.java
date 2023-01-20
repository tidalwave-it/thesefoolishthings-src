/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import it.tidalwave.util.As;
import it.tidalwave.util.AsExtensions;
import it.tidalwave.util.Id;
import it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.role.Findable;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.io.Persistable._Persistable_;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class TransactionalProcessor
  {
    private static final As.Type<Findable<Person>> _FindableOfPersons_ = As.type(Findable.class);

    @Transactional
    public void persistPeople (@Nonnull final Iterable<Person> persons)
      {
        persons.forEach(person ->  person.as(_Persistable_).persist());
      }

//    @Transactional
//    public void removePeople (@Nonnull final Iterable<Person> persons)
//            throws Exception
//      {
//        persons.forEach(_r(person ->  person.as(_Removable_).remove()));
//      }

    @Transactional @Nonnull
    public List<Person> retrievePeople()
      {
        return Person.prototype().as(_FindableOfPersons_).findAll();
      }

    @Transactional @Nonnull
    public Optional<Person> retrievePerson (@Nonnull final Id id)
      {
        return Person.prototype().as(_FindableOfPersons_).findById(id);
      }
  }
