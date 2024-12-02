/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import it.tidalwave.role.Aggregate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A specialisation of {@link Aggregate}{@code <PresentationModel>} which offers a convenience method for aggregating
 * its contained objects.
 *
 * @author  Fabrizio Giudici
 * @since 3.2-ALPHA-3
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @ToString
public class PresentationModelAggregate implements Aggregate<PresentationModel>
  {
    @Nonnull
    private final Aggregate<PresentationModel> delegate;

    /*******************************************************************************************************************
     *
     * Creates a new, empty instance.
     *
     * @return          the new instance
     *
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModelAggregate newInstance()
      {
        return new PresentationModelAggregate(Aggregate.newInstance());
      }

    /*******************************************************************************************************************
     *
     * Adds another {@link PresentationModel} with the given roles, associated to the given name. With a plain
     * {@link Aggregate}{@code <PresentationModel>} the code would be:
     *
     * <pre>
     *   Aggregate&lt;PresentationModel&gt; aggregate = Aggregate.newInstance()
     *            .with("name", PresentationModel.of("", r(role1, role2, role3));
     * </pre>
     *
     * The simplified code is:
     *
     * <pre>
     *   Aggregate&lt;PresentationModel&gt; aggregate = PresentationModelAggregate.newInstance()
     *            .withPmOf("name", r(role1, role2, role3));
     * </pre>
     *
     * @param   name    the name of the {@code PresentationModel}
     * @param   roles   the roles
     * @return          the new {@code PresentationModel}
     *
     ******************************************************************************************************************/
    @Nonnull
    public PresentationModelAggregate withPmOf (@Nonnull final String name, @Nonnull final Collection<Object> roles)
      {
        return new PresentationModelAggregate(delegate.with(name, PresentationModel.of("", roles)));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Optional<PresentationModel> getByName (@Nonnull final String name)
      {
        return delegate.getByName(name);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Collection<String> getNames()
      {
        return delegate.getNames();
      }
  }