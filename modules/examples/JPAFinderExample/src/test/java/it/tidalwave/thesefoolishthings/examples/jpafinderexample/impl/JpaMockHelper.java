/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/thesefoolishthings-examples/it-tidalwave-thesefoolishthings-examples-finderexample3
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class JpaMockHelper
  {
    public final EntityManager em = mock(EntityManager.class);
    
    private final TypedQuery query = mock(TypedQuery.class);
    
    public final TxManager mockTxManager = new TxManager()
      {
        @Override
        public <T> T computeInTx (@Nonnull final Function<EntityManager, T> task)
          {
            return task.apply(em);
          }
      };

    public String sqlQuery;

    public int firstResult;

    public int maxResults;

    public JpaMockHelper ()
      {
        when(em.createQuery(anyString(), any())).thenAnswer(new Answer<TypedQuery<?>>()
          {
            @Nonnull
            public TypedQuery<?> answer (@Nonnull final InvocationOnMock invocation)
              {
                sqlQuery = (String)invocation.getArguments()[0];
                return query;  
              }
          });

        when(query.setFirstResult(anyInt())).thenAnswer(new Answer<Query>()
          {
            @Nonnull
            public TypedQuery<?> answer (@Nonnull final InvocationOnMock invocation)
              {
                firstResult = (Integer)invocation.getArguments()[0];
                return query;
              }
          });

        when(query.setMaxResults(anyInt())).thenAnswer(new Answer<Query>()
          {
            @Nonnull
            public TypedQuery<?> answer (@Nonnull final InvocationOnMock invocation)
              {
                maxResults = (Integer)invocation.getArguments()[0];
                return query;
              }
          });
        
        when(query.getResultList()).thenReturn(new ArrayList<PersonEntity>());
        // in the test class it's used for count()
        when(query.getSingleResult()).thenReturn(1L);
      }
  }
