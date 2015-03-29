/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role;

/***********************************************************************************************************************
 *
 * The role of an object that can be removed (e.g. deleted).
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface Removable
  {
    public final static Class<Removable> Removable = Removable.class;

    /*******************************************************************************************************************
     *
     * A default {@code Removable} which does nothing (useful for implementing the NullObject pattern).
     *
     ******************************************************************************************************************/
    public final static Removable DEFAULT = new Removable()
      {
        public void remove()
          {
          }
      };

    /*******************************************************************************************************************
     *
     * Removes this object from some implicit, or context-provided container (e.g. a persistnce facility, etc...).
     *
     * @throws  Exception   if the operation fails
     *
     ******************************************************************************************************************/
    public void remove()
      throws Exception;
  }
