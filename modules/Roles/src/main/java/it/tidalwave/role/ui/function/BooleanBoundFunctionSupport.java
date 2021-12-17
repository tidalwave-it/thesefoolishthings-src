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
package it.tidalwave.role.ui.function;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import it.tidalwave.role.ui.ChangingSource;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public abstract class BooleanBoundFunctionSupport extends BoundFunctionSupport<Boolean, Boolean>
  {
    protected final ChangingSource<Boolean>[] sources;

    private boolean oldValue;

    private boolean newValue;

    private final PropertyChangeListener pcl = new PropertyChangeListener()
      {
        @Override
        public void propertyChange (@Nonnull final PropertyChangeEvent event)
          {
            newValue = function();
            fireValueChanged(oldValue, newValue);
            oldValue = newValue;
          }
      };

    public BooleanBoundFunctionSupport (@Nonnull final ChangingSource<Boolean> ... sources)
      {
        this.sources = sources;

        for (final ChangingSource<Boolean> source : sources)
          {
            source.addPropertyChangeListener(pcl);
          }

        oldValue = function();
      }

    protected abstract boolean function();

    @Override @Nonnull
    public final Boolean get()
      {
        return newValue;
      }
  }
