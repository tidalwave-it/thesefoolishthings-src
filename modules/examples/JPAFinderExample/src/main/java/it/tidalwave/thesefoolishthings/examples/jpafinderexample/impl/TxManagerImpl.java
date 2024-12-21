/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import javax.annotation.Nonnull;
import java.util.function.Function;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class TxManagerImpl implements TxManager
  {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("example");

    @Override
    public <T> T computeInTx (@Nonnull final Function<? super EntityManager, T> task)
      {
        EntityTransaction tx = null;

        try (final var em = emf.createEntityManager())
          {
            tx = em.getTransaction();
            tx.begin();
            final var result = task.apply(em);
            tx.commit();
            return result;
          }
        catch (Exception e)
          {
            if (tx != null)
              {
                tx.rollback();
              }

            throw new RuntimeException(e);
          }
      }

    @Override
    public void close()
      {
        emf.close();
      }
  }
