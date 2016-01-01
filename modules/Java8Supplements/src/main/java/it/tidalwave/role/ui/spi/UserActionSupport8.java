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

package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.Callback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A Java 8 extension for {@link UserActionSupport} that supports lambdas.
 * 
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 * @since   3.0
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(staticName = "withCallback") @Slf4j
public class UserActionSupport8 extends UserActionSupport
  {
    @Nonnull
    private final Callback callback;

    private UserActionSupport8 (final @Nonnull Callback callback, final @Nonnull Object ... rolesOrFactories)
      {
        super(rolesOrFactories);
        this.callback = callback;
      }

    @Nonnull
    public UserActionSupport8 withRoles (final @Nonnull Object ... rolesOrFactories)
      {
        return new UserActionSupport8(callback, rolesOrFactories);
      }

    @Override
    public final void actionPerformed()
      {
        try
          {
            callback.call();
          }
        catch (Throwable e)
          {
            log.error("", e);
          }
      }
  }
