/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.Optional;

/***********************************************************************************************************************
 *
 * An extension of {@link As} for Java 8 which makes use of {@link Optional}.
 *
 * @since   3.1-ALPHA-2
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface As8 extends As
  {
    /*******************************************************************************************************************
     *
     * Returns the requested role, or an empty {@link Optional}.
     *
     * @param   <T>     the role type
     * @param   type    the role type
     * @return          the optional role
     *
     ******************************************************************************************************************/
    @Nonnull
    default <T> Optional<T> asOptional (final @Nonnull Class<T> type)
      {
        return Optional.ofNullable(as(type, throwable -> null));
      }
  }