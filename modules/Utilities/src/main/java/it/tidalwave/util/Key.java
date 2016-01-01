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
package it.tidalwave.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.Serializable;
import it.tidalwave.util.spi.ReflectionUtils;
import it.tidalwave.util.impl.TypeHolder;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 * @since   1.11.0
 *
 **********************************************************************************************************************/
public final class Key<T> implements StringValue, Comparable<Key<T>>, Serializable
  {
    private static final long serialVersionUID = 2817490298518793579L;

    @Nonnull
    private final String name;

    @Getter @Nonnull
    private final Class<T> type;

    /*******************************************************************************************************************
     *
     * Create a new instance with the given name.
     *
     * @param   name        the name
     *
     ******************************************************************************************************************/
    public Key (final @Nonnull String name)
      {
        this.name = name;
        type = (Class<T>)ReflectionUtils.getTypeArguments(TypeHolder.class, (new TypeHolder<T>() {}).getClass()).get(0);
      }

    /*******************************************************************************************************************
     *
     * Create a new instance with the given name.
     *
     * @param   name        the name
     *
     ******************************************************************************************************************/
    public Key (final @Nonnull StringValue name)
      {
        this(name.stringValue());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String stringValue()
      {
        return name;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public int compareTo (final @Nonnull Key<T> other)
      {
        return this.name.compareTo(other.name);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public boolean equals (final @CheckForNull Object other)
      {
        return (other != null)
               && (other instanceof Key)
               && this.name.equals(((Key)other).name);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public int hashCode()
      {
        return name.hashCode();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("Key[%s]", name);
      }
  }
