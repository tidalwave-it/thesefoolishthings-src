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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * A class which encapsulates a task..
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 **********************************************************************************************************************/
public abstract class Task<T, E extends Throwable>
  {
    @Nonnull
    private final String name;

    /*******************************************************************************************************************
     *
     * Creates a new {@code Task}.
     *
     ******************************************************************************************************************/
    public Task()
      {
        name = String.format("%s@%x", getClass().getName(), System.identityHashCode(this));
      }

    /*******************************************************************************************************************
     *
     * Creates a new {@code Task} with the given name.
     *
     * @param  name  the name
     *
     ******************************************************************************************************************/
    public Task (final @Nonnull String name)
      {
        this.name = String.format("Task@%x[%s]", System.identityHashCode(this), name);
      }

    /*******************************************************************************************************************
     *
     * The method that must contain the body of the {@code Task}.
     *
     ******************************************************************************************************************/
    public abstract T run()
      throws E;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return name;
      }
  }

