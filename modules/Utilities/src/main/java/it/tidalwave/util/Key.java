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
import java.util.Optional;
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
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @since   1.11.0
 * @stereotype flyweight
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(access = AccessLevel.PRIVATE) @EqualsAndHashCode(exclude = "type") @ToString @Slf4j
public class Key<T> implements StringValue, Comparable<Key<?>>, Serializable
  {
    private static final long serialVersionUID = 2817490298518793579L;

    // FIXME: a Set would be enough.
    @VisibleForTesting static final ConcurrentHashMap<String, Key<?>> INSTANCES = new ConcurrentHashMap<>();

    @Getter @Nonnull
    private final String name;

    @Getter @Nonnull
    private Class<T> type;

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
        this.type = (Class<T>)ReflectionUtils.getTypeArguments(Key.class, getClass()).get(0);
      }

    /*******************************************************************************************************************
     *
     * Creates an instance with the given name and type. If an identical key already exists, that existing instance is
     * returned. If a key with the same name but different type exists:
     *
     * <ul>
     *   <li>if the previous type is {@code Object}, it is replaced by the new type; since key instances are
     *   flyweight, this means that all the references previously obtained will see the change;</li>
     *   <li>otherwise an exception is thrown.</li>
     * </ul>
     *
     * We can call this behaviour "lazy typing" of the key. It allows to "pre-register" a key with a name, but unknown
     * type (for instance this could be done when reading from a file where there is no type information) and only later
     * specify the type (when a value associated to that key is going to be processed). For instance, the property
     * unmarshallers of NorthernWind work this way.
     *
     * @param <T>     the static type
     * @param name    the name
     * @param type    the dynamic type
     * @return        the key
     * @throws        IllegalArgumentException  when trying to create a {@code Key} with the same name of an existing
     * instance, but a different type.
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Key<T> of (final @Nonnull String name, final @Nonnull Class<T> type)
      {
        final Key<T> newKey = new Key<T>(name, type);
        final Key<T> key = (Key<T>)INSTANCES.putIfAbsent(name, newKey);

        if ((key != null) && (key.getType() != newKey.getType()))
          {
            if (key.getType() != Object.class)
              {
                final String message = String.format("Can't create %s: previously created with type %s",
                                                     newKey, key.getType().getName());
                throw new IllegalArgumentException(message);
              }

            log.debug("Redefining {} as {}", key, type);
            key.type = type;
          }

        return key != null ? key : newKey;
      }

    /*******************************************************************************************************************
     *
     * Returns a key registered with a given name, if it exists. While the type bound to the key is statically unknown
     * (thus the result is a {@code Key<Object>}), it can be dynamically retrieved by means of {@link #getType()}.
     *
     * @param name    the name
     * @return        the key
     * @since         3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Optional<Key<Object>> named (final @Nonnull String name)
      {
        return Optional.ofNullable((Key<Object>)INSTANCES.get(name));
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
        return this.name.compareTo(other.name);
//        final Comparator<Key<?>> byType = Comparator.comparing(k -> k.getType().getName());
//        final Comparator<Key<?>> byName = Comparator.comparing(Key::getName);
//        return byName.thenComparing(byType).compare(this, other);
      }
  }
