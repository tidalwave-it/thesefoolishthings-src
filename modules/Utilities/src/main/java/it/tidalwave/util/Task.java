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
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.util.concurrent.Callable;

/***************************************************************************************************************************************************************
 *
 * A class which encapsulates a task.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.experimental
 *
 **************************************************************************************************************************************************************/
@SuppressWarnings("this-escape")
public abstract class Task<T, E extends Throwable>
  {
    @Nonnull
    private final String name;

    /***********************************************************************************************************************************************************
     * Creates a new {@code Task}.
     **********************************************************************************************************************************************************/
    public Task()
      {
        name = String.format("%s@%x", getClass().getName(), System.identityHashCode(this));
      }

    /***********************************************************************************************************************************************************
     * Creates a new {@code Task} with the given name.
     *
     * @param  name         the name
     **********************************************************************************************************************************************************/
    public Task (@Nonnull final String name)
      {
        this.name = String.format("Task@%x[%s]", System.identityHashCode(this), name);
      }

    /***********************************************************************************************************************************************************
     * The method that must contain the body of the {@code Task}.
     *
     * @return              the computed value
     * @throws  E           in case of error
     **********************************************************************************************************************************************************/
    public abstract T run()
      throws E;

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return name;
      }

    /***********************************************************************************************************************************************************
     * Creates a {@code Task} from a {@link Runnable}.
     *
     * @param   runnable    the wrapped object
     * @return              the {@code Task}
     * @since               3.2-ALPHA-1 (was previously on {@code Task8}
     **********************************************************************************************************************************************************/
    @Nonnull
    public static Task<Void, RuntimeException> ofRunnable (@Nonnull final Runnable runnable)
      {
        return new Task<>()
          {
            @Override
            public Void run ()
              {
                runnable.run();
                return null;
              }
          };
      }

    /***********************************************************************************************************************************************************
     * Creates a {@code Task} from a {@link Callable}.
     *
     * @param <T>           the return type of the callable
     * @param   callable    the wrapped object
     * @return              the {@code Task}
     * @since               3.2-ALPHA-1 (was previously on {@code Task8}
     **********************************************************************************************************************************************************/
    @Nonnull
    public static <T> Task<T, Exception> ofCallable (@Nonnull final Callable<? extends T> callable)
      {
        return new Task<>()
          {
            @Override
            public T run ()
                    throws Exception
              {
                return callable.call();
              }
          };
      }

    /***********************************************************************************************************************************************************
     * Creates a {@code Task} from a {@link Callback}.
     *
     * @param   callback    the wrapped object
     * @return              the {@code Task}
     * @since               3.2-ALPHA-1 (was previously on {@code Task8}
     **********************************************************************************************************************************************************/
    @Nonnull
    public static Task<Void, Throwable> ofCallback (@Nonnull final Callback callback)
      {
        return new Task<>()
          {
            @Override
            public Void run ()
                    throws Throwable
              {
                callback.call();
                return null;
              }
          };
      }
  }

