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

import it.tidalwave.role.ui.impl.DefaultStyleable;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * A role which carries styles.
 * 
 * @stereotype Role
 * 
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Styleable
  {
    public static final Class<Styleable> Styleable = Styleable.class;
    
    @Nonnull
    public Collection<String> getStyles();

    /*******************************************************************************************************************
     *
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Styleable of (final @Nonnull Collection<String> styles)
      {
        return new DefaultStyleable(styles);
      }

    /*******************************************************************************************************************
     *
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Styleable of (final @Nonnull String ... styles)
      {
        return new DefaultStyleable(styles);
      }
  }
