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
import it.tidalwave.role.Sortable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FinderTest 
  {    
    private final static List<String> ALL_NAMES = Arrays.asList("");
    
    static class SortableCompositeDecorator<T, F> implements Composite<T, Finder<T, F>> 
      {
        @Nonnull
        private final Composite<T, Finder<T, F>> delegate;

        @Nonnull
        private final Sortable sortController;

        public SortableCompositeDecorator (final @Nonnull Composite<T, Finder<T, F>> delegate, 
                                           final @Nonnull Sortable sortController) 
          {
            this.delegate = delegate;
            this.sortController = sortController;
          }
        
        @Override @Nonnull
        public Finder<T, F> findChildren()
          {
            return (Finder<T, F>)delegate.findChildren().sort(sortController.getSortCriterion(), sortController.getSortDirection());
          } 
      }
    
    static interface NameFinder extends Finder<String, NameFinder>
      {
        public NameFinder startingWith (@Nonnull String prefix);  
      }
    
    static class NameFinderImplementation extends FinderSupport<String, NameFinder> implements NameFinder
      {
        private String prefix = "";
        
        public NameFinderImplementation() 
          {
            super("NameFinderImplementation");
          }
        
        @Override @Nonnull
        protected List<? extends String> doCompute() 
          {
            final List<String> results = new ArrayList<String>();
            
            for (final String name : ALL_NAMES)
              {
                if (name.startsWith(prefix))
                  {
                    results.add(name);  
                  }
              }
            
            return results;
          }

        @Nonnull
        public NameFinder startingWith (final @Nonnull String prefix)
          {
            this.prefix = prefix;
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
        Composite<String, NameFinder> composite = null;
        List<? extends String> results1 = composite.findChildren().startingWith("A").max(10).results();
        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }
    
    public void test3()
      {
        Composite<Object, Finder<?, ? extends Finder<?, ?>>> composite1 = null;  
        composite1.findChildren().from(10).max(5).results();
        new SortableCompositeDecorator<?, ?>(composite1);
      }
  }
