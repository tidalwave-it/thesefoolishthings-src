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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import javax.annotation.Nonnull;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class TxManagerImpl implements TxManager
  {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("example");

    @Override
    public <T> T computeInTx (@Nonnull Function<EntityManager, T> task)
      {
        EntityManager em = null;
        EntityTransaction tx = null;

        try
          {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            final T result = task.apply(em);
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
        finally
          {
            if (em != null)
              {
                em.close();
              }
          }
      }

    @Override
    public void close()
      {
        emf.close();
      }
  }
