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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Pair;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.TxManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// START SNIPPET: JPAExampleFinder
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Slf4j
public class JpaPersonFinder implements Finder<Person>
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
    private final TxManager txManager;

    @Nonnegative
    private final int firstResult;

    @Nonnegative
    private final int maxResults;

    @Nonnull
    private final List<Pair<JpaqlSortCriterion, SortDirection>> sortCriteria;
    // END SNIPPET: fields

    public JpaPersonFinder (@Nonnull final TxManager txManager)
      {
        this(txManager, 0, Integer.MAX_VALUE, new ArrayList<>());
      }

    // START SNIPPET: intermediate-methods
    @Override @Nonnull
    public Finder<Person> from (@Nonnegative final int firstResult)
      {
        return new JpaPersonFinder(txManager, firstResult, maxResults, sortCriteria);
      }

    @Override @Nonnull
    public Finder<Person> max (@Nonnegative final int maxResults)
      {
        return new JpaPersonFinder(txManager, firstResult, maxResults, sortCriteria);
      }
    // END SNIPPET: intermediate-methods

    // START SNIPPET: sort-method
    @Override @Nonnull
    public Finder<Person> sort (@Nonnull final SortCriterion criterion, @Nonnull final SortDirection direction)
      {
        if (!(criterion instanceof JpaqlSortCriterion))
          {
            throw new IllegalArgumentException("Can't sort by " + criterion);
          }

        return new JpaPersonFinder(txManager,
                                   firstResult,
                                   maxResults,
                                   concat(sortCriteria, Pair.of((JpaqlSortCriterion)criterion, direction)));
      }
    // END SNIPPET: sort-method

    // START SNIPPET: termination-methods
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
        // Warning: the stream must be consumed *within* runInTx2()
        return txManager.computeInTx(em -> createQuery(em, PersonEntity.class, "SELECT p")
                .getResultStream()
                .findFirst()
                .map(JpaPersonRegistry::fromEntity));
      }

    @Override @Nonnull
    public List<? extends Person> results()
      {
        // Warning: the stream must be consumed *within* runInTx2()
        return txManager.computeInTx(em -> createQuery(em, PersonEntity.class, "SELECT p")
                .getResultStream()
                .map(JpaPersonRegistry::fromEntity)
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
    private <S> TypedQuery<S> createQuery (@Nonnull final EntityManager em,
                                           @Nonnull final Class<S> type,
                                           @Nonnull final String jpaqlPrefix)
      {
        final AtomicReference<String> temp = new AtomicReference<>(jpaqlPrefix + " FROM PersonEntity p");
        sortCriteria.forEach(p -> temp.set(p.a.processSql(temp.get(), p.b)));
        final String jpaql = temp.get();
        log.info(">>>> {}", jpaql);
        return
                // START SNIPPET: createQuery
                em.createQuery(jpaql, type).setFirstResult(firstResult).setMaxResults(maxResults);
                // END SNIPPET: createQuery
      }
    // END SNIPPET: createQueryFull

    @Nonnull
    private static <X> List<X> concat (@Nonnull final List<X> list, @Nonnull final X object)
      {
        final List<X> temp = new ArrayList<>(list);
        temp.add(object);
        return temp;
      }
  }
// END SNIPPET: JPAExampleFinder
