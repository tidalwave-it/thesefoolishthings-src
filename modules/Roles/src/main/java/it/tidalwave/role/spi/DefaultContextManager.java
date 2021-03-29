/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role
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
import static it.tidalwave.role.spi.impl.LogUtil.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultContextManager implements ContextManager
  {
    /** Useful for troubleshooting in cases when multiple instances are erroneously created. */
    private static final boolean DUMP_STACK_AT_CREATION = Boolean.getBoolean(
            DefaultContextManager.class.getName() + ".dumpStackAtCreation");

    /** The list of global contexts, ordered by priority. */
    private final List<Object> globalContexts = new ArrayList<>();

    /** The list of local contexts, ordered by priority. */
    private final ThreadLocal<Stack<Object>> localContexts = new ThreadLocal<Stack<Object>>()
      {
        @Override @Nonnull
        protected Stack<Object> initialValue()
          {
            return new Stack<>();
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultContextManager()
      {
        if (DUMP_STACK_AT_CREATION)
          {
            try
              {
                throw new RuntimeException();
              }
            catch (Exception e)
              {
                log.trace(">>>> created context manager " + this, e);
              }
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public List<Object> getContexts()
      {
        final List<Object> contexts = new ArrayList<>(localContexts.get());
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
    public <T> T findContextOfType (@Nonnull final Class<T> contextType)
      throws NotFoundException
      {
        for (final Object context : getContexts())
          {
            if (contextType.isAssignableFrom(context.getClass()))
              {
                return contextType.cast(context);
              }
          }

        throw new NotFoundException("No current context of type " + contextType);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void addGlobalContext (@Nonnull final Object context)
      {
        globalContexts.add(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void removeGlobalContext (@Nonnull final Object context)
      {
        globalContexts.remove(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void addLocalContext (@Nonnull final Object context)
      {
        localContexts.get().push(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void removeLocalContext (@Nonnull final Object context)
      {
        localContexts.get().remove(context);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <V, T extends Throwable> V runWithContext (@Nonnull final Object context,
                                                      @Nonnull final Task<V, T> task)
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
    public <V, T extends Throwable> V runWithContexts (@Nonnull final List<Object> contexts,
                                                       @Nonnull final Task<V, T> task)
      throws T
      {
        final String taskId = shortId(task);
        final String contextIds = shortIds(contexts);

        try
          {
            log.trace("runWithContexts({}, {})", contextIds, taskId);

            for (final Object context : contexts)
              {
                addLocalContext(context);
              }

            if (log.isTraceEnabled())
              {
                log.trace(">>>> contexts now: {} - {}", getContexts(), this);
              }

            return task.run();
          }
        finally
          {
            for (final Object context : contexts)
              {
                removeLocalContext(context);
              }

            log.trace(">>>> runWithContexts({}, {}) completed", contextIds, taskId);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <V> V runWithContext (@Nonnull final Object context, @Nonnull final Supplier<V> task)
      {
        return runWithContext(context, new Task<V, RuntimeException>()
          {
            @Override
            public V run()
              {
                return task.get();
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <V> V runWithContexts (@Nonnull final List<Object> contexts, @Nonnull final Supplier<V> task)
      {
        return runWithContext(contexts, new Task<V, RuntimeException>()
          {
            @Override
            public V run()
              {
                return task.get();
              }
          });
      }
  }
