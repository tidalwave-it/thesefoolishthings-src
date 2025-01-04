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
package it.tidalwave.role.impl;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import it.tidalwave.util.As;
import it.tidalwave.util.LazySupplier;
import it.tidalwave.util.Parameters;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.spi.OwnerRoleFactory;
import it.tidalwave.role.spi.OwnerRoleFactoryProvider;
import static it.tidalwave.util.Parameters.r;

/***************************************************************************************************************************************************************
 *
 * An implementation of {@link As} that keeps some local roles and queries {@link OwnerRoleFactory}.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class AsDelegate implements As
  {
    @Nonnull
    private final Object owner;

    @Nonnull
    private final LazySupplier<OwnerRoleFactory> ownerRoleFactory;


    @Nonnull
    private final List<Object> roles = new ArrayList<>();

    /***********************************************************************************************************************************************************
     * Constructor for use in subclassing.
     **********************************************************************************************************************************************************/
    protected AsDelegate ()
      {
        owner = this;
        ownerRoleFactory = LazySupplier.of(() -> OwnerRoleFactoryProvider.getInstance().createRoleFactory(this));
      }

    /***********************************************************************************************************************************************************
     * Constructor for use in composition.
     *
     * @param  owner             the owner
     * @since  3.2-ALPHA-3 (refactored)
     **********************************************************************************************************************************************************/
    public AsDelegate (@Nonnull final Object owner)
      {
        this(owner, Collections.emptyList());
      }

    /***********************************************************************************************************************************************************
     * Constructor for use in composition. In addition to the mandatory owner, it accepts a single pre-instantiated
     * role, or a {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param  owner          the owner
     * @param  role           the role or {@link it.tidalwave.util.RoleFactory}
     * @since  3.2-ALPHA-3
     **********************************************************************************************************************************************************/
    public AsDelegate (@Nonnull final Object owner, @Nonnull final Object role)
      {
        this(owner, r(Parameters.mustNotBeArrayOrCollection(role, "role")));
      }

    /***********************************************************************************************************************************************************
     * Constructor for use in composition. In addition to the mandatory owner, it accepts a collection of
     * pre-instantiated roles, or instances of {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param  owner          the owner
     * @param  roles          roles or {@link it.tidalwave.util.RoleFactory} instances
     * @since  3.2-ALPHA-3 (refactored)
     **********************************************************************************************************************************************************/
    public AsDelegate (@Nonnull final Object owner, @Nonnull final Collection<Object> roles)
      {
        this(o -> OwnerRoleFactoryProvider.getInstance().createRoleFactory(o), owner, roles);
      }

    /***********************************************************************************************************************************************************
     * Constructor for use in tests. This constructor doesn't call {@link OwnerRoleFactoryProvider#getInstance()}.
     *
     * @param  systemRoleFactoryFunction  the factory
     * @param  owner          the owner
     * @param  roles          roles or {@link it.tidalwave.util.RoleFactory} instances
     * @since  3.2-ALPHA-3 (refactored)
     **********************************************************************************************************************************************************/
    public AsDelegate (@Nonnull final Function<Object, OwnerRoleFactory> systemRoleFactoryFunction,
                       @Nonnull final Object owner,
                       @Nonnull final Collection<Object> roles)
      {
        ownerRoleFactory = LazySupplier.of(() -> systemRoleFactoryFunction.apply(owner));
        this.owner = owner;
        this.roles.addAll(RoleFactory.resolveFactories(owner, roles));
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     *
     * First, local roles are probed; then the owner, in case it directly implements the required role; at last,
     * the systemRoleFactory is invoked.
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public <T> Optional<T> maybeAs (@Nonnull final Class<? extends T> type)
      {
        for (final var role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                return Optional.of(type.cast(role));
              }
          }

        final var r = ownerRoleFactory.get().findRoles(type);
        return r.isEmpty() ? Optional.empty() : Optional.of(r.iterator().next());
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     *
     * The list contains all the relevant local roles, as well as those retrieved by the delegate, in this order.
     **********************************************************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> asMany (@Nonnull final Class<? extends T> type)
      {
        final var results = new ArrayList<T>();

        for (final var role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                results.add(type.cast(role));
              }
          }

        results.addAll(ownerRoleFactory.get().findRoles(type));

        return results;
      }
  }
