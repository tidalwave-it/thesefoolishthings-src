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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.Parameters;
import it.tidalwave.role.ui.impl.DefaultPresentable;
import static it.tidalwave.util.Parameters.r;

/***********************************************************************************************************************
 *
 * The role of an object that can be presented on a UI, thus is capable of creating a {@link PresentationModel}.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Presentable
  {
    public static final Class<Presentable> _Presentable_ = Presentable.class;

    /*******************************************************************************************************************
     *
     * Creates a {@link PresentationModel}.
     *
     * @return                  the {@code PresentationModel}
     * @since                   3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    public default PresentationModel createPresentationModel()
      {
        return createPresentationModel(Collections.emptyList());
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link PresentationModel} with some roles.
     *
     * @param  instanceRoles    the roles
     * @return                  the {@code PresentationModel}
     * @since                   3.2-ALPHA-3 (refactored)
     *
     ******************************************************************************************************************/
    @Nonnull
    public PresentationModel createPresentationModel (@Nonnull Collection<Object> instanceRoles);

    /*******************************************************************************************************************
     *
     * Creates a {@link PresentationModel} with a single role.
     *
     * @param  instanceRole     the role
     * @return                  the {@code PresentationModel}
     * @since                   3.2-ALPHA-3
     *
     ******************************************************************************************************************/
    @Nonnull
    public default PresentationModel createPresentationModel (@Nonnull final Object instanceRole)
      {
        Parameters.mustNotBeArrayOrCollection(instanceRole, "instanceRole");
        return createPresentationModel(r(instanceRole));
      }

    /*******************************************************************************************************************
     *
     * Creates a default {@link Presentable} for the given object.
     *
     * @param   owner           the object
     * @return                  the new instance
     * @since                   3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Presentable of (@Nonnull final Object owner)
      {
        return new DefaultPresentable(owner);
      }
  }
