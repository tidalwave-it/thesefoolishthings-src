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
package it.tidalwave.thesefoolishthings.examples.asexample2;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import it.tidalwave.util.Id;
import it.tidalwave.role.AsExtensions;
import it.tidalwave.role.ContextManager;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.datum.JpaPersistenceContext;
import lombok.experimental.ExtensionMethod;
import static it.tidalwave.role.Persistable.*;
import static it.tidalwave.role.Removable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class AsExample2 
  {
    @Inject @Nonnull
    private ContextManager contextManager;
    
    @Inject @Nonnull
    private JpaPersistenceContext jpaContext;
    
    public void run()
      throws Exception
      {
        final Person joe = new Person(new Id("1"), "Joe", "Smith");
////        System.err.println(as(joe, Displayable).getDisplayName());
//        System.err.println(joe.as(Displayable).getDisplayName());
        
        contextManager.addLocalContext(jpaContext);
        joe.as(Persistable).persist();
        joe.as(Removable).remove();
        contextManager.removeLocalContext(jpaContext);
        
//        runWithContext(jpaContext, new SimpleTask()
//          {
//            public Void run() 
//              {
////                joe.as(Persistable).persist();
////                joe.as(Removable).remove();
//                return null;
//              }
//          });
      } 
  }
