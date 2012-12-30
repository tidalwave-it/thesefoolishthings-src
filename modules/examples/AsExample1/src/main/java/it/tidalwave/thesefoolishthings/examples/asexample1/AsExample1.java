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
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.Displayable.Displayable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class AsExample1 
  {
    public static void main (final @Nonnull String ... args)
      {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("it/tidalwave/thesefoolishthings/examples/asexample1/Beans.xml");
        final Person joe = new Person("Joe", "Smith");
        System.err.println(joe.as(Displayable).getDisplayName());
      } 
  }
