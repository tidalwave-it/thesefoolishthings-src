/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.ui.vaadin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/***********************************************************************************************************************
 *
 * A support, partial implementation of an {@link Indexed} {@link Container}.
 * 
 * @stereotype Adapter
 * 
 * @author  Fabrizio Giudici
 * @author  based on LazyQyeryFinder by Tommi S.E. Laukkanen
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class IndexedContainerSupport implements Container, Indexed
  {
    private static final long serialVersionUID = 65363534234233L;
    
    @Nonnull
    protected final Class<?> datumType;
    
    private final List<Object> propertyIds = new ArrayList<Object>();
            
    private final Map<Object, Class> propertyTypeMap = new HashMap<Object, Class>();

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public IndexedContainerSupport (final @Nonnull Class<?> datumType)
      {
        this.datumType = datumType;
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
      {
        propertyIds.add(propertyId);
        propertyTypeMap.put(propertyId, type);
        return false; // TODO
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public boolean removeContainerProperty (final @Nonnull Object propertyId) 
      {
        propertyIds.remove(propertyId);
        propertyTypeMap.remove(propertyId);
        return false; // TODO
      }
    
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Collection<?> getContainerPropertyIds() 
      {
        return new CopyOnWriteArrayList<Object>(propertyIds);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Class<?> getType (final @Nonnull Object propertyId) 
      {
        return propertyTypeMap.get(propertyId);
      } 

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Collection<?> getItemIds() 
      {
        final List<Object> result = new ArrayList<Object>();
        final int count = size() - 1;
        
        for (int i = 0; i < count; i++)
          {
            result.add(i);
          }
        
        return result;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Property getContainerProperty (final Object itemId, final Object propertyId)
      {
        return getItem(itemId).getItemProperty(propertyId);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object getIdByIndex (final int index) 
      {
        return index;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final int indexOfId (final Object itemId) 
      {
        return (Integer) itemId;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final boolean containsId (final Object itemId)
      {
        if (itemId.getClass() == Integer.class) 
          {
            final int index = (Integer)itemId;
            return (index >= 0) && (index < size());
          }
        else 
          {
            return false;
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final boolean isFirstId (final Object itemId) 
      {
        if (itemId.getClass() == Integer.class) 
          {
            return (Integer) itemId == 0;
          }
        else
          {
            return false;
          }          
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final boolean isLastId (final Object itemId)
      {
        if (itemId.getClass() == Integer.class) 
          {
            return (Integer) itemId == size() - 1;
          }
        else 
          {
            return false;
          }        
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object firstItemId() 
      {
        return 0;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object lastItemId() 
      {
        return size() - 1;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object nextItemId (final Object itemId)
      {
        return (Integer)itemId + 1;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object prevItemId (final Object itemId)
      {
        return (Integer)itemId - 1;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object addItemAt (final int index) 
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object addItemAfter (final Object previousItemId)
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Item addItemAt (final int index, final Object newItemId) 
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Item addItemAfter (final Object previousItemId, final Object newItemId) 
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Item addItem (final Object itemId) 
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final Object addItem() 
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final boolean removeItem (final Object itemId)
      {
        throw new UnsupportedOperationException();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public final boolean removeAllItems()
      {
        throw new UnsupportedOperationException();
      }
  }