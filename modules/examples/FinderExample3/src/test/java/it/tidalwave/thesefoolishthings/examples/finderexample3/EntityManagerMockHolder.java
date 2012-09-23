/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.finderexample3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class EntityManagerMockHolder 
  {
    public final EntityManager em = mock(EntityManager.class);
    
    private final Query query = mock(Query.class);
    
    public String sqlQuery;
    
    public int firstResult;
    
    public int maxResults;

    public EntityManagerMockHolder() 
      {
        when(em.createQuery(anyString())).thenAnswer(new Answer<Query>() 
          {
            @Nonnull
            public Query answer (final @Nonnull InvocationOnMock invocation)  
              {
                sqlQuery = (String)invocation.getArguments()[0];
                return query;  
              }
          });

        when(query.setFirstResult(anyInt())).thenAnswer(new Answer<Query>() 
          {
            @Nonnull
            public Query answer (final @Nonnull InvocationOnMock invocation)  
              {
                firstResult = (Integer)invocation.getArguments()[0];
                return query;
              }
          });

        when(query.setMaxResults(anyInt())).thenAnswer(new Answer<Query>() 
          {
            @Nonnull
            public Query answer (final @Nonnull InvocationOnMock invocation)  
              {
                maxResults = (Integer)invocation.getArguments()[0];
                return query;
              }
          });
        
        when(query.getResultList()).thenReturn(new ArrayList<String>());
        when(query.getSingleResult()).thenReturn(1);
      }
  }
