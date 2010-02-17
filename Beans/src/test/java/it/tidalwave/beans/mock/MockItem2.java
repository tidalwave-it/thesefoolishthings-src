/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.beans.mock;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class MockItem2 
  {
    private String name;
    private boolean nameAvailable;
    
    public void setName (final String name)
      {
        this.name = name;    
      }
    
    public String getName()
      {
        return name;    
      }
    
    public void setNameAvailable (final boolean nameAvailable)
      {
        this.nameAvailable = nameAvailable;    
      }
    
    public boolean isNameAvailable()
      {
        return nameAvailable;    
      }
    
    public boolean isAvailable()
      {
        return nameAvailable;   
      }
    
//    // This is necessary to verify that JavaBeanAspect doesn't use equals()
//    // for replacing inner beans.
//    @Override
//    public boolean equals (final Object object) 
//      {
//        return false;
//      }
  }
