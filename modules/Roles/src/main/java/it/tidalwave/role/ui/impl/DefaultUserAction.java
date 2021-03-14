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
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.util.Callback;
import it.tidalwave.util.spi.AsSupport;
import it.tidalwave.role.ui.UserAction;
// import javafx.beans.property.SimpleBooleanProperty;
// import javafx.beans.value.ChangeListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
// import java.beans.PropertyChangeListener;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(staticName = "withCallback") @Slf4j
public class DefaultUserAction implements UserAction
  {
    // @Getter @Accessors(fluent = true)
    // private final SimpleBooleanProperty enabledProperty = new SimpleBooleanProperty(true); // BoundProperty<Boolean>?

    // FIXME: what to do? Should be in sync with enabledProperty?
    @Getter @Accessors(fluent = true)
    private final BoundProperty<Boolean> enabled = new BoundProperty<Boolean>(true);

    @Delegate @Nonnull
    private final AsSupport asSupport;

    @Nonnull
    private final Callback callback;

    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-1 (was previously in {@code UserAction8}
     *
     ******************************************************************************************************************/
    public DefaultUserAction (final @Nonnull Callback callback, final @Nonnull Object ... rolesOrFactories)
      {
        this.callback = callback;
        this.asSupport = new AsSupport(this, rolesOrFactories);
        // enabledProperty.addListener(changeListener);
        // enabled.addPropertyChangeListener(propertyChangeListener);
//        enabled.addListener(invalidationListener);
      }

//    private final InvalidationListener invalidationListener = (Observable observable) ->
//      {
//        log.info("invalidated {}", observable);
//      };

    // private final ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> enabled.set(newValue);

    //private final PropertyChangeListener propertyChangeListener =
    //        evt -> enabledProperty.set((Boolean)evt.getNewValue());


    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-1 (was previously in {@code UserAction8}
     *
     ******************************************************************************************************************/
    @Nonnull
    public DefaultUserAction withRoles (final @Nonnull Object ... rolesOrFactories)
      {
        return new DefaultUserAction(callback, rolesOrFactories);
      }

    @Override
    public void actionPerformed() // FIXME: change with composition
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
