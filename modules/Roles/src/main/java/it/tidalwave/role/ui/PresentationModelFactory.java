/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import it.tidalwave.util.RoleFactory;

/***********************************************************************************************************************
 *
 * A factory that creates a default {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface PresentationModelFactory
  {
    public static final Class<PresentationModelFactory> PresentationModelFactory = PresentationModelFactory.class;

    /*******************************************************************************************************************
     *
     * Creates a new instance of {@link PresentationModel}.
     *
     * @param  datum              the related datum
     * @param  rolesOrFactories   roles of {@link RoleFactory} instances to put in the presentation model
     *
     ******************************************************************************************************************/
    @Nonnull
    public PresentationModel createPresentationModel (@Nonnull Object datum, @Nonnull Object ... rolesOrFactories);
  }
