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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.role.spi.DefaultDisplayable;
import static it.tidalwave.util.BundleUtilities.*;

/***********************************************************************************************************************
 *
 * @since   3.1-ALPHA-2
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Interface.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public interface Displayable8 extends Displayable
  {
    public static final Class<Displayable8> Displayable8 = Displayable8.class;

    /*******************************************************************************************************************
     *
     * Creates a {@link Displayable} from a resource bundle. The bundle resource file is named {@code Bundle.properties}
     * and it should be placed in the same package as the owner class.
     *
     * @param   ownerClass  the class that owns the bundle
     * @param   key         the resource key
     * @return              the {@code Displayable}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable displayableFromBundle (final @Nonnull Class<?> ownerClass, final @Nonnull String key)
      {
        return new DefaultDisplayable(getMessage(ownerClass, key));
      }
  }
