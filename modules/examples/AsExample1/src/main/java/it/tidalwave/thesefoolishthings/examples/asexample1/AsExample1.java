/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.asexample1;

import javax.annotation.Nonnull;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.Id;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.Displayable.Displayable;
import static it.tidalwave.role.Marshallable.Marshallable;
import it.tidalwave.thesefoolishthings.examples.person.DefaultPersonRegistry;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;
import java.util.Arrays;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class AsExample1 
  {
    private static Object context;
    
    public static void main (final @Nonnull String ... args)
      throws Exception
      {
        context = new ClassPathXmlApplicationContext("it/tidalwave/thesefoolishthings/examples/asexample1/Beans.xml");
        final Person joe = new Person(new Id("1"), "Joe", "Smith");
        final Person luke = new Person(new Id("2"), "Luke", "Skywalker");
        System.err.println(joe.as(Displayable).getDisplayName());
        
        joe.as(Marshallable).marshal(System.err);
        System.err.println("");
        
        final ListOfPersons listOfPersons = new ListOfPersons(Arrays.asList(joe, luke));
        listOfPersons.as(Marshallable).marshal(System.err);
        System.err.println("");
        
        final DefaultPersonRegistry personRegistry = new DefaultPersonRegistry();
        personRegistry.addPerson(joe);
        personRegistry.addPerson(luke);
        personRegistry.as(Marshallable).marshal(System.err);
        System.err.println("");
      } 
  }
