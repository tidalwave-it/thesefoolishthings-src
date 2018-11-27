/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import it.tidalwave.util.Callback;
import it.tidalwave.role.ui.UserAction8;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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
public class UserActionSupport8 extends UserActionSupport implements UserAction8
  {
    @Getter @Accessors(fluent = true)
    private final BooleanProperty enabledProperty = new SimpleBooleanProperty(true);

    @Nonnull
    private final Callback callback;

//    private final InvalidationListener invalidationListener = (Observable observable) ->
//      {
//        log.info("invalidated {}", observable);
//      };

    private final ChangeListener<Boolean> changeListener =
            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) ->
      {
        this.enabled().set(newValue);
      };

    public UserActionSupport8 (final @Nonnull Callback callback, final @Nonnull Object ... rolesOrFactories)
      {
        super(rolesOrFactories);
        this.callback = callback;
        enabledProperty.addListener(changeListener);
//        enabledProperty.addListener(invalidationListener);
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
