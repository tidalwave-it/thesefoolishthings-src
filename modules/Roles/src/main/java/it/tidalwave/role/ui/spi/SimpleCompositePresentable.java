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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.As.NotFoundBehaviour;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Finder;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.Task;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelFactory;
import it.tidalwave.role.spi.ContextSampler;
import it.tidalwave.role.impl.DefaultSimpleComposite;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.role.SimpleComposite._SimpleComposite_;
import static it.tidalwave.role.spi.impl.LogUtil.*;

/***********************************************************************************************************************
 *
 * An implementation of {@link Presentable} for datum instances having the {@link SimpleComposite} role.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class SimpleCompositePresentable<T extends As> implements Presentable
  {
    @RequiredArgsConstructor
    static class SCPFinder<T extends As> extends SimpleFinderSupport<PresentationModel>
      {
        private static final long serialVersionUID = -3235827383866946732L;

        @Nonnull
        private final SimpleCompositePresentable<T> scp;

        @Nonnull
        private final Collection<Object> rolesOrFactories;

        public SCPFinder (@Nonnull final SCPFinder<T> other, @Nonnull final Object override)
          {
            super(other, override);
            final SCPFinder<T> source = getSource(SCPFinder.class, other, override);
            this.scp = source.scp;
            this.rolesOrFactories = source.rolesOrFactories;
          }

        @Override @Nonnull
        protected List<? extends PresentationModel> computeResults()
          {
            return scp.contextSampler.runWithContexts(new Task<List<? extends PresentationModel>, RuntimeException>()
              {
                @Override
                public List<? extends PresentationModel> run()
                  {
                    final List<PresentationModel> results = new ArrayList<>();

                    try
                      {
                        @SuppressWarnings("unchecked")
                        final SimpleComposite<T> composite = scp.datum.as(_SimpleComposite_);

                        for (final T child : composite.findChildren().results())
                          {
                            final Presentable presentable = child.as(_Presentable_,
                                                                     t -> new SimpleCompositePresentable<>(child));

                            results.add(presentable.createPresentationModel(rolesOrFactories));
                          }
                      }
                    catch (AsException e)
                      {
                        // ok, no Composite role
                        log.trace(">>>> no composite role for {}", scp.datum);
                      }

                    return results;
                  }
              });
          }
      }

    private static final long serialVersionUID = 324646965695684L;

    @Nonnull
    private final T datum;

    // This is not @Injected to avoid a dependency on Spring AOP
    @Nonnull
    private final PresentationModelFactory defaultPresentationModelFactory;

    private final ContextSampler contextSampler;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public SimpleCompositePresentable (@Nonnull final T datum)
      {
        this(datum, new DefaultPresentationModelFactory());
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public SimpleCompositePresentable (@Nonnull final T datum,
                                       @Nonnull final PresentationModelFactory defaultPresentationModelFactory)
      {
        this.datum = datum;
        this.defaultPresentationModelFactory = defaultPresentationModelFactory;
        contextSampler = new ContextSampler(datum);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public PresentationModel createPresentationModel (@Nonnull final Collection<Object> rolesOrFactories)
      {
        return internalCreatePresentationModel(datum, rolesOrFactories);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private PresentationModel internalCreatePresentationModel (@Nonnull final T datum,
                                                               @Nonnull final Collection<Object> rolesOrFactories)
      {
        final Finder<PresentationModel> pmFinder = new SCPFinder(this, rolesOrFactories);

        return contextSampler.runWithContexts(new Task<PresentationModel, RuntimeException>()
          {
            @Override @Nonnull
            public PresentationModel run()
              {
                final List<Object> roles = resolveRoles(datum, rolesOrFactories);
                roles.add(new DefaultSimpleComposite<>(pmFinder));
                log.trace(">>>> roles for {}: {}", shortId(datum), shortIds(roles));

                return defaultPresentationModelFactory.createPresentationModel(datum, roles);
              }
          });
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveRoles (@Nonnull final T datum, @Nonnull final Collection<Object> rolesOrFactories)
      {
        final List<Object> roles = new ArrayList<>();

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
