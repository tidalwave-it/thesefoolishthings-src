/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.text.Format;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.Composite;
import it.tidalwave.role.ui.PresentationModel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An adapter between {@link PresentationModel} and {@link Container}.
 * 
 * @stereotype Adapter
 * 
 * @author  Fabrizio Giudici
 * @author based on LazyQyeryFinder by Tommi S.E. Laukkanen
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ReadOnlyPresentationModelContainer extends IndexedContainerSupport 
  {
    private static final long serialVersionUID = 345346356345349283L;
    
    // TODO: move to Finder
    private static final Finder<PresentationModel> EMPTY_FINDER = new SimpleFinderSupport<PresentationModel>("EMPTY_FINDER") 
      {
        @Override  @Nonnull
        protected List<? extends PresentationModel> computeResults()
          {
            return Collections.emptyList();
          }
      };
    
    @Nonnull
    private final Finder<PresentationModel> finder;
    
    private final int batchSize = 20;

    private final Map<Object, Format> formatMapByPropertyId = new HashMap<Object, Format>();    
    
    /*******************************************************************************************************************
     *
     * Creates an empty instance for the given datum type.
     * 
     * @param  datumType   the datum type
     *
     ******************************************************************************************************************/
    public ReadOnlyPresentationModelContainer (final @Nonnull Class<?> datumType)
      {
        super(datumType);
        this.finder = EMPTY_FINDER;
      }

    /*******************************************************************************************************************
     *
     * Creates an instance for the given {@link PresentationModel} and datum type.
     * 
     * @param  presentationModel   the {@code PresentationModel}
     * @param  datumType           the datum type
     *
     ******************************************************************************************************************/
    public ReadOnlyPresentationModelContainer (final @Nonnull PresentationModel presentationModel,
                                               final @Nonnull Class<?> datumType) 
      {
        super(datumType);
        this.finder = presentationModel.as(Composite.class).findChildren();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public final int size() 
      {
        return finder.count();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public final Item getItem (final @Nonnull Object itemId)
      {
        final int index = (Integer) itemId;
        final int baseIndex = index - index % batchSize;
        final PresentationModel pm = finder.from(baseIndex).max(batchSize).results().get(index - baseIndex);
        final Object object = pm.as(datumType);
        return new FormattedItemDecorator(pm, new BeanItem<Object>(object), formatMapByPropertyId);
      }
    
    public void registerPropertyFormat (final @Nonnull Object propertyId, final @Nonnull Format format)
      {
        formatMapByPropertyId.put(propertyId, format);      
      }
  }