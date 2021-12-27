/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Parameters;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import static it.tidalwave.util.Parameters.r;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultAs implements As
  {
    @Nonnull
    private final AsDelegate delegate;

    @Nonnull
    private final Object owner;

    @Nonnull
    private final List<Object> roles = new ArrayList<>();

    /*******************************************************************************************************************
     *
     * Constructor for use in subclassing.
     *
     ******************************************************************************************************************/
    protected DefaultAs()
      {
        owner = this;
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(this);
      }

    /*******************************************************************************************************************
     *
     * Constructor for use in composition.
     *
     * @param  owner             the owner
     * @since  3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    public DefaultAs (@Nonnull final Object owner)
      {
        this(owner, Collections.emptyList());
      }

    /*******************************************************************************************************************
     *
     * Constructor for use in composition. In addition to the mandatory owner, it accepts a single pre-instantiated
     * role, or a {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param  owner          the owner
     * @param  role           the role or {@link it.tidalwave.util.RoleFactory}
     * @since  3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    public DefaultAs (@Nonnull final Object owner, @Nonnull final Object role)
      {
        this(owner, r(Parameters.mustNotBeArrayOrCollection(role, "role")));
      }

    /*******************************************************************************************************************
     *
     * Constructor for use in composition. In addition to the mandatory owner, it accepts a collection of
     * pre-instantiated roles, or instances of {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param  owner          the owner
     * @param  roles          roles or {@link it.tidalwave.util.RoleFactory} instances
     * @since  3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    public DefaultAs (@Nonnull final Object owner, @Nonnull final Collection<Object> roles)
      {
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(owner);
        this.owner = owner;
        this.roles.addAll(resolveFactories(roles));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull final Class<T> type)
      {
        return as(type, As.Defaults.throwAsException(type));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     * First, local roles are probed; then the owner, in case it directly implements the required role; at last,
     * the delegate is invoked.
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull final Class<T> type, @Nonnull final As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        for (final Object role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                return type.cast(role);
              }
          }

        final Collection<? extends T> r = delegate.as(type);

        if (r.isEmpty())
          {
            return notFoundBehaviour.run(new AsException(type));
          }

        return r.iterator().next();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     * The list contains all the relevant local roles, as well as those retrieved by the delegate, in this order.
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> asMany (@Nonnull final Class<T> type)
      {
        final Collection<T> results = new ArrayList<>();

        for (final Object role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                results.add(type.cast(role));
              }
          }

        results.addAll(delegate.as(type));

        return results;
      }

    /*******************************************************************************************************************
     *
     * Resolve the factories: if found, they are invoked and the produced role is added to the list.
     *
     * @param  roles  the list of roles or factory roles
     * @return                   a list of roles
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveFactories (@Nonnull final Collection<Object> roles)
      {
        final List<Object> result = new ArrayList<>();

        for (final Object roleOrFactory : roles)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                result.add(((RoleFactory<Object>)roleOrFactory).createRoleFor(owner));
              }
            else
              {
                result.add(roleOrFactory);
              }
          }

        return result;
      }
  }
