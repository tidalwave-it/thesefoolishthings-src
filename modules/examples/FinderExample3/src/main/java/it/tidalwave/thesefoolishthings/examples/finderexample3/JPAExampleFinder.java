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
package it.tidalwave.thesefoolishthings.examples.finderexample3;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.util.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JPAExampleFinder implements Finder<Person>
  {
    @RequiredArgsConstructor
    public static class JpaqlSortCriterion implements SortCriterion
      {
        @Nonnull
        private final String field;

        public String processSql (@Nonnull final String sql, @Nonnull final SortDirection sortDirection)
          {
            final String orderBy = sql.contains("ORDER BY") ? ", " : " ORDER BY ";
            return sql + orderBy + field + ((sortDirection == SortDirection.DESCENDING) ? " DESC" : "");
          }
      }
    
    public static final SortCriterion BY_FIRST_NAME = new JpaqlSortCriterion("p.firstName");
    public static final SortCriterion BY_LAST_NAME  = new JpaqlSortCriterion("p.lastName");
            
    @Nonnull
    private final EntityManager em;

    @Nonnegative
    private final int firstResult;

    @Nonnegative
    private final int maxResults;

    @Nonnull
    private final String sql;

    @Nonnull
    private final List<Pair<JpaqlSortCriterion, SortDirection>> sortCriteria;


    public JPAExampleFinder (@Nonnull final EntityManager em)
      {
        this(em, 0, Integer.MAX_VALUE, " FROM Person p", new ArrayList<>());
      }

    @Override @Nonnull
    public Finder<Person> from (@Nonnegative final int firstResult)
      {
        return new JPAExampleFinder(em, firstResult, maxResults, sql, sortCriteria);
      }

    @Override @Nonnull
    public Finder<Person> max (@Nonnegative final int maxResults)
      {
        return new JPAExampleFinder(em, firstResult, maxResults, sql, sortCriteria);
      }

    @Override @Nonnull
    public Finder<Person> sort (@Nonnull final SortCriterion criterion)
      {
        return sort(criterion, SortDirection.ASCENDING);
      }

    @Override @Nonnull
    public Finder<Person> sort (@Nonnull final SortCriterion criterion, @Nonnull final SortDirection direction)
      {
        if (!(criterion instanceof JpaqlSortCriterion))
          {
            throw new IllegalArgumentException("Can't sort by " + criterion);
          }

        final List<Pair<JpaqlSortCriterion, SortDirection>> temp = new ArrayList<>(sortCriteria);
        temp.add(Pair.of((JpaqlSortCriterion)criterion, direction));
        return new JPAExampleFinder(em, firstResult, maxResults, sql, temp);
      }

    @Override @Nonnull
    public Optional<Person> optionalResult()
      {
        final List<? extends Person> results = results();

        if (results.size() > 1)
          {
            throw new RuntimeException("More than a single result");
          }

        return (Optional<Person>)results.stream().findFirst();
      }

    @Override @Nonnull
    public Optional<Person> optionalFirstResult()
      {
        return Optional.of(createQuery(Person.class, "SELECT p.firstName").getSingleResult());
      }

    @Override @Nonnull
    public List<? extends Person> results()
      {
        return createQuery(Person.class,"SELECT p.firstName").getResultList();
      }

    @Override @Nonnegative
    public int count()
      {
        return createQuery(Integer.class, "SELECT COUNT(*)").getSingleResult();
      }
    
    @Nonnull
    private <S> TypedQuery<S> createQuery (@Nonnull final Class<S> type, @Nonnull final String jpaqlPrefix)
      {
        final AtomicReference<String> temp = new AtomicReference<>(sql);
        sortCriteria.forEach(p -> temp.set(p.a.processSql(temp.get(), p.b)));
        final String jpaql = jpaqlPrefix + temp.get();
        return em.createQuery(jpaql, type).setMaxResults(maxResults).setFirstResult(firstResult);
      }
  }
