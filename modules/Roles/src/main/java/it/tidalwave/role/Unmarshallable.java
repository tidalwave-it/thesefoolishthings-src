/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.IOException;

/***********************************************************************************************************************
 *
 * The role of an object that can be unmarshalled.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Unmarshallable
  {
    public static final Class<Unmarshallable> Unmarshallable = Unmarshallable.class;

    /*******************************************************************************************************************
     *
     * Unmarshals the object from the given {@link InputStream}.
     *
     * @param   is             the {@code InputStream}
     * @return                 the unmarshalled object
     * @throws  IOException    when an error occurs
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T unmarshal (@Nonnull InputStream is)
      throws IOException;
  }
