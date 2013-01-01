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
package it.tidalwave.role;

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
public class ContextRunner
  {
    private final static List<Object> globalContexts = new ArrayList<Object>();

    private final static Stack<Object> localContexts = new Stack<Object>();

    private final static ThreadLocal<Object> currentContext = new ThreadLocal<Object>();

    @Nonnull
    public static List<Object> getContexts()
      {
        final List<Object> contexts = new ArrayList<Object>();
        contexts.addAll(localContexts);
        Collections.reverse(contexts);
        contexts.addAll(0, globalContexts);
        return contexts;
      }

    @Nonnull
    public static <T> T findContext (final @Nonnull Class<T> contextClass)
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

    public static void addGlobalContext (final @Nonnull Object context)
      {
        globalContexts.add(context);
      }

    public static <V, T extends Throwable> V runInContext (final @Nonnull Object context,
                                                           final @Nonnull Task<V, T> task)
      throws T
      {
        try
          {
            localContexts.push(context);
            currentContext.set(context);

            return task.run();
          }
        finally
          {
            localContexts.pop();
            currentContext.set(localContexts.isEmpty() ? null : localContexts.peek());
          }
      }
  }
