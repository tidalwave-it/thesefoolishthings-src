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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample;

import javax.annotation.Nonnull;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsExtensions;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl.JpaPersonRegistry;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.role.Findable;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistryHelper;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.thesefoolishthings.examples.jpafinderexample.PersonRegistry3.*;
import static it.tidalwave.thesefoolishthings.examples.jpafinderexample.role.Findable._Findable_;
import static it.tidalwave.util.Finder.SortDirection.DESCENDING;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
@Slf4j
public class Main
  {
    private static final As.Type<Findable<Person>> _Findable_of_Person_ = As.type(_Findable_);

    public static void main (@Nonnull final String... args)
            throws Exception
      {
        try (final TxManager txManager = TxManager.getInstance())
          {
            final PersonRegistry3 registry = new JpaPersonRegistry(txManager);
            PersonRegistryHelper.populate(registry);

            final List<? extends Person> p1 = registry.findPerson().results();
            log.info("******** All: {}", p1);

            final int n1 = registry.findPerson().count();
            log.info("******** Count: {}",  n1);

            final List<? extends Person> p2 = registry.findPerson().from(3).max(2).results();
            log.info("******** wo persons from the 3rd position: {}", p2);

            final List<? extends Person> p3 = registry.findPerson().sort(BY_FIRST_NAME).results();
            log.info("******** All, sorted by first name: {}", p3);

            final List<? extends Person> p4 = registry.findPerson().sort(BY_LAST_NAME, DESCENDING).results();
            log.info("******** All, sorted by last name, descending: {}", p4);

            final List<? extends Person> p5 =
                    Person.prototype().as(_Findable_of_Person_).find()
                          .sort(BY_LAST_NAME, DESCENDING)
                          .from(2)
                          .max(3)
                          .results();
            log.info("******** Three persons from the 2nd position, with Person.as(): {}", p5);
          }
      }
  }
