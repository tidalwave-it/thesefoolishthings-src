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
package it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import it.tidalwave.util.AsExtensions;
import it.tidalwave.util.Id;
import it.tidalwave.role.ContextManager;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.role.XStreamContext1;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.role.XStreamContext2;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.thesefoolishthings.examples.dci.marshal.role.Loadable._Loadable_;
import static it.tidalwave.thesefoolishthings.examples.dci.marshal.role.Savable._Savable_;
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
        var xStreamContext1 = new XStreamContext1();

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
        try (var binder = contextManager.binder(new XStreamContext2()))
          {
            codeThatUsesMarshalling();
          }
        // END SNIPPET: xstreamcontext-contextmanager2
      }

    private void alternateSyntax()
            throws IOException
      {
        contextManager.runEWithContexts(this::codeThatUsesMarshalling, new XStreamContext2());
        var s = contextManager.runEWithContexts(this::codeThatUsesMarshalling2, new XStreamContext2());
        log.info("{}", s);
      }

    private void codeThatUsesMarshalling()
            throws IOException
      {
        var path1 = Path.of("target/Person.xml");
        var path2 = Path.of("target/People.xml");
        // START SNIPPET: xstreamcontext-example1
        var joe = new Person(new Id("1"), "Joe", "Smith");
        var luke = new Person(new Id("2"), "Luke", "Skywalker");

        var marshalledPersons = "";
        var marshalledPerson = "";

        try (var os = new ByteArrayOutputStream())
          {
            joe.as(_Marshallable_).marshal(os);
            log.info("******** (joe as Marshallable) marshalled: {}\n", marshalledPerson = os.toString(UTF_8));
          }

        try (var os = new ByteArrayOutputStream())
          {
            ListOfPersons.of(joe, luke).as(_Marshallable_).marshal(os);
            log.info("******** (listOfPersons as Marshallable) marshalled: {}\n", marshalledPersons = os.toString(UTF_8));
          }
        // END SNIPPET: xstreamcontext-example1

        // START SNIPPET: xstreamcontext-example2
        try (var is = new ByteArrayInputStream(marshalledPerson.getBytes(UTF_8)))
          {
            var person = Person.prototype().as(_Unmarshallable_).unmarshal(is);
            log.info("******** Unmarshalled person: {}\n", person);
          }

        try (var is = new ByteArrayInputStream(marshalledPersons.getBytes(UTF_8)))
          {
            var listOfPersons = ListOfPersons.empty().as(_Unmarshallable_).unmarshal(is);
            log.info("******** Unmarshalled persons: {}\n", listOfPersons);
          }
        // END SNIPPET: xstreamcontext-example2

        // START SNIPPET: xstreamcontext-savable-loadable
        joe.as(_Savable_).saveTo(path1);
        ListOfPersons.of(joe, luke).as(_Savable_).saveTo(path2);
        var p = Person.prototype().as(_Loadable_).loadFrom(path1);
        var lp = ListOfPersons.empty().as(_Loadable_).loadFrom(path2);
        // END SNIPPET: xstreamcontext-savable-loadable
        log.info("******** Loaded person: {}\n", p);
        log.info("******** Loaded persons: {}\n", lp);
      }

    @Nonnull
    private String codeThatUsesMarshalling2()
            throws IOException
      {
        codeThatUsesMarshalling();
        return "foo bar";
      }
  }
