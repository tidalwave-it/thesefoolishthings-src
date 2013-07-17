/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.Task;
import it.tidalwave.role.ContextManager;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultContextManager implements ContextManager
  {
    /** The list of global contexts, ordered by priority. */
    private final List<Object> globalContexts = new ArrayList<Object>();

    /** The list of local contexts, ordered by priority. */
    private final ThreadLocal<Stack<Object>> localContexts = new ThreadLocal<Stack<Object>>()
      {
        @Override @Nonnull
        protected Stack<Object> initialValue()
          {
            return new Stack<Object>();
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public List<Object> getContexts()
      {
        final List<Object> contexts = new ArrayList<Object>();
        contexts.addAll(localContexts.get());
        Collections.reverse(contexts);
        contexts.addAll(0, globalContexts);
        return contexts;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> T findContext (final @Nonnull Class<T> contextClass)
      throws NotFoundException
      {
        for (final Object context : getContexts())
          {
            if (contextClass.isAssignableFrom(context.getClass()))
              {
                return contextClass.cast(context);
              }
          }

        throw new NotFoundException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void addGlobalContext (final @Nonnull Object context)
      {
        globalContexts.add(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void removeGlobalContext (final @Nonnull Object context)
      {
        globalContexts.remove(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void addLocalContext (final @Nonnull Object context)
      {
        localContexts.get().push(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void removeLocalContext (final @Nonnull Object context)
      {
        localContexts.get().remove(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <V, T extends Throwable> V runWithContext (final @Nonnull Object context,
                                                      final @Nonnull Task<V, T> task)
      throws T
      {
        return runWithContexts(Collections.singletonList(context), task);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <V, T extends Throwable> V runWithContexts (final @Nonnull List<Object> contexts,
                                                       final @Nonnull Task<V, T> task)
      throws T
      {
        try
          {
            log.trace("runWithContexts({}, {})", contexts, task);

            for (final Object context : contexts)
              {
                addLocalContext(context);
              }

            return task.run();
          }
        finally
          {
            for (final Object context : contexts)
              {
                removeLocalContext(context);
              }

            log.trace(">>>> runWithContexts({}, {}) completed", contexts, task);
          }
      }
  }
