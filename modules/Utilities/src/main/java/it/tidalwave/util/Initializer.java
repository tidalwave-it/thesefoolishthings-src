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
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.io.Serializable;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **************************************************************************************************************************************************************/
@FunctionalInterface
public interface Initializer<T>
  {
    /***********************************************************************************************************************************************************
     * @param object    an argument
     * @return          the argument
     **********************************************************************************************************************************************************/
    public T initialize (@Nonnull T object);

    /***********************************************************************************************************************************************************
     * @param <T>       the static type
     * @return          an empty initializer
     * @since           3.2-ALPHA-2
     **********************************************************************************************************************************************************/
    @Nonnull
    public static <T> Initializer<T> empty()
      {
        return new EmptyInitializer<>();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    static final class EmptyInitializer<K> implements Initializer<K>, Serializable
      {
        private static final long serialVersionUID = 6039459583930596L;

        @Override @Nonnull
        public K initialize (@Nonnull final K entity)
          {
            return entity;
          }
      }
  }
