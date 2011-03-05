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
    static interface SpecializedFinder extends Finder<String, SpecializedFinder>
      {
        public SpecializedFinder anExtraMethod();  
      }
    
    static class DefaultSpecializedFinder extends FinderSupport<String, DefaultSpecializedFinder> implements SpecializedFinder
      {
        public DefaultSpecializedFinder() 
          {
            super("foobar");
          }
        
        @Override
        protected List<? extends String> doCompute() 
          {
            throw new UnsupportedOperationException("Not supported yet.");
          }

        public SpecializedFinder anExtraMethod() 
          {
            return this;
          }
      }
    
    static interface SpecializedFinderProvider 
      {
        public SpecializedFinder findSomething();  
      }
    
    public void test1()
      {
        Composite<String, BaseFinder<String>> provider = null;
        List<? extends String> results1 = provider.findChildren().max(10).results();
        List<? extends Integer> results2 = provider.findChildren().ofType(Integer.class).results();
      }
    
    public void test2()
      {
        Composite<String, SpecializedFinder> provider = null;
        List<? extends String> results1 = provider.findChildren().max(10).anExtraMethod().results();
        List<? extends Integer> results2 = provider.findChildren().ofType(Integer.class).results();
      }
  }
