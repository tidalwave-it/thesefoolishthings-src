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
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.spi.impl.DatumAndRole;
import it.tidalwave.role.spi.impl.MultiMap;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static java.util.Arrays.asList;
import static it.tidalwave.role.spi.impl.Hierarchy1.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

class XCA1 extends CA1 {}
class YCA1 extends XCA1 {}

class UnderTest extends RoleManagerSupport
  {
    private final Map<Class<?>, Class<?>[]> map = new HashMap<>();
    
    public void register (final @Nonnull Class<?> roleClass, final @Nonnull Class<?> ... ownerClasses)
      {
        map.put(roleClass, ownerClasses);  
      }
    
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
    protected Class<?>[] findDatumTypesForRole (final Class<?> roleImplementationClass)
      {
        final Class<?>[] result = map.get(roleImplementationClass);       
        return (result == null) ? new Class<?>[0] : result;
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
    
    @Test
    public void testRegisteredRoles()
      {
        final UnderTest underTest = new UnderTest();
        
        underTest.register(RI1A.class, CA1.class, CA2.class);
        underTest.register(RI3A.class, CA3.class);
        underTest.register(RI2B.class, CB1.class, CB3.class);
        underTest.register(RI2A.class, CA1.class, CB3.class);
        underTest.register(RI3C.class, IA1.class);
        underTest.register(RI2C.class, IA2.class);
        
        underTest.scan(asList(RI1A.class, RI1B.class, RI1C.class, 
                              RI2A.class, RI2B.class, RI2C.class, 
                              RI3A.class, RI3B.class, RI3C.class));
        
        final MultiMap<DatumAndRole, Class<?>> m = underTest.roleMapByDatumAndRole;
        
        assertThat(m.size(), is(8));
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));
        
        // The following were not mapped now, but will be discovered later:
        // CA2 -> R12C 
        // CB1 -> R12C 
        // CB2 -> RI1A, R12C, R13C 
        // CB3 -> R11A 

        assertEquals(underTest.findRoleImplementationsFor(CA1.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(CA1.class, R2.class), asSet(RI2A.class));
        assertEquals(underTest.findRoleImplementationsFor(CA1.class, R3.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CA2.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(CA2.class, R2.class), asSet(RI2C.class));
        assertEquals(underTest.findRoleImplementationsFor(CA2.class, R3.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CA3.class, R1.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CA3.class, R2.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CA3.class, R3.class), asSet(RI3A.class));
        assertEquals(underTest.findRoleImplementationsFor(CB1.class, R1.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CB1.class, R2.class), asSet(RI2B.class, RI2C.class));
        assertEquals(underTest.findRoleImplementationsFor(CB1.class, R3.class), asSet());
        assertEquals(underTest.findRoleImplementationsFor(CB2.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(CB2.class, R2.class), asSet(RI2C.class));
        assertEquals(underTest.findRoleImplementationsFor(CB2.class, R3.class), asSet(RI3C.class));
        assertEquals(underTest.findRoleImplementationsFor(CB3.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(CB3.class, R2.class), asSet(RI2A.class, RI2B.class));
        assertEquals(underTest.findRoleImplementationsFor(CB3.class, R3.class), asSet());

          System.err.println(m);
        assertThat(m.size(), is(13));
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));
        
        // These were not explicitly registered - late discovery
        assertEquals(underTest.findRoleImplementationsFor(XCA1.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(XCA1.class, R2.class), asSet(RI2A.class));
        assertEquals(underTest.findRoleImplementationsFor(XCA1.class, R3.class), asSet());
        
        assertThat(m.size(), is(15));       
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));
        
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R2.class)), is(asSet(RI2A.class)));
        
        // These were not explicitly registered - late discovery
        assertEquals(underTest.findRoleImplementationsFor(YCA1.class, R1.class), asSet(RI1A.class));
        assertEquals(underTest.findRoleImplementationsFor(YCA1.class, R2.class), asSet(RI2A.class));
        assertEquals(underTest.findRoleImplementationsFor(YCA1.class, R3.class), asSet());
        
        assertThat(m.size(), is(17));
        assertThat(m.getValues(new DatumAndRole(IA1.class,  R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class,  R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class,  R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class,  R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class,  R2.class)), is(asSet(RI2A.class, RI2B.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R2.class)), is(asSet(RI2A.class)));

        assertThat(m.getValues(new DatumAndRole(YCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(YCA1.class, R2.class)), is(asSet(RI2A.class)));
      }
    
    // TODO: test role creations (findRoles())
    
    private static void assertEquals (Set l1, Set l2)
      {
        assertThat(l1, is(l2));
      }
    
    @Nonnull
    private static Set<Class<?>> asSet (final Class<?> ... objects)
      {
        return new HashSet<>(asList(objects));
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