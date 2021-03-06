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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Id;

/***********************************************************************************************************************
 *
 * A factory for creating a new, unique {@code Id} for an object.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface IdFactory
  {
    public static final Class<IdFactory> _IdFactory_ = IdFactory.class;

    /*******************************************************************************************************************
     *
     * Creates a new id.
     *
     * @return  the new id
     *
     ******************************************************************************************************************/
    @Nonnull
    public Id createId();

    /*******************************************************************************************************************
     *
     * Creates a new id for an object of the given class.
     *
     * @param   objectClass  the class of the object for which the {@code Id} is created
     * @return               the new id
     *
     ******************************************************************************************************************/
    @Nonnull
    public Id createId (@Nonnull Class<?> objectClass);

    /*******************************************************************************************************************
     *
     * Creates a new id for the given object of the given class. This method allows to explicitly pass a {@code Class}
     * for cases in which the {@code object} implements multiple interfaces and one wants to specify the referenced one.
     *
     * @param   object       the object for which the {@code Id}
     * @param   objectClass  the class of the object for which the {@code Id} is created
     * @return               the new id
     *
     ******************************************************************************************************************/
    @Nonnull
    public Id createId (@Nonnull Class<?> objectClass, @Nonnull Object object);
  }
