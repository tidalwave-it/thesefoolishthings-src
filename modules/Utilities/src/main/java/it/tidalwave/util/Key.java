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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import it.tidalwave.util.annotation.VisibleForTesting;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @since   1.11.0
 * @stereotype flyweight
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(access = AccessLevel.PRIVATE) @EqualsAndHashCode @ToString
public class Key<T> implements StringValue, Comparable<Key<?>>, Serializable
  {
    private static final long serialVersionUID = 2817490298518793579L;

    // FIXME: a Set would be enough.
    @VisibleForTesting static final ConcurrentHashMap<Key<?>, Key<?>> INSTANCES = new ConcurrentHashMap<>();

    @Getter @Nonnull
    private final String name;

    @Getter @Nonnull
    private final Class<T> type;

    /*******************************************************************************************************************
     *
     * Create a new instance with the given name.
     *
     * @param   name        the name
     * @deprecated use {@link #of(String, Class)}
     *
     ******************************************************************************************************************/
    @Deprecated
    public Key (final @Nonnull String name)
      {
        this.name = name;
        type = (Class<T>)ReflectionUtils.getTypeArguments(Key.class, getClass()).get(0);
      }

    /*******************************************************************************************************************
     *
     * Creates an instance with the given name and type. If an identical key already exists, that existing instance is
     * returned. It is allowed to have two keys with the same name and different types (e.g. {@code Key.of("foo",
     * String.class)} and {@code Key.of("foo", Integer.class)}: they are considered as two distinct keys.
     *
     * @param <T>     the static type
     * @param name    the name
     * @param type    the dynamic type
     * @return        the key
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Key<T> of (final @Nonnull String name, final @Nonnull Class<T> type)
      {
        final Key<T> newKey = new Key<T>(name, type);
        final Key<T> key = (Key<T>)INSTANCES.putIfAbsent(newKey, newKey);
        return key != null ? key : newKey;
      }

    /*******************************************************************************************************************
     *
     * Returns all the keys registered in the system.
     *
     * @return    a mutable and sorted set of keys.
     * @since     3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Set<Key<?>> allKeys()
      {
        return new TreeSet<Key<?>>(INSTANCES.values());
      }

    /*******************************************************************************************************************
     *
     * Create a new instance with the given name.
     *
     * @param   name        the name
     * @deprecated use {@link #of(String, Class)}
     *
     ******************************************************************************************************************/
    public Key (final @Nonnull StringValue name)
      {
        this(name.stringValue());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String stringValue()
      {
        return name;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public int compareTo (final @Nonnull Key<?> other)
      {
        final Comparator<Key<?>> byType = Comparator.comparing(k -> k.getType().getName());
        final Comparator<Key<?>> byName = Comparator.comparing(Key::getName);
        return byName.thenComparing(byType).compare(this, other);
      }
  }
