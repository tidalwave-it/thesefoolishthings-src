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
package it.tidalwave.actor.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An executor that propagates the {@link Collaboration}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ExecutorWithPriority
  {
    private final Runnable consumer = new Runnable()
      {
        @Override
        public void run()
          {
            for (;;)
              {
                final Runnable runnable = runnableQueue.poll();

                if (runnable == null)
                  {
                    break;
                  }

                runnable.run();
              }
          }
      };

    private static interface PriorityRunnable extends Runnable
      {
      }

    private final BlockingQueue<Runnable> runnableQueue = new LinkedBlockingDeque<Runnable>()
      {
        @Override
        public boolean add (final @Nonnull Runnable runnable)
          {
            if (runnable instanceof PriorityRunnable)
              {
                addFirst(runnable);
              }
            else
              {
                addLast(runnable);
              }

            return true;
          }
      };

    private final Executor executor;

    /*******************************************************************************************************************
     *
     * Creates an executor with the given pool size.
     *
     * @param  poolSize          the pool size
     * @param  name              the thread base name
     * @param  initialPriority   the initial thread priority in this executor
     *
     ******************************************************************************************************************/
    public ExecutorWithPriority (final @Nonnegative int poolSize,
                                 final @Nonnull String name,
                                 final @Nonnegative int initialPriority)
      {
        final ThreadFactory threadFactory = new ThreadFactory()
          {
            private int count = 0;

            @Override @Nonnull
            public Thread newThread (final @Nonnull Runnable runnable)
              {
                final Thread thread = new Thread(runnable, name + "-" + count++);
                thread.setPriority(initialPriority);
                return thread;
              }
          };

        // first parameter should be 0, but in this case it goes monothread
        executor = new ThreadPoolExecutor(poolSize, poolSize, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
//        executor = new ThreadPoolExecutor(poolSize, poolSize, 2, TimeUnit.SECONDS, runnableQueue, threadFactory);
      }

    /*******************************************************************************************************************
     *
     * Schedules the execution of a worker at the end of the queue.
     *
     * @param  worker  the worker
     *
     ******************************************************************************************************************/
    public void execute (final @Nonnull Runnable worker)
      {
        runnableQueue.add(worker);
        executor.execute(consumer);
//        executor.execute(worker);
      }

    /*******************************************************************************************************************
     *
     * Schedules the execution of a worker as soon as possible.
     *
     * @param  worker  the worker
     *
     ******************************************************************************************************************/
    public void executeWithPriority (final @Nonnull Runnable worker)
      {
//        executor.execute(new PriorityRunnable()
        runnableQueue.add(new PriorityRunnable()
          {
            @Override
            public void run()
              {
                worker.run();
              }
          });
        executor.execute(consumer);
      }
  }
