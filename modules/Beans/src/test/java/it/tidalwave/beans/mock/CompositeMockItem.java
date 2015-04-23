/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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
package it.tidalwave.beans.mock;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class CompositeMockItem
  {
    private String name;
    private boolean nameAvailable;
    private final MockItem1 item1 = new MockItem1();
    private final MockItem2 item2 = new MockItem2();

    public MockItem1 getItem1()
      {
        return item1;
      }

    public MockItem2 getItem2()
      {
        return item2;
      }

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
  }
