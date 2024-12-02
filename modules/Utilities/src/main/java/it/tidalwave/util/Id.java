/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.UUID;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * An opaque wrapper for identifiers.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor @EqualsAndHashCode
public class Id implements Serializable, Comparable<Id>, StringValue
  {
    private static final long serialVersionUID = 3309234234279593043L;

    @Nonnull
    private final Object value;

    /*******************************************************************************************************************
     *
     * @param value   the id value
     * @return        the new instance
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Id of (@Nonnull final Object value)
      {
        return new Id(value);
      }

    /*******************************************************************************************************************
     *
     * @return        the new instance
     * @since         3.2-ALPHA-9
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Id ofUuid()
      {
        return Id.of(UUID.randomUUID().toString());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String stringValue()
      {
        return (value instanceof StringValue) ? ((StringValue)value).stringValue() : value.toString();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @Override
    public int compareTo (final Id other)
      {
        return stringValue().compareTo(other.stringValue());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return stringValue();
      }
  }
