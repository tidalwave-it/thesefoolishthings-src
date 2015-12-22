/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.util.Task;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.role.spi.RoleManager;
import it.tidalwave.role.spi.ContextSampler;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.spi.LogUtil.*;

/***********************************************************************************************************************
 *
 * An implementation for {@link As} based on Spring.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable(preConstruction = true) @Slf4j
class SpringAsDelegate implements AsDelegate
  {
    @Inject @Nonnull
    private RoleManager roleManager;

    @Nonnull
    private final Object owner;

    private final ContextSampler contextSampler;

    /*******************************************************************************************************************
     *
     * Constructor for use with subclassing.
     *
     ******************************************************************************************************************/
    public SpringAsDelegate()
      {
        this.owner = this;
        contextSampler = new ContextSampler(this);
      }

    /*******************************************************************************************************************
     *
     * Constructor for use with composition.
     *
     * @param  owner  the owner object
     *
     ******************************************************************************************************************/
    public SpringAsDelegate (final @Nonnull Object owner)
      {
        this.owner = owner;
        contextSampler = new ContextSampler(owner);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public <T> Collection<? extends T> as (final @Nonnull Class<T> roleType)
      {
        log.trace("as({}) for {}", shortName(roleType), shortId(owner));
        log.trace(">>>> contexts: {}", contextSampler);

        final List<T> roles = new ArrayList<T>(contextSampler.runWithContexts(new Task<List<? extends T>, RuntimeException>()
          {
            @Override @Nonnull
            public List<? extends T> run()
              {
                return roleManager.findRoles(owner, roleType);
              }
          }));

        if (roleType.isAssignableFrom(owner.getClass()))
          {
            roles.add(roleType.cast(owner));
          }

        log.trace(">>>> as() returning {}", shortIds((List)roles));

        return roles;
      }
  }
