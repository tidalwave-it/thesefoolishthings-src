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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Id;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.PersonRegistry3;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JpaPersonRegistry implements PersonRegistry3
  {
    @Nonnull
    private final TxManager txManager;

    @Override @Nonnull
    public Finder<Person> findPerson()
      {
        return new JpaPersonFinder(txManager);
      }

    @Override
    public void add (@Nonnull final Person person)
      {
        txManager.runInTx(em -> em.persist(toEntity(person)));
      }

    @Nonnull
    public static PersonEntity toEntity (@Nonnull final Person person)
      {
        return new PersonEntity(person.getId().stringValue(), person.getFirstName(), person.getLastName());
      }

    @Nonnull
    public static Person fromEntity (@Nonnull final PersonEntity entity)
      {
        return new Person(Id.of(entity.getId()), entity.getFirstName(), entity.getLastName());
      }
  }
