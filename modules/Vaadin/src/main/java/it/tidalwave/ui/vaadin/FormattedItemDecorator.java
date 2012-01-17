/***********************************************************************************************************************
 *
 * blueBill Stats
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://bluebill.tidalwave.it/mobile/
 * SCM: http://java.net/hg/bluebill-server~stats-src
 *
 **********************************************************************************************************************/
package it.tidalwave.ui.vaadin;

import java.util.Collection;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.text.Format;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import lombok.Delegate;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FormattedItemDecorator implements Item 
  {
//    @Delegate(types=Item.class) @Nonnull
    private final Item delegate;
    
    private final Map<Object, Format> formatMapByPropertyId = new HashMap<Object, Format>();

    public FormattedItemDecorator (final @Nonnull Item delegate, final @Nonnull Map<Object, Format> formatMapByPropertyId)
      {
        this.delegate = delegate;
        this.formatMapByPropertyId.putAll(formatMapByPropertyId);
      }   
    
    @Override @Nonnull
    public Property getItemProperty (final @Nonnull Object propertyId) 
      {
        return new FormattedPropertyDecorator(delegate.getItemProperty(propertyId), formatMapByPropertyId.get(propertyId));
      }

    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        return delegate.removeItemProperty(id);
    }

    public Collection<?> getItemPropertyIds() {
        return delegate.getItemPropertyIds();
    }

    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        return delegate.addItemProperty(id, property);
    }
  }
