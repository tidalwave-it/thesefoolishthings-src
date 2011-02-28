/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.swing.beansbinding.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jdesktop.beansbinding.Converter;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class CommaSeparatedListConverter extends Converter<Collection<String>, String>
  {
    @Override
    public String convertForward (final Collection<String> collection) 
      {
        if (collection == null)
          {
            return null;
          }
        
        final StringBuilder builder = new StringBuilder();
        
        for (final String s : collection)
          {
            if (builder.length() > 0)
              {
                builder.append(", ");  
              }
            
            builder.append(s);
          }
        
        return builder.toString();
      }

    @Override
    public Collection<String> convertReverse (final String string) 
      {
        if (string == null)
          {
            return null;
          }
        
        if (string.trim().equals(""))
          {
            return Collections.<String>emptyList();  
          }
        
        final List<String> result = new ArrayList<String>();
        final String[] split = string.split(",");
        
        for (final String s : split)
          {
            result.add(s.trim());
          } 
        
        return result;
      }
  }
