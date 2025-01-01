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
package it.tidalwave.thesefoolishthings.examples.dci.marshal.role;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream.converter.IdXStreamConverter;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream.converter.PersonConverter;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.Getter;

/***************************************************************************************************************************************************************
 *
 * A DCI local Context that provides an {@link XStream} for a few datum classes.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
// START SNIPPET: xstreamcontext
@Getter @DciContext
public class XStreamContext2 implements XStreamContext
  {
    private final XStream xStream = new XStream(new StaxDriver());

    public XStreamContext2()
      {
        // xStream.alias("person", PersonConverter.MutablePerson.class);
        xStream.alias("PERSON", Person.class);
        xStream.aliasField("ID", PersonConverter.MutablePerson.class, "id");
        xStream.aliasField("FIRST-NAME", PersonConverter.MutablePerson.class, "firstName");
        xStream.aliasField("LAST-NAME", PersonConverter.MutablePerson.class, "lastName");
        xStream.registerConverter(new IdXStreamConverter());
        xStream.registerConverter(new PersonConverter());

        xStream.alias("PERSONS", ListOfPersons.class);
        xStream.addImplicitCollection(ListOfPersons.class, "persons");

        xStream.addPermission(AnyTypePermission.ANY);
      }
  }
// END SNIPPET: xstreamcontext
