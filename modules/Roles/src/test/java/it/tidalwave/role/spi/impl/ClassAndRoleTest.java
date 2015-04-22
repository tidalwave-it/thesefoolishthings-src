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
package it.tidalwave.role.spi.impl;

import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

interface IA1 { }
interface IA2 { }
interface IA3 { }

interface IB1 extends IA1 { }
interface IB2 extends IA2, IA3 { }
interface IB3 { }

class CA1 { }
class CA2 implements IA2 { }
class CA3 implements IA3 { }

class CB1 implements IB2 { }
class CB2 extends CA2 implements IB1 { }
class CB3 extends CA1 { }

interface R1 { }

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ClassAndRoleTest
  {
    @Test(dataProvider = "provider")
    public void testGetSuper (final @Nonnull ClassAndRole underTest, final @Nonnull List<ClassAndRole> expected)
      {
        final List<ClassAndRole> actual = underTest.getSuper();
        assertThat(actual, is(expected));
      }

    @DataProvider(name = "provider")
    private static Object[][] provider()
      {
        return new Object[][]
          {
              {
                new ClassAndRole(CA1.class, R1.class), asList(new ClassAndRole(CA1.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class))
              },
              {
                new ClassAndRole(CA2.class, R1.class), asList(new ClassAndRole(CA2.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class),
                                                              new ClassAndRole(IA2.class,    R1.class))
              },
              {
                new ClassAndRole(CA3.class, R1.class), asList(new ClassAndRole(CA3.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class),
                                                              new ClassAndRole(IA3.class,    R1.class))
              },
              {
                new ClassAndRole(CB1.class, R1.class), asList(new ClassAndRole(CB1.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class),
                                                              new ClassAndRole(IB2.class,    R1.class),
                                                              new ClassAndRole(IA2.class,    R1.class),
                                                              new ClassAndRole(IA3.class,    R1.class))
              },
              {
                new ClassAndRole(CB2.class, R1.class), asList(new ClassAndRole(CB2.class,    R1.class),
                                                              new ClassAndRole(CA2.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class),
                                                              new ClassAndRole(IA2.class,    R1.class),
                                                              new ClassAndRole(IB1.class,    R1.class),
                                                              new ClassAndRole(IA1.class,    R1.class))
              },
              {
                new ClassAndRole(CB3.class, R1.class), asList(new ClassAndRole(CB3.class,    R1.class),
                                                              new ClassAndRole(CA1.class,    R1.class),
                                                              new ClassAndRole(Object.class, R1.class))
              }
          };
      }
  }
