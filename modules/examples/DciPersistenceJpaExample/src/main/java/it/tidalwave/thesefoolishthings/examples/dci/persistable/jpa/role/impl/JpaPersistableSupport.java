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
package it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.role.impl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import it.tidalwave.util.Id;
import it.tidalwave.role.Removable;
import it.tidalwave.role.io.Persistable;
import it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.role.Findable;
import lombok.AllArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@AllArgsConstructor
public abstract class JpaPersistableSupport<T, E> implements Persistable, Removable, Findable<T>
  {
    @Nonnull
    private final T datum;

    @Nonnull
    private final Class<E> entityClass;

    @Nonnull
    private final Function<E, T> fromEntity;

    @Nonnull
    private final Function<T, E> toEntity;

    @Nonnull
    private final JpaPersistenceContext context;

    @Override
    public void persist()
      {
        context.persist(toEntity.apply(datum));
      }

    @Override
    public void remove()
      {
        context.remove(toEntity.apply(datum));
      }

    @Override @Nonnull
    public List<T> findAll()
      {
        final String query = "SELECT p FROM %s p";
        return context.createQuery(String.format(query, entityClass.getSimpleName()), entityClass)
                      .getResultStream()
                      .map(fromEntity)
                      .collect(Collectors.toList());
      }

    @Override @Nonnull
    public Optional<T> findById (@Nonnull final Id id)
      {
        final String query = "SELECT p FROM %s p WHERE p.id = :id";
        return context.createQuery(String.format(query, entityClass.getSimpleName()), entityClass)
                      .setParameter("id", id.stringValue())
                      .getResultStream()
                      .map(fromEntity)
                      .findFirst();
      }
  }
