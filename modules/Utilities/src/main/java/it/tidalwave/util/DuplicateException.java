/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * Notifies that the current operation failed because it would create an illegal duplication (of objects, of ids,
 * etc...)
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public class DuplicateException extends Exception
  {
    private static final long serialVersionUID = 12560685435629394L;

    /*******************************************************************************************************************
     *
     * Creates a messageless exception.
     *
     ******************************************************************************************************************/
    public DuplicateException()
      {
      }

    /*******************************************************************************************************************
     *
     * Creates an exception with a message.
     *
     * @param  message  the message
     *
     ******************************************************************************************************************/
    public DuplicateException (@Nonnull final String message)
      {
        super(message);
      }
}
