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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.impl.DefaultMutableDisplayable;

/***********************************************************************************************************************
 *
 * A specialized {@link it.tidalwave.role.ui.LocalizedDisplayable} which is mutable.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface MutableLocalizedDisplayable extends MutableDisplayable, LocalizedDisplayable
  {
    public final static Class<MutableLocalizedDisplayable> MutableLocalizedDisplayable =
            MutableLocalizedDisplayable.class;

    /*******************************************************************************************************************
     *
     * Creates an instance with an initial given display name in {@code Locale.ENGLISH}.
     *
     * @param  displayName   the display name
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static MutableLocalizedDisplayable of (final @Nonnull String displayName)
      {
        return of(displayName, "???");
      }

    /*******************************************************************************************************************
     *
     * Creates an instance with an initial given display name in {@code Locale.ENGLISH} and an explicit identifier for
     * {@code toString()}.
     *
     * @param  displayName   the display name
     * @param  toStringName  the name to be rendered when {@code toString()} is called
     * @since 3.2-ALPHA-1
     *
     ******************************************************************************************************************/
    @Nonnull
    public static MutableLocalizedDisplayable of (final @Nonnull String displayName,
                                                  final @Nonnull String toStringName)
      {
        return new DefaultMutableDisplayable(displayName, toStringName);
      }
  }
