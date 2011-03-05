/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import java.util.List;
import it.tidalwave.role.Composite;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FinderTest 
  {    
    static interface MyFinder extends Finder<String, MyFinder>
      {
        public MyFinder anExtraMethod();  
      }
    
    static class MyFinderImplementation extends FinderSupport<String, MyFinder> implements MyFinder
      {
        public MyFinderImplementation() 
          {
            super("MyFinderImplementation");
          }
        
        @Override
        protected List<? extends String> doCompute() 
          {
            throw new UnsupportedOperationException("Not supported yet.");
          }

        public MyFinder anExtraMethod() 
          {
            return this;
          }
      }
    
    public void test1()
      {
        Composite<String, SimpleFinder<String>> composite = null;
        List<? extends String> results1 = composite.findChildren().max(10).results();
        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }
    
    public void test2()
      {
        Composite<String, MyFinder> composite = null;
        List<? extends String> results1 = composite.findChildren().max(10).anExtraMethod().results();
        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }
  }
