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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.List;
import it.tidalwave.util.NotFoundException;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static java.util.Arrays.asList;
import static it.tidalwave.role.spi.impl.Hierarchy1.*;

class UnderTest extends RoleManagerSupport
  {
    @Override
    protected <T> T getBean (Class<T> beanType) 
      {
        throw new UnsupportedOperationException("Not supported yet."); 
      }

    @Override
    protected Class<?> findContextForRole (Class<?> roleImplementationClass) 
      throws NotFoundException 
      {
        throw new UnsupportedOperationException("Not supported yet."); 
      }

    @Override
    protected Class<?>[] findDatumTypesForRole (Class<?> roleImplementationClass)
      {
        throw new UnsupportedOperationException("Not supported yet."); 
      }
  }

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RoleManagerSupportTest
  {
    @Test(dataProvider = "provider")
    public void testFindAllImplementedInterfacesOf (final @Nonnull Class<?> clazz, final @Nonnull List<Class<?>> expected)
      {
        RoleManagerSupport.findAllImplementedInterfacesOf(clazz);
      }
    
    @DataProvider(name = "provider")
    private static Object[][] provider()
      {
        return new Object[][]
          {
              {
                CA1.class, asList()
              },
              {
                CA2.class, asList(IA2.class)
              },
              {
                CA3.class, asList(IA3.class)
              },
              {
                CB1.class, asList(IB2.class, IA2.class, IA3.class)
              },
              {
                CB2.class, asList(IA2.class, IB1.class, IA1.class)
              },
              {
                CB3.class, asList()
              }
          };
      }
  }