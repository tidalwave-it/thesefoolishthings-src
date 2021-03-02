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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import java.util.Collection;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AsSupport implements As
  {
    @Nonnull
    private final AsDelegate delegate;

    @Nonnull
    private final Object owner;

    @Nonnull
    private final List<Object> roles = new ArrayList<Object>();

    /*******************************************************************************************************************
     *
     * Constructor for use in subclassing.
     *
     ******************************************************************************************************************/
    protected AsSupport()
      {
        owner = this;
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(this);
      }

    /*******************************************************************************************************************
     *
     * Constructor for use in composition. In addition to the mandatory owner, it's possible to pass a collection of
     * pre-instantiated roles, or instances of {@link RoleFactory} that will be invoked to create additional roles.
     *
     * @param  owner             the owner
     * @param  rolesOrFactories  a collection of roles or factories for roles
     *
     ******************************************************************************************************************/
    public AsSupport (final @Nonnull Object owner, final @Nonnull Object ... rolesOrFactories)
      {
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(owner);
        this.owner = owner;
        roles.addAll(resolveFactories(Arrays.asList(rolesOrFactories)));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (final @Nonnull Class<T> type)
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
    public <T> T as (final @Nonnull Class<T> type, final @Nonnull As.NotFoundBehaviour<T> notFoundBehaviour)
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

        return (T)r.iterator().next();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     * The list contains all the relevant local roles, as well as those retrieved by the delegate, in this order.
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> Collection<T> asMany (final @Nonnull Class<T> type)
      {
        Collection<T> results = new ArrayList<T>();

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
     * @param  rolesOrFactories  the list of roles or factory roles
     * @return                   a list of roles
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveFactories (final @Nonnull List<Object> rolesOrFactories)
      {
        final List<Object> result = new ArrayList<Object>();

        for (final Object roleOrFactory : rolesOrFactories)
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
