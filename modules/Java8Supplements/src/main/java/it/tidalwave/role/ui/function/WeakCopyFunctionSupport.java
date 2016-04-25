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
package it.tidalwave.role.ui.function;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.Changeable;
import it.tidalwave.role.ui.ChangingSource;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * Changes the destination only at a certain condition in function of the target.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class WeakCopyFunctionSupport<T> extends UnaryBoundFunctionSupport<T, T> implements Changeable<T>
  {
    @Nonnull
    protected T targetValue;

    public WeakCopyFunctionSupport (final @Nonnull ChangingSource<T> source)
      {
        super(source);
      }

    @Override
    protected void onSourceChange (final @Nonnull T oldSourceValue, final @Nonnull T newSourceValue)
      {
        final T oldValue = function(oldSourceValue);
        final T newValue = function(newSourceValue);

        if (shouldChange(oldValue, newValue))
          {
            value = newValue;
            fireValueChanged(oldValue, newValue);
          }
      }

    @Override
    public void set (final T value)
      {
        this.targetValue = value;
      }

    protected abstract boolean shouldChange (T oldValue, T newValue);

    @Override @Nonnull
    protected T function (final @Nonnull T value)
      {
        return value;
      }
  }
