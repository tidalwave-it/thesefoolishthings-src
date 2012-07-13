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
import javax.annotation.Nullable;
import java.text.Format;
import com.vaadin.data.Property;
import lombok.Delegate;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class FormattedPropertyDecorator implements Property
  {
    @Delegate(types=Property.class) @Nonnull
    private final Property delegate;
    
    @Nullable 
    private final Format format;
 
    @Override @Nonnull
    public String toString() 
      {
        if (delegate == null) 
          {
            return "null property";
          }

        final Object value = delegate.getValue();
        
        return (format != null) ? format.format(value) : (value != null) ? value.toString() : "";
      }
  }
