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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import it.tidalwave.util.Task;
import it.tidalwave.role.ContextManager;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.spi.LogUtil.*;

/***********************************************************************************************************************
 *
 * A facility that samples the current contexts at creation time and make them available later.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j @ToString(of = "contexts")
public class ContextSampler
  {
    // TODO: should be weak references? Should a context be alive as soon as all the objects created with it are
    // alive?
    private final List<Object> contexts;

    // No @Inject here, we don't want to depend on a DI framework
    private final ContextManager contextManager = ContextManager.Locator.find();

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public ContextSampler (final @Nonnull Object owner)
      {
        contexts = Collections.unmodifiableList(contextManager.getContexts());

        if (log.isTraceEnabled())
          {
            log.trace(">>>> contexts for {} at construction time: {}", shortId(owner), shortIds(contexts));
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<Object> getContexts()
      {
        return new CopyOnWriteArrayList<>(contexts);
      }

    /*******************************************************************************************************************
     *
     * Runs a {@link Task} associated with a the samples contexts.
     *
     * @param  task                the task
     *
     ******************************************************************************************************************/
    public <V, T extends Throwable> V runWithContexts (@Nonnull Task<V, T> task)
      throws T
      {
        return contextManager.runWithContexts(contexts, task);
      }
  }
