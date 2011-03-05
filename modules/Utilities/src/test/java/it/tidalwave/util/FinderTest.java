/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.util;

import it.tidalwave.role.Composite;
import java.util.List;

/**
 *
 * @author fritz
 */
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
    
    static interface SpecializedFinderProvider 
      {
        public MyFinder findSomething();  
      }
    
    public void test1()
      {
        Composite<String, SimpleFinder<String>> provider = null;
        List<? extends String> results1 = provider.findChildren().max(10).results();
        List<? extends Integer> results2 = provider.findChildren().ofType(Integer.class).results();
      }
    
    public void test2()
      {
        Composite<String, MyFinder> provider = null;
        List<? extends String> results1 = provider.findChildren().max(10).anExtraMethod().results();
        List<? extends Integer> results2 = provider.findChildren().ofType(Integer.class).results();
      }
  }
