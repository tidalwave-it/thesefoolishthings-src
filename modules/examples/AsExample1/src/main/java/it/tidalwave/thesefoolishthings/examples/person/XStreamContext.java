/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.person;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.util.Id;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * A DCI local Context that provides an {@link XStream} for a few datum classes.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciContext
public class XStreamContext
  {
    @Getter
    private final XStream xStream = new XStream(new StaxDriver());

    public XStreamContext()
      {
        xStream.aliasField("first-name", Person.class, "firstName");
        xStream.aliasField("last-name", Person.class, "lastName");
        xStream.alias("person", Person.class);
        xStream.alias("persons", ListOfPersons.class);
        xStream.addImplicitCollection(ListOfPersons.class, "persons");

        xStream.useAttributeFor(Person.class, "id");
        xStream.registerConverter(new SingleValueConverter()
          {
            @Override
            public String toString (final Object object)
              {
                return ((Id)object).stringValue();
              }

            @Override
            public Object fromString (final String string)
              {
                return new Id(string);
              }

            @Override
            public boolean canConvert (Class type)
              {
                return type.equals(Id.class);
              }
          });
      }
  }
