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
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.role.impl.AsDelegate;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * Objects implementing this interface can provide am adapter of the required type. The adapter can be found with a
 * variety of approaches that depend on the implementation. This capability can be used to implement a design based
 * on the Data, Context and Interaction pattern (DCI). For further details, please look at the
 * <a href="http://tidalwave.it/projects/thesefoolishthings">project website</a>, where a tutorial is available.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface As
  {
    /*******************************************************************************************************************
     *
     * A type reference for roles that can be used in place of a class literal, especially when roles with generics are
     * used. Example of usage:
     * <pre>
     *
     *     interface DataRetriever&lt;T&gt;
     *       {
     *         public List&lt;T&gt; retrieve();
     *       }
     *
     *     class CodeSample
     *       {
     *         private static final As.Type&lt;DataRetriever&lt;String&gt;&gt; _StringRetriever_ = As.type(DataRetriever.class);
     *
     *         public void method (As object)
     *           {
     *             List&lt;String&gt; f3 = object.as(_StringRetriever_).retrieve();
     *           }
     *       }
     * </pre>
     *
     * @since     3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static final class Type<T>
      {
        @Nonnull
        private final Class<?> type;


        @Nonnull @SuppressWarnings("unchecked")
        /* package */ Class<T> getType()
          {
            return (Class<T>)type;
          }
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation delegate for the given object (or returns the object itself if it is the
     * default implementation of {@code As}).
     *
     * @param     object         the object
     * @return                   the implementation
     * @since     3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public static As forObject (@Nonnull final Object object)
      {
        return (object instanceof AsDelegate) ? (As)object : new AsDelegate(object);
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation delegate for the given object. It accepts a single pre-instantiated role,
     * or a {@link RoleFactory} that will be invoked to create additional roles.
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
        return new AsDelegate(object, role);
      }

    /*******************************************************************************************************************
     *
     * Creates an {@code As} implementation delegate for the given object. It accepts a collection of pre-instantiated
     * roles, or instances of {@link RoleFactory} that will be invoked to create additional roles.
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
        return new AsDelegate(object, roles);
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
    public default <T> T as (@Nonnull final Class<? extends T> type)
      {
        return maybeAs(type).orElseThrow(() -> new AsException(type));
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
    public <T> Optional<T> maybeAs (@Nonnull Class<? extends T> type);

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
    public <T> Collection<T> asMany (@Nonnull Class<? extends T> type);

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
    public static <T> Type<T> type (@Nonnull final Class<?> type) // FIXME: there's no static check of the argument
      {
        return new Type<>(type);
      }

    /*******************************************************************************************************************
     *
     * Returns a role for this object of the specified type. If the implementation can find multiple compliant
     * roles, only one will be returned.
     *
     * @param   <T>                 the static type
     * @param   type                the type reference
     * @return                      the role
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> T as (@Nonnull final Type<? extends T> type)
      {
        return as(type.getType());
      }

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>                 the static type
     * @param   type                the type reference
     * @return                      the optional role
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Optional<T> maybeAs (@Nonnull final Type<? extends T> type)
      {
        return maybeAs(type.getType());
      }

    /*******************************************************************************************************************
     *
     * Returns the requested role or an empty {@link Optional}.
     *
     * @param   <T>                 the static type
     * @param   type                the type reference
     * @return                      the roles
     * @since                       3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> Collection<T> asMany (@Nonnull final Type<? extends T> type)
      {
        return asMany(type.getType());
      }
  }
