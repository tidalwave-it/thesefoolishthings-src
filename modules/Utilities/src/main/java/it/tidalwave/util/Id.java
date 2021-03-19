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

import javax.annotation.concurrent.Immutable;
import javax.annotation.Nonnull;
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
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Id of (@Nonnull final Object value)
      {
        return new Id(value);
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
