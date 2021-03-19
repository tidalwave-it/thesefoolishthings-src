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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// FIXME: weak listeners
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(exclude="pcs") @ToString(exclude="pcs")
public class BoundProperty<T> implements ChangingSource<T>, Changeable<T>
  {
    @Delegate
    private final transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String PROP_VALUE = "value";

    private T value;

    /*******************************************************************************************************************
     *
     * @since 3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> BoundProperty<T> of (@Nonnull final T value)
      {
        return new BoundProperty<>(value);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void set (final T value)
      {
        final T oldValue = this.value;
        this.value = value;
        pcs.firePropertyChange(PROP_VALUE, oldValue, value);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public T get()
      {
        return value;
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull final ChangingSource<T> source)
      {
        source.addPropertyChangeListener(event -> set((T)event.getNewValue()));

        if (source instanceof Changeable)
          {
            final Changeable<T> changeable = (Changeable<T>)source;
            changeable.set(value);
            this.addPropertyChangeListener(event -> changeable.set((T)event.getNewValue()));
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void unbindAll()
      {
        for (final PropertyChangeListener listener : pcs.getPropertyChangeListeners().clone())
          {
            pcs.removePropertyChangeListener(listener);
          }
      }
  }