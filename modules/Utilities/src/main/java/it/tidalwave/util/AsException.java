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
package it.tidalwave.util;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * This exception is thrown by an {@code as(...)} method that is expected to retrieve a role for a given datum, when the
 * role is not found.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public class AsException extends RuntimeException
  {
    /*******************************************************************************************************************
     *
     * Creates an exception.
     *
     * @param  roleType    the type of the role that was searched and not found
     *
     ******************************************************************************************************************/
    public AsException (@Nonnull final Class<?> roleType)
      {
        super(roleType.getName());
      }

//    public AsException (@Nonnull final Throwable t)
//      {
//        super(t);
//      }

    /*******************************************************************************************************************
     *
     * Creates an exception with a cause.
     *
     * @param  roleType    the type of the role that was searched and not found
     * @param  cause       the cause for not having found the role
     *
     ******************************************************************************************************************/
    public AsException (@Nonnull final Class<?> clazz, @Nonnull final Throwable cause)
      {
        super(clazz.getName(), cause);
      }
  }
