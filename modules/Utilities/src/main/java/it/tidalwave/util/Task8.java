/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

/***********************************************************************************************************************
 *
 * Java 8 extensions for {@link Task}
 * 
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @since   3.0
 *
 **********************************************************************************************************************/
public abstract class Task8<T, E extends Throwable> extends Task<T, E>
  {
    /*******************************************************************************************************************
     *
     * Creates a {@code Task} from a {@link Runnable}.
     *
     * @param   runnable    the wrapped object
     * @return              the {@code Task}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Task8<Void, RuntimeException> ofRunnable (final @Nonnull Runnable runnable)
      {
        return new Task8<Void, RuntimeException>()
          {
            @Override
            public Void run()
              {
                runnable.run();
                return null;
              }
          };
      }

    /*******************************************************************************************************************
     *
     * Creates a {@code Task} from a {@link Callable}.
     *
     * @param   callable    the wrapped object
     * @return              the {@code Task}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Task8<T, Exception> ofCallable (final @Nonnull Callable<T> callable)
      {
        return new Task8<T, Exception>()
          {
            @Override
            public T run()
              throws Exception
              {
                return callable.call();
              }
          };
      }

    /*******************************************************************************************************************
     *
     * Creates a {@code Task} from a {@link Callback}.
     *
     * @param   callback    the wrapped object
     * @return              the {@code Task}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Task8<Void, Throwable> ofCallback (final @Nonnull Callback callback)
      {
        return new Task8<Void, Throwable>()
          {
            @Override
            public Void run()
              throws Throwable
              {
                callback.call();
                return null;
              }
          };
      }
  }
