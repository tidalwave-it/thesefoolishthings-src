/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
import java.util.List;
import java.util.MissingResourceException;
import com.vaadin.data.Container;
import org.openide.util.NbBundle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A provider of table header labels based on resource bundles. It asks a delegate {@link Container} the property ids
 * and looks them up in the bundleLabel, prefixed by {@code header.}. For instance, if the {@code Container} return the ids
 * {@code firstName}, {@code lastName}, the bundle must contain:
 * 
 * <pre>
 * headerLabel.firstName: First name
 * headerLabel.lastName:  Last name
 * </pre>
 * 
 * @author  Fabrizio Giudici
 * @version $Id: HeaderLabelProvider.java 1039 2011-10-19 07:56:26Z cuccu $
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j 
public class HeaderLabelProvider
  {
    private static final String PREFIX = "headerLabel.";
    
    @Nonnull
    private final Container container;  
    
    @Nonnull
    private final Class<?> bundleClass;
    
    /*******************************************************************************************************************
     *
     * Returns the labels for the table headers.
     * 
     * @return  the labels
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<String> getHeaderLabels() 
      {
        final List<String> headerLabels = new ArrayList<String>();
        
        for (final Object propertyId : container.getContainerPropertyIds())
          {
            final String bundleId = PREFIX + propertyId;
            String label = propertyId.toString();
                    
            try
              {
                label = NbBundle.getMessage(bundleClass, bundleId);
              }
            catch (MissingResourceException e)
              {
                log.warn("Can't find bundle for {} in class {}", bundleId, bundleClass);
              }
            
            headerLabels.add(label);
          }
        
        return headerLabels;
      }
  }  