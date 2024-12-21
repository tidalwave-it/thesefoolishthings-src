/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Id;
import it.tidalwave.role.impl.DefaultIdentifiable;

/***************************************************************************************************************************************************************
 *
 * The role of an object that can expose a unique identifier.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **************************************************************************************************************************************************************/
@FunctionalInterface
public interface Identifiable
  {
    public static final Class<Identifiable> _Identifiable_ = Identifiable.class;

    /***********************************************************************************************************************************************************
     * Returns the identifier.
     *
     * @return              the id
     **********************************************************************************************************************************************************/
    @Nonnull
    public Id getId();

    /***********************************************************************************************************************************************************
     * Returns a default instance which returns the given idr.
     *
     * @param         id    the id
     * @return              the identifiable
     * @since               3.2-ALPHA-6
     **********************************************************************************************************************************************************/
    @Nonnull
    public static Identifiable of (@Nonnull final Id id)
      {
        return new DefaultIdentifiable(id);
      }
  }
