/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role.spring;

import it.tidalwave.role.*;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.Task;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SpringContextManager implements ContextManager
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

    @Override @Nonnull
    public List<Object> getContexts()
      {
        final List<Object> contexts = new ArrayList<Object>();
        contexts.addAll(localContexts.get());
        Collections.reverse(contexts);
        contexts.addAll(0, globalContexts);
        return contexts;
      }

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

    @Override
    public void addGlobalContext (final @Nonnull Object context)
      {
        globalContexts.add(context);
      }

    @Override
    public void addLocalContext (final @Nonnull Object context)
      {
        localContexts.get().push(context);
      }

    @Override
    public void removeLocalContext (final @Nonnull Object context)
      {
        localContexts.get().remove(context);
      }

    @Override @Nonnull
    public <V, T extends Throwable> V runWithContext (final @Nonnull Object context,
                                                      final @Nonnull Task<V, T> task)
      throws T
      {
        try
          {
            addLocalContext(context);
            return task.run();
          }
        finally
          {
            removeLocalContext(context);
          }
      }
  }
