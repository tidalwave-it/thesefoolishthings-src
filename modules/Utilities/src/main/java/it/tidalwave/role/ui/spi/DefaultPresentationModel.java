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
import java.beans.PropertyChangeSupport;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.util.spi.AsSupport;
import lombok.Delegate;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A default implementation of {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ToString
public class DefaultPresentationModel implements PresentationModel
  {
    @Delegate
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Delegate
    private final AsSupport asDelegate;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultPresentationModel (final @Nonnull Object datum,
                                     final @Nonnull Object ... rolesOrFactories)
      {
        asDelegate = new AsSupport(datum, rolesOrFactories);
      }
  }
