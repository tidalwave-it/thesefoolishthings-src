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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * A DCI local Context that provides an {@link XStream} for a few datum classes.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciContext
public class XStreamContext
  {
    @Getter
    private final XStream xStream = new XStream(new StaxDriver());

    public XStreamContext()
      {
        xStream.alias("person", Person.class);
        xStream.aliasField("first-name", Person.class, "firstName");
        xStream.aliasField("last-name", Person.class, "lastName");
        xStream.useAttributeFor(Person.class, "id");
        xStream.registerConverter(new IdXStreamConverter());

        xStream.alias("persons", ListOfPersons.class);
        xStream.addImplicitCollection(ListOfPersons.class, "persons");
      }
  }
