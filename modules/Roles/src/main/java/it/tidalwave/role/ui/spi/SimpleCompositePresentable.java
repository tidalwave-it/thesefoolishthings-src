/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role
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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.Task;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelFactory;
import it.tidalwave.role.spi.ContextSampler;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.role.SimpleComposite._SimpleComposite_;
import static it.tidalwave.role.spi.impl.LogUtil.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

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
public class SimpleCompositePresentable implements Presentable
  {
    @RequiredArgsConstructor
    static class SCPFinder extends SimpleFinderSupport<PresentationModel>
      {
        private static final long serialVersionUID = -3235827383866946732L;

        @Nonnull
        private final SimpleCompositePresentable scp;

        @Nonnull
        private final Collection<Object> roles;

        public SCPFinder (@Nonnull final SCPFinder other, @Nonnull final Object override)
          {
            super(other, override);
            final SCPFinder source = getSource(SCPFinder.class, other, override);
            this.scp = source.scp;
            this.roles = source.roles;
          }

        @Override @Nonnull
        protected List<? extends PresentationModel> computeResults()
          {
            return scp.contextSampler.runWithContexts(new Task<List<? extends PresentationModel>, RuntimeException>()
              {
                @Override @Nonnull
                public List<? extends PresentationModel> run()
                  {
                    final List<As> children = scp.datum.maybeAs(_SimpleComposite_)
                                                       .map(c -> c.findChildren().results()).orElse(emptyList());
                    return children.stream()
                                   .map(child -> child.maybeAs(_Presentable_)
                                                      .orElseGet(() -> new SimpleCompositePresentable(child)))
                                   .map(presentable -> presentable.createPresentationModel(roles))
                                   .collect(toList());
                  }
              });
          }
      }

    private static final long serialVersionUID = 324646965695684L;

    @Nonnull
    private final As datum;

    // This is not @Injected to avoid a dependency on Spring AOP
    @Nonnull
    private final PresentationModelFactory defaultPresentationModelFactory;

    private final ContextSampler contextSampler;

    /*******************************************************************************************************************
     *
     * @param datum     the owner
     *
     ******************************************************************************************************************/
    public SimpleCompositePresentable (@Nonnull final As datum)
      {
        this(datum, new DefaultPresentationModelFactory());
      }

    /*******************************************************************************************************************
     *
     * @param datum                             the owner
     * @param defaultPresentationModelFactory   the {@code PresentationModelFactory}
     *
     ******************************************************************************************************************/
    public SimpleCompositePresentable (@Nonnull final As datum,
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
    public PresentationModel createPresentationModel (@Nonnull final Collection<Object> roles)
      {
        return internalCreatePresentationModel(datum, roles);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private PresentationModel internalCreatePresentationModel (@Nonnull final As datum,
                                                               @Nonnull final Collection<Object> roles)
      {
        final SCPFinder pmFinder = new SCPFinder(this, roles);

        return contextSampler.runWithContexts(new Task<PresentationModel, RuntimeException>()
          {
            @Override @Nonnull
            public PresentationModel run()
              {
                final List<Object> r = resolveRoles(datum, roles);

                if (datum.maybeAs(_SimpleComposite_).isPresent())
                  {
                    r.add(SimpleComposite.of(pmFinder));
                  }

                log.trace(">>>> r for {}: {}", shortId(datum), shortIds(r));

                return defaultPresentationModelFactory.createPresentationModel(datum, r);
              }
          });
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private List<Object> resolveRoles (@Nonnull final As datum, @Nonnull final Collection<Object> roles)
      {
        final List<Object> r = new ArrayList<>();

        for (final Object roleOrFactory : roles)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                r.add(((RoleFactory<As>)roleOrFactory).createRoleFor(datum));
              }
            else
              {
                r.add(roleOrFactory);
              }
          }

        return r;
      }
  }
