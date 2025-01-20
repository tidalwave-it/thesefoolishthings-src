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

// import javax.annotation.concurrent.ThreadSafe;
import jakarta.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import it.tidalwave.util.annotation.VisibleForTesting;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***************************************************************************************************************************************************************
 *
 * A supplier of an object that is lazily evaluated (when its value is requested for the first time). It warranties
 * that the real wrapped supplier is called only once.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-13
 *
 **************************************************************************************************************************************************************/
/* @ThreadSafe */ @RequiredArgsConstructor(staticName = "of") @EqualsAndHashCode(of = {"ref", "initialized"})
public class LazySupplier<T> implements Supplier<T>
  {
    @Nonnull
    private final Supplier<T> supplier;

    @VisibleForTesting final AtomicReference<T> ref = new AtomicReference<>(null);

    @Getter
    @VisibleForTesting final AtomicBoolean initialized = new AtomicBoolean(false);

    /** {@inheritDoc} */
    @Override @Nonnull
    public synchronized T get()
      {
        // AtomicReference.updateAndGet() not good because in case of contention it might call supplier multiple times
        // We don't mess with double check for null since nowadays synchronized is fast
        if (ref.get() == null)
          {
            ref.set(supplier.get());
            initialized.set(true);
          }

        return ref.get();
      }

    public synchronized void clear()
      {
        ref.set(null);
      }

    public synchronized void set (@Nonnull final T ref)
      {
        this.ref.set(ref);
      }

    @Nonnull
    public String toString()
      {
        return String.format("LazySupplier(%s)", (ref.get() == null && !initialized.get()) ? "<not initialised>" : ref.get());
      }
  }
