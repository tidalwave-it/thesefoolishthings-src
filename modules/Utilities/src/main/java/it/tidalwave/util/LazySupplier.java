/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Supplier;
import it.tidalwave.util.annotation.VisibleForTesting;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * A supplier of an object that is lazily evaluated (when its value is requested for the first time). It warranties
 * that the real wrapped supplier is called only once.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-13
 *
 **********************************************************************************************************************/
@ThreadSafe @RequiredArgsConstructor(staticName = "of")
public class LazySupplier<T> implements Supplier<T>
  {
    @Nonnull
    private final Supplier<T> supplier;

    @VisibleForTesting volatile T ref = null;

    /** {@inheritDoc} */
    @Override @Nonnull
    public synchronized T get()
      {
        // AtomicReference.updateAndGet() not good because in case of contention it might call supplier multiple times
        // We don't mess with double check for null since nowadays synchronized is fast
        if (ref == null)
          {
            ref = supplier.get();
          }

        return ref;
      }

    public synchronized void clear()
      {
        ref = null;
      }

    public void set (@Nonnull final T ref)
      {
        this.ref = ref;
      }
  }
