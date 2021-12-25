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
package it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import it.tidalwave.util.Id;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.role.ContextManager;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.role.XStreamContext1;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.role.XStreamContext2;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import static java.nio.charset.StandardCharsets.UTF_8;
import static it.tidalwave.role.io.Marshallable._Marshallable_;
import static it.tidalwave.role.io.Unmarshallable._Unmarshallable_;

/***********************************************************************************************************************
 *
 * Note: this code uses {@code @ExtensionMethod} from Lombok to enhance datum classes with the
 * {@link it.tidalwave.util.As#as(Class)}, but it is not strictly required.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class) @Slf4j
public class DciMarshalXStreamExample
  {
    @Inject @Nonnull
    private ContextManager contextManager;

    public void run()
            throws IOException
      {
        runWithXStreamContext1();
        runWithXStreamContext2();
      }

    private void runWithXStreamContext1()
            throws IOException
      {
        // START SNIPPET: xstreamcontext-contextmanager
        final XStreamContext1 xStreamContext1 = new XStreamContext1();

        try
          {
            contextManager.addLocalContext(xStreamContext1);
            codeThatUsesMarshalling();
          }
        finally
          {
            contextManager.removeLocalContext(xStreamContext1);
          }
        // END SNIPPET: xstreamcontext-contextmanager
      }

    private void runWithXStreamContext2()
            throws IOException
      {
        // START SNIPPET: xstreamcontext-contextmanager2
        try (final ContextManager.Binder binder = contextManager.binder(new XStreamContext2()))
          {
            codeThatUsesMarshalling();
          }
        // END SNIPPET: xstreamcontext-contextmanager2
      }

    private void alternateSyntax()
            throws IOException
      {
        contextManager.runEWithContexts(this::codeThatUsesMarshalling, new XStreamContext2());
        final String s = contextManager.runEWithContexts(this::codeThatUsesMarshalling2, new XStreamContext2());
        log.info("{}", s);
      }

    private void codeThatUsesMarshalling()
            throws IOException
      {
        // START SNIPPET: xstreamcontext-example1
        final Person joe = new Person(new Id("1"), "Joe", "Smith");
        final Person luke = new Person(new Id("2"), "Luke", "Skywalker");

        final String marshalledPersons;
        final String marshalledPerson;

        try (final ByteArrayOutputStream os = new ByteArrayOutputStream())
          {
            joe.as(_Marshallable_).marshal(os);
            log.info("******** (joe as Marshallable) marshalled: {}\n", marshalledPerson = os.toString(UTF_8));
          }

        try (final ByteArrayOutputStream os = new ByteArrayOutputStream())
          {
            ListOfPersons.of(joe, luke).as(_Marshallable_).marshal(os);
            log.info("******** (listOfPersons as Marshallable) marshalled: {}\n", marshalledPersons = os.toString(UTF_8));
          }
        // END SNIPPET: xstreamcontext-example1

        // START SNIPPET: xstreamcontext-example2
        try (final ByteArrayInputStream is = new ByteArrayInputStream(marshalledPerson.getBytes(UTF_8)))
          {
            final Person person = Person.prototype().as(_Unmarshallable_).unmarshal(is);
            log.info("******** Unmarshalled person: {}\n", person);
          }

        try (final ByteArrayInputStream is = new ByteArrayInputStream(marshalledPersons.getBytes(UTF_8)))
          {
            final ListOfPersons listOfPersons = new ListOfPersons().as(_Unmarshallable_).unmarshal(is);
            log.info("******** Unmarshalled persons: {}\n", listOfPersons);
          }
        // END SNIPPET: xstreamcontext-example2
      }

    @Nonnull
    private String codeThatUsesMarshalling2()
            throws IOException
      {
        codeThatUsesMarshalling();
        return "foo bar";
      }
  }
