/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Finder;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelFactory;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.role.ui.Presentable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An implementation of {@link Presentable} for datum instances having the {@link SimpleComposite} role.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class SimpleCompositePresentable<T extends As> implements Presentable
  {
    private static final long serialVersionUID = 324646965695684L;

    @Nonnull
    private final T datum;

    // This is not @Injected to avoid a dependency on Spring AOP
    @Nonnull
    private final PresentationModelFactory defaultPresentationModelFactory;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public SimpleCompositePresentable (final @Nonnull T datum)
      {
        this(datum, new DefaultPresentationModelFactory());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object ... rolesOrFactories)
      {
        return internalCreatePresentationModel(datum, new ArrayList<Object>(Arrays.asList(rolesOrFactories)));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private PresentationModel internalCreatePresentationModel (final @Nonnull T datum,
                                                               final @Nonnull List<Object> rolesOrFactories)
      {
        final Finder<PresentationModel> pmFinder = new SimpleFinderSupport<PresentationModel>()
          {
            @Override @Nonnull
            protected List<? extends PresentationModel> computeResults()
              {
                final List<PresentationModel> results = new ArrayList<PresentationModel>();

                try
                  {
                    @SuppressWarnings("unchecked")
                    final SimpleComposite<T> composite = datum.as(SimpleComposite.class);

                    for (final T child : composite.findChildren().results())
                      {
                        results.add(internalCreatePresentationModel(child, rolesOrFactories));
                      }
                  }
                catch (AsException e)
                  {
                    // ok, no Composite role
                  }

                return results;
              }
          };

        final List<Object> roles = resolveRoles(datum, rolesOrFactories);
        roles.add(new DefaultSimpleComposite<PresentationModel>(pmFinder));
        log.trace(">>>> roles for {}: {}", datum, roles);

        return defaultPresentationModelFactory.createPresentationModel(datum, roles.toArray());
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveRoles (final @Nonnull T datum,
                                       final @Nonnull List<Object> rolesOrFactories)
      {
        final List<Object> roles = new ArrayList<Object>();

        for (final Object roleOrFactory : rolesOrFactories)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                roles.add(((RoleFactory<T>)roleOrFactory).createRoleFor(datum));
              }
            else
              {
                roles.add(roleOrFactory);
              }
          }

        return roles;
      }
  }