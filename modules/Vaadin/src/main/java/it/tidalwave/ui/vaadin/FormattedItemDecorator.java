/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.ui.vaadin;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.text.Format;
import it.tidalwave.util.AsException;
import it.tidalwave.role.ui.PresentationModel;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FormattedItemDecorator implements Item
  {
//    @Delegate(types=Item.class) @Nonnull

    private final PresentationModel pm;

    @Getter
    private final Item delegate;
    
    private final Map<Object, Format> formatMapByPropertyId = new HashMap<Object, Format>();

    public FormattedItemDecorator (final @Nonnull PresentationModel pm,
                                   final @Nonnull Item delegate,
                                   final @Nonnull Map<Object, Format> formatMapByPropertyId)
      {
        this.pm = pm;
        this.delegate = delegate;
        this.formatMapByPropertyId.putAll(formatMapByPropertyId);
      }

    @Override @Nonnull
    public Property getItemProperty (final @Nonnull Object propertyId)
      {
        Property delegateProperty = delegate.getItemProperty(propertyId);

        if (delegateProperty != null)
          {
            return new FormattedPropertyDecorator(delegateProperty, formatMapByPropertyId.get(propertyId));
          }

        try
          {
            return pm.as(DerivedPropertyFactory.class).getProperty(propertyId);
          }
        catch (AsException e)
          {
            return new ObjectProperty<String>("missing: " + propertyId);
          }
      }

    public boolean removeItemProperty (final Object id)
      {
        return delegate.removeItemProperty(id);
      }

    public Collection<?> getItemPropertyIds()
      {
        return delegate.getItemPropertyIds();
      }

    public boolean addItemProperty (final Object id, final Property property)
      {
        return delegate.addItemProperty(id, property);
      }
  }
