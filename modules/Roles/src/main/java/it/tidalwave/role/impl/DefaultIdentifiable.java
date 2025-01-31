/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.impl;

// import javax.annotation.concurrent.Immutable;
import jakarta.annotation.Nonnull;
import java.io.Serializable;
import it.tidalwave.util.Id;
import it.tidalwave.role.Identifiable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***************************************************************************************************************************************************************
 *
 * A default implementation of {@link Identifiable} which wraps a given id. This class is not part of the public API.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Getter /* @Immutable */  @RequiredArgsConstructor
public class DefaultIdentifiable implements Identifiable, Serializable
  {
    private static final long serialVersionUID = 45654634423793043L;

    @Nonnull
    private final Id id;

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("DefaultIdentifiable@%x[%s]", System.identityHashCode(this), id);
      }
  }
