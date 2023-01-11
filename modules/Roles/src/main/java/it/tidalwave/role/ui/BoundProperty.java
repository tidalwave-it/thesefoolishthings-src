/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
     * Creates a new {@code BoundProperty} with the given initial value.
     *
     * @param <T>     the type of the property
     * @param value   the initial value
     * @return        the property
     * @since         3.2-ALPHA-2
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> BoundProperty<T> of (@Nonnull final T value)
      {
        return new BoundProperty<>(value);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *  This method fires a property change event associated to {@link #PROP_VALUE}.
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
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public T get()
      {
        return value;
      }

    /*******************************************************************************************************************
     *
     * Binds this property to a {@link ChangingSource}. Every change in the value of the source will be synchronized to
     * the value of this property. If the source is a {@link Changeable} binding will be bidirectional, that is the
     * object will be set to the current value of this property and will be kept in sync.
     *
     * @param     source    the source
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
     * {@inheritDoc}
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