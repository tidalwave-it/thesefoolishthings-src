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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class JpaMockHelper
  {
    public final EntityManager em = mock(EntityManager.class);

    @SuppressWarnings("unchecked")
    private final TypedQuery<PersonEntity> query = mock(TypedQuery.class);

    @SuppressWarnings("unchecked")
    private final TypedQuery<Long> queryCount = mock(TypedQuery.class);

    public final TxManager mockTxManager = new TxManager()
      {
        @Override
        public <T> T computeInTx (@Nonnull final Function<? super EntityManager, T> task)
          {
            return task.apply(em);
          }
      };

    @Nonnull
    private final ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);

    @Nonnull
    private final ArgumentCaptor<Integer> firstResultCaptor = ArgumentCaptor.forClass(Integer.class);

    @Nonnull
    private final ArgumentCaptor<Integer> maxResultsCaptor = ArgumentCaptor.forClass(Integer.class);

    private boolean captured = false;

    public JpaMockHelper()
      {
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(queryCount.getSingleResult()).thenReturn(1L);
        when(em.createQuery(anyString(), eq(PersonEntity.class))).thenReturn(query);
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(queryCount);
      }

    @Nonnull
    public String getSql()
      {
        capture();
        return queryCaptor.getValue();
      }

    public Optional<Integer> getFirstResult()
      {
        capture();
        return firstResultCaptor.getAllValues().stream().findFirst();
      }

    public Optional<Integer> getMaxResults()
      {
        capture();
        return maxResultsCaptor.getAllValues().stream().findFirst();
      }

    private void capture()
      {
        if (!captured)
          {
            verify(query, atLeast(0)).setFirstResult(firstResultCaptor.capture());
            verify(query, atLeast(0)).setMaxResults(maxResultsCaptor.capture());
            verify(em, atLeast(0)).createQuery(queryCaptor.capture(), any());
            captured = true;
          }
      }
  }
