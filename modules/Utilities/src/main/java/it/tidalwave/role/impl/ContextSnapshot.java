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

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.util.ContextManager;
import it.tidalwave.util.Task;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.util.ShortNames.*;

/***************************************************************************************************************************************************************
 *
 * A facility that takes a snapshot of the contexts that are current at creation time and make them available later.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Slf4j
public class ContextSnapshot
  {
    private final ContextManager contextManager = ContextManager.getInstance();

    // TODO: should be weak references? Should a context be alive as soon as all the objects created with it are
    // alive?
    private final List<Object> contexts = Collections.unmodifiableList(contextManager.getContexts());

    /***********************************************************************************************************************************************************
     * Creates a new instance and samples the currently available contexts.
     *
     * @param owner     the owner
     **********************************************************************************************************************************************************/
    public ContextSnapshot (@Nonnull final Object owner)
      {
        if (log.isTraceEnabled())
          {
            log.trace(">>>> contexts for {} at construction time: {}", shortId(owner), shortIds(contexts));
          }
      }

    /***********************************************************************************************************************************************************
     * Returns the previously sampled contexts.
     *
     * @return    the contexts
     **********************************************************************************************************************************************************/
    @Nonnull
    public List<Object> getContexts()
      {
        return new CopyOnWriteArrayList<>(contexts);
      }

    /***********************************************************************************************************************************************************
     * Runs a {@link Task} associated with the sampled contexts.
     *
     * @param  <V>      the type of the result value
     * @param  <T>      the type of the exception
     * @param  task     the task
     * @return          the value produced by the task
     * @throws T        the exception(s) thrown by the task
     **********************************************************************************************************************************************************/
    public <V, T extends Throwable> V runWithContexts (@Nonnull final Task<V, T> task)
      throws T
      {
        return contextManager.runWithContexts(contexts, task);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("ContextSnapshot%s", shortIds(contexts));
      }
  }
