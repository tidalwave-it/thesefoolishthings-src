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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.finderexample3;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.util.NotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JPAExampleFinder implements Finder<String>
  {
    private static class JpaqlSortCriterion implements SortCriterion
      {
        @Nonnull
        private final String field;

        public JpaqlSortCriterion (final @Nonnull String field) 
          {
            this.field = field;
          }
        
        public String processSql (final @Nonnull String sql, final @Nonnull SortDirection sortDirection)
          {
            return sql + " ORDER BY " + field + ((sortDirection == SortDirection.DESCENDING) ? " DESC" : "");  
          }
      }
    
    public static final SortCriterion BY_FIRST_NAME = new JpaqlSortCriterion("p.firstName");
    public static final SortCriterion BY_LAST_NAME  = new JpaqlSortCriterion("p.lastName");
            
    @Nonnull
    private final EntityManager em;
    
    private final int firstResult;
    private final int maxResults;
    private final String sql;

    public JPAExampleFinder (final @Nonnull EntityManager em) 
      {
        this.em = em;
        sql = " FROM Person p";
        firstResult = 0;
        maxResults = Integer.MAX_VALUE;
      }

    @Nonnull
    public Finder<String> from (final @Nonnegative int firstResult) 
      {
        return new JPAExampleFinder(em, firstResult, maxResults, sql);
      }

    @Nonnull
    public Finder<String> max (final @Nonnegative int maxResults) 
      {
        return new JPAExampleFinder(em, firstResult, maxResults, sql);
      }

    public Finder<String> withContext(Object context) 
      {
        throw new UnsupportedOperationException("Not supported.");
      }
    
    @Nonnull
    public <AnotherType> Finder<AnotherType> ofType (final @Nonnull Class<AnotherType> type) 
      {
        throw new UnsupportedOperationException("Not supported.");
      }

    @Nonnull
    public Finder<String> sort (final @Nonnull SortCriterion criterion) 
      {
        return sort(criterion, SortDirection.ASCENDING);
      }

    @Nonnull
    public Finder<String> sort (final @Nonnull SortCriterion criterion, 
                                final @Nonnull SortDirection direction) 
      {
        if (criterion instanceof JpaqlSortCriterion)
          {
            final String newSql = ((JpaqlSortCriterion)criterion).processSql(this.sql, direction);
            return new JPAExampleFinder(em, firstResult, maxResults, newSql);
          }
        else
          {
            throw new IllegalArgumentException("Can't sort by " + criterion);
          }
      }

    @Nonnull
    public String result() 
      throws NotFoundException 
      {
        if (count() > 1)
          {
            throw new RuntimeException("More than one single result");  
          }
        
        return firstResult();
      }

    @Nonnull
    public String firstResult() throws NotFoundException
      {
        return NotFoundException.throwWhenNull((String)createQuery("SELECT p.firstName").getSingleResult(), "");
      }

    @Nonnull
    public List<? extends String> results() 
      {
        return createQuery("SELECT p.firstName").getResultList();
      }

    @Nonnegative
    public int count() 
      {
        return (Integer)createQuery("SELECT COUNT(*)").getSingleResult();
      }
    
    @Nonnull
    private Query createQuery (final @Nonnull String sqlPrefix)
      {
        return em.createQuery(sqlPrefix + sql).setMaxResults(maxResults)
                                              .setFirstResult(firstResult);
        
      }
  }
