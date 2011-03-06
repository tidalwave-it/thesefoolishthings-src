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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.FinderSupport;

/***********************************************************************************************************************
 * 
 * The role of a composite object, that is an object which contains children.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface Composite<Type, SpecializedFinder extends Finder<Type>>
  {
    //@bluebook-begin other
    public static final Class<Composite> Composite = Composite.class;

    static class EmptyFinder extends FinderSupport<Object, EmptyFinder>
      {
        public EmptyFinder() 
          {
          }

        public EmptyFinder (final @Nonnull EmptyFinder prototype)
          {
            super(prototype);
          }

        @Override @Nonnull
        protected List<? extends Object> computeResults() 
          {
            return Collections.emptyList();
          }
      }
    
    /*******************************************************************************************************************
     * 
     * A default <code>Composite</code> with no children.
     *
     ******************************************************************************************************************/
    public final static Composite<Object, Finder<Object>> DEFAULT = new Composite<Object, Finder<Object>>() 
      {
        private final Finder<Object> EMPTY_FINDER = new EmptyFinder();
        
        @Override @Nonnull
        public Finder<Object> findChildren() 
          {
            return EMPTY_FINDER;
          }
      };
    
    //@bluebook-end other
    /*******************************************************************************************************************
     * 
     * Returns the children of this object.
     * 
     * @return  the children
     *
     ******************************************************************************************************************/
    @Nonnull
    public SpecializedFinder findChildren();
  }
