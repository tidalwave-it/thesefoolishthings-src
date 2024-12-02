/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;
import it.tidalwave.util.LazySupplier;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl.TxManagerImpl;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface TxManager extends AutoCloseable
  {
    static class __ // TODO: Turn to interface constant with Java 11
      {
        static final LazySupplier<TxManager> TXMANAGER_REF = LazySupplier.of(TxManagerImpl::new);
      }

    @Nonnull
    public static TxManager getInstance()
      {
        return __.TXMANAGER_REF.get();
      }

    // START SNIPPET: methods
    public <T> T computeInTx (@Nonnull Function<? super EntityManager, T> task);

    public default void runInTx (@Nonnull final Consumer<? super EntityManager> task)
    // END SNIPPET: methods
      {
        computeInTx(em ->
          {
            task.accept(em);
            return null;
          });
      }

    public default void close()
      {
      }
  }
