/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.role.io;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

/***********************************************************************************************************************
 *
 * The role of an object that can be marshalled.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Marshallable
  {
    public static final Class<Marshallable> _Marshallable_ = Marshallable.class;

    /*******************************************************************************************************************
     *
     * Marshals this object to an {@link OutputStream}.
     *
     * @param    os           the {@code OutputStream}
     * @throws   IOException  in case of errors
     *
     ******************************************************************************************************************/
    public void marshal (@Nonnull OutputStream os)
      throws IOException;
  }
