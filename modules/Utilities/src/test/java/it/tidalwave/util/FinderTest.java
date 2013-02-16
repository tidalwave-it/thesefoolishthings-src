/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - Utilities
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.role.Composite;
import it.tidalwave.util.spi.FinderSupport;
import it.tidalwave.util.spi.ExtendedFinderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FinderTest
  {
    private final static List<String> ALL_NAMES = Arrays.asList("");

    static interface NameFinder extends ExtendedFinderSupport<String, NameFinder>
      {
        @Nonnull
        public NameFinder startingWith (@Nonnull String prefix);
      }

    static class NameFinderImplementation extends FinderSupport<String, NameFinderImplementation> implements NameFinder
      {
        private String prefix = "";

        public NameFinderImplementation()
          {
          }

        @Override @Nonnull
        protected List<? extends String> computeResults()
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

    public void test1() // just to see the syntax
      {
        Composite<String, Finder<String>> composite = null;
        List<? extends String> results1 = composite.findChildren().max(10).results();
        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }

    public void test2() // just to see the syntax
      {
        Composite<String, NameFinder> composite = null;
        List<? extends String> results1 = composite.findChildren().from(2).startingWith("A").max(10).results();
        List<? extends Integer> results2 = composite.findChildren().ofType(Integer.class).results();
      }
  }
