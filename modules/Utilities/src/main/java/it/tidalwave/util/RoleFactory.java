/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/***************************************************************************************************************************************************************
 *
 * A factory of roles.
 *
 * @param   <T>   the type of the owner
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@FunctionalInterface
public interface RoleFactory<T, R>
  {
    /***********************************************************************************************************************************************************
     * {@return one or more roles — if multiple ones have to be returned, they must be wrapped in a {@link java.util.Collection}}.
     * @param   owner   the owner
     **********************************************************************************************************************************************************/
    @Nonnull
    public Optional<R> createRoleFor (@Nonnull T owner);

    /***********************************************************************************************************************************************************
     * {@return the role type}.
     * @since   4.0-ALPHA-2
     **********************************************************************************************************************************************************/
    @Nonnull @SuppressWarnings("unchecked")
    public default Class<R> getRoleType()
      {
        return (Class<R>)ReflectionUtils.getTypeArguments(RoleFactory.class, getClass()).get(1);
      }

    /***********************************************************************************************************************************************************
     * {@return a collection of roles resolving factories}; that is, any factory in the collection is invoked so it can eventually create another role.
     * @param   roleOrFactories   a collection of roles or {@link RoleFactory} instances
     * @param   owner             the role owner
     * @since   4.0-ALPHA-2
     **********************************************************************************************************************************************************/
    @Nonnull @SuppressWarnings("unchecked")
    public static <T> Collection<Object> resolveFactories (@Nonnull final T owner, @Nonnull final Iterable<Object> roleOrFactories)
      {
        final List<Object> result = new ArrayList<>();

        for (final var roleOrFactory : roleOrFactories)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                ((RoleFactory<T, ?>)roleOrFactory).createRoleFor(owner).ifPresent(result::add);
              }
            else
              {
                result.add(roleOrFactory);
              }
          }

        return result;
      }
  }
