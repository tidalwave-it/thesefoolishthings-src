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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.util.impl.DefaultAs;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * Objects implementing this interface can provide am adapter of the required type. The adapter can be found with a
 * variety of approaches that depend on the implementation. This capability can be used to implement a design based
 * on the Data, Context and Interaction pattern (DCI).
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface As
  {
    /*******************************************************************************************************************
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Deprecated
    public static interface NotFoundBehaviour<T>
      {
        @Nonnull
        public T run (@Nullable final Throwable t);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Deprecated
    public static final class Defaults
      {
        private Defaults()
          {
          }

        public static <X> NotFoundBehaviour<X> throwAsException (@Nonnull final Class<X> clazz)
          {
            return new NotFoundBehaviour<X>()
              {
//                @Override
                @Nonnull
                public X run (@Nonnull final Throwable t)
                  {
                    throw new AsException(clazz, t);
                  }
              };
          }
      }

    /*******************************************************************************************************************
     *
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static final class Ref<T>
      {
        @Nonnull
        private final Class<?> type;

        @Nonnull
        public Class<T> getType()
          {
            return (Class<T>)type;
          }
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation for the given object (or returns the object itself if it is the default
     * implementation of {@code As}).
     *
     * @param     object         the object
     * @return                   the implementation
     * @since     3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public static As forObject (@Nonnull final Object object)
      {
        return (object instanceof DefaultAs) ? (As)object : new DefaultAs(object);
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation for the given object. It accepts a single pre-instantiated role, or a
     * {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param     object         the object
     * @param     role           the role or {@link it.tidalwave.util.RoleFactory}
     * @return                   the implementation
     * @since     3.2-ALPHA-13
     *
     ******************************************************************************************************************/
    @Nonnull
    public static As forObject (@Nonnull final Object object, @Nonnull final Object role)
      {
        return new DefaultAs(object, role);
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation for the given object. It accepts a collection of pre-instantiated roles,
     * or instances of {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param     object         the object
     * @param     roles          roles or {@link it.tidalwave.util.RoleFactory} instances
     * @return                   the implementation
     * @since     3.2-ALPHA-13
     *
     ******************************************************************************************************************/
    @Nonnull
    public static As forObject (@Nonnull final Object object, @Nonnull final Collection<Object> roles)
      {
        return new DefaultAs(object, roles);
      }

    /*******************************************************************************************************************
     *
     * Returns an adapter to this object of the specified type. If the implementation can find multiple compliant
     * adapters, only one will be returned.
     *
     * @param   <T>     the static type
     * @param   type    the dynamic type
     * @return          the adapter
     * @throws          AsException if no adapter is found
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> T as (@Nonnull final Class<T> type)
      {
        return maybeAs(type).orElseThrow(() -> new AsException(type));
      }

    /*******************************************************************************************************************
     *
     * Returns an adapter to this object of the specified type. If the implementation can find multiple compliant
     * adapters, only one will be returned. If no adapter is found, the result provided by the given default
     * behaviour will be returned.
     *
     * @param   <T>                 the static type
     * @param   type                the dynamic type
     * @param   notFoundBehaviour   the behaviour to apply when an adapter is not found
     * @return                      the adapter
     * @deprecated
     *
     ******************************************************************************************************************/
    @Nonnull @Deprecated
    public default <T> T as (@Nonnull final Class<T> type, @Nonnull final NotFoundBehaviour<T> notFoundBehaviour)
      {
        return maybeAs(type).orElseGet(() -> notFoundBehaviour.run(new AsException(type)));
      }

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>                 the static type
     * @param   type                the dynamic type
     * @return                      the optional role
     * @since                       3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Optional<T> maybeAs (@Nonnull Class<T> type);

    /*******************************************************************************************************************
     *
     * Searches for multiple adapters of the given type and returns them.
     *
     * @param   <T>     the static type
     * @param   type    the dynamic type
     * @return          a collection of adapters, possibly empty
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> asMany (@Nonnull Class<T> type);

    /*******************************************************************************************************************
     *
     * Creates a role type reference.
     *
     * @param   <T>                 the static type
     * @param   type                the dynamic type
     * @return                      the type reference
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Ref<T> ref (@Nonnull final Class<?> type) // FIXME: there's no static check of the argument
      {
        return new Ref<>(type);
      }

    /*******************************************************************************************************************
     *
     * Returns a role for this object of the specified type. If the implementation can find multiple compliant
     * roles, only one will be returned.
     *
     * @param   <T>                 the static type
     * @param   ref                 the type reference
     * @return                      the role
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> T as (@Nonnull final Ref<T> ref)
      {
        return as(ref.getType());
      }

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>                 the static type
     * @param   ref                 the type reference
     * @return                      the optional role
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Optional<T> maybeAs (@Nonnull final Ref<T> ref)
      {
        return maybeAs(ref.getType());
      }

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>                 the static type
     * @param   ref                 the type reference
     * @return                      the roles
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Collection<T> asMany (@Nonnull final Ref<T> ref)
      {
        return asMany(ref.getType());
      }
  }
