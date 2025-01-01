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
package it.tidalwave.message;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Map;
import it.tidalwave.util.Key;
import it.tidalwave.util.TypeSafeMap;
import lombok.Getter;
import lombok.ToString;

/***************************************************************************************************************************************************************
 *
 * A message that notifies that the system has been just powered on.
 *
 * @stereotype  Message
 * @since       3.2-ALPHA-25
 * @author      Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Immutable @Getter @ToString
public final class PowerOnEvent
  {
    @Nonnull
    private final TypeSafeMap properties;

    /***********************************************************************************************************************************************************
     * Creates a new instance.
     **********************************************************************************************************************************************************/
    public PowerOnEvent ()
      {
        this(Map.of());
      }

    /***********************************************************************************************************************************************************
     * Creates a new instance with a map of properties.
     * @param  properties   the properties
     **********************************************************************************************************************************************************/
    public PowerOnEvent (@Nonnull final Map<Key<?>, Object> properties)
      {
        this.properties = TypeSafeMap.ofCloned(properties);
      }
  }
