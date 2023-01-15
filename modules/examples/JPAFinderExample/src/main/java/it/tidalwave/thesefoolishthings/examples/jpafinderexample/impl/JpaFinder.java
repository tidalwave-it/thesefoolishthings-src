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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Pair;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.util.CollectionUtils.concat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// START SNIPPET: JPAExampleFinder
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Slf4j
public class JpaFinder<T, E> implements Finder<T>
  {
    // START SNIPPET: sort-criterion
    @RequiredArgsConstructor
    static final class JpaqlSortCriterion implements SortCriterion
      {
        @Nonnull
        private final String field;

        @Nonnull
        public String processSql (@Nonnull final String jpaql, @Nonnull final SortDirection sortDirection)
          {
            final String orderBy = jpaql.contains("ORDER BY") ? ", " : " ORDER BY ";
            return jpaql + orderBy + field + ((sortDirection == SortDirection.DESCENDING) ? " DESC" : "");
          }
      }
    
    public static final SortCriterion BY_FIRST_NAME = new JpaqlSortCriterion("p.firstName");
    public static final SortCriterion BY_LAST_NAME  = new JpaqlSortCriterion("p.lastName");
    // END SNIPPET: sort-criterion

    // START SNIPPET: fields
    @Nonnull
    private final Class<E> entityClass;

    @Nonnull
    private final Function<E, T> fromEntity;

    @Nonnull
    private final TxManager txManager;

    @Nonnegative
    private final int firstResult;

    @Nonnegative
    private final int maxResults;

    @Nonnull
    private final List<Pair<JpaqlSortCriterion, SortDirection>> sortCriteria;
    // END SNIPPET: fields

    public JpaFinder (@Nonnull final Class<E> entityClass,
                      @Nonnull final Function<E, T> fromEntity,
                      @Nonnull final TxManager txManager)
      {
        this(entityClass, fromEntity, txManager, 0, Integer.MAX_VALUE, new ArrayList<>());
      }

    // START SNIPPET: intermediate-methods
    @Override @Nonnull
    public Finder<T> from (@Nonnegative final int firstResult)
      {
        return new JpaFinder(entityClass, fromEntity, txManager, firstResult, maxResults, sortCriteria);
      }

    @Override @Nonnull
    public Finder<T> max (@Nonnegative final int maxResults)
      {
        return new JpaFinder(entityClass, fromEntity, txManager, firstResult, maxResults, sortCriteria);
      }
    // END SNIPPET: intermediate-methods

    // START SNIPPET: sort-method
    @Override @Nonnull
    public Finder<T> sort (@Nonnull final SortCriterion criterion, @Nonnull final SortDirection direction)
      {
        if (!(criterion instanceof JpaqlSortCriterion))
          {
            throw new IllegalArgumentException("Can't sort by " + criterion);
          }

        return new JpaFinder(entityClass,
                             fromEntity,
                             txManager,
                             firstResult,
                             maxResults,
                             concat(sortCriteria, Pair.of((JpaqlSortCriterion)criterion, direction)));
      }
    // END SNIPPET: sort-method

    // START SNIPPET: termination-methods
    @Override @Nonnull
    public Optional<T> optionalResult()
      {
        final List<? extends T> results = results();

        if (results.size() > 1)
          {
            throw new RuntimeException("More than a single result");
          }

        return (Optional<T>)results.stream().findFirst();
      }

    @Override @Nonnull
    public Optional<T> optionalFirstResult()
      {
        // Warning: the stream must be consumed *within* runInTx2()
        return txManager.computeInTx(em -> createQuery(em, entityClass, "SELECT p")
                .getResultStream()
                .findFirst()
                .map(fromEntity));
      }

    @Override @Nonnull
    public List<? extends T> results()
      {
        // Warning: the stream must be consumed *within* runInTx2()
        return txManager.computeInTx(em -> createQuery(em, entityClass, "SELECT p")
                .getResultStream()
                .map(fromEntity)
                .collect(Collectors.toList()));
      }

    @Override @Nonnegative
    public int count()
      {
        return txManager.computeInTx(em -> createQuery(em, Long.class, "SELECT COUNT(p)").getSingleResult()).intValue();
      }
    // END SNIPPET: termination-methods

    // START SNIPPET: createQueryFull
    @Nonnull
    private <R> TypedQuery<R> createQuery (@Nonnull final EntityManager em,
                                           @Nonnull final Class<R> resultType,
                                           @Nonnull final String jpaqlPrefix)
      {
        final AtomicReference<String> buffer =
                new AtomicReference<>(jpaqlPrefix + " FROM " + entityClass.getSimpleName() + " p");
        sortCriteria.forEach(p -> buffer.updateAndGet(prev -> p.a.processSql(prev, p.b)));
        final String jpaql = buffer.get();
        log.info(">>>> {}", jpaql);
        // START SNIPPET: createQuery
        return em.createQuery(jpaql, resultType).setFirstResult(firstResult).setMaxResults(maxResults);
        // END SNIPPET: createQuery
      }
    // END SNIPPET: createQueryFull
  }
// END SNIPPET: JPAExampleFinder
