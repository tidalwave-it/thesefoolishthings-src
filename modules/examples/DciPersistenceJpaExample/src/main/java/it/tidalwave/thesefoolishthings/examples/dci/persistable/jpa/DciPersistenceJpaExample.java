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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.role.ContextManager;
import it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.role.impl.JpaPersistenceContext;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.person.PersonRegistryHelper;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class DciPersistenceJpaExample
  {
    @Inject
    private ContextManager contextManager;

    @Inject
    private JpaPersistenceContext jpaPersistenceContext;

    @Inject
    private TransactionalProcessor transactionalProcessor;

    @PostConstruct
    public void run()
            throws Exception
      {
        contextManager.runEWithContexts(this::process, jpaPersistenceContext);
      }

    private void process()
            throws Exception
      {
        final List<Person> people = new ArrayList<>();
        PersonRegistryHelper.populate(people);

        log.info("******** INSERTING PEOPLE...\n");
        transactionalProcessor.persistPeople(people);

        final var qPeople = transactionalProcessor.retrievePeople();
        log.info("******** RETRIEVED PEOPLE {}\n", qPeople);

        final var person = transactionalProcessor.retrievePerson(qPeople.get(1).getId());
        log.info("******** RETRIEVED PERSON {}\n", person.map(Person::toString).orElse("<not found>"));
      }
  }
