/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.role.spi.impl;

import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static it.tidalwave.role.spi.impl.Mocks.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DatumAndRoleTest
  {
    @Test(dataProvider = "provider")
    public void testGetSuper (@Nonnull final DatumAndRole underTest, @Nonnull final List<DatumAndRole> expected)
      {
        // when
        final List<DatumAndRole> actual = underTest.getSuper();
        // then
        assertThat(actual, is(expected));
      }

    @DataProvider
    private static Object[][] provider()
      {
        return new Object[][]
          {
              {
                new DatumAndRole(CA1.class, R1.class), asList(new DatumAndRole(CA1.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class))
              },
              {
                new DatumAndRole(CA2.class, R1.class), asList(new DatumAndRole(CA2.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class),
                                                              new DatumAndRole(IA2.class,    R1.class))
              },
              {
                new DatumAndRole(CA3.class, R1.class), asList(new DatumAndRole(CA3.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class),
                                                              new DatumAndRole(IA3.class,    R1.class))
              },
              {
                new DatumAndRole(CB1.class, R1.class), asList(new DatumAndRole(CB1.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class),
                                                              new DatumAndRole(IB2.class,    R1.class),
                                                              new DatumAndRole(IA2.class,    R1.class),
                                                              new DatumAndRole(IA3.class,    R1.class))
              },
              {
                new DatumAndRole(CB2.class, R1.class), asList(new DatumAndRole(CB2.class,    R1.class),
                                                              new DatumAndRole(CA2.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class),
                                                              new DatumAndRole(IA2.class,    R1.class),
                                                              new DatumAndRole(IB1.class,    R1.class),
                                                              new DatumAndRole(IA1.class,    R1.class))
              },
              {
                new DatumAndRole(CB3.class, R1.class), asList(new DatumAndRole(CB3.class,    R1.class),
                                                              new DatumAndRole(CA1.class,    R1.class),
                                                              new DatumAndRole(Object.class, R1.class))
              }
          };
      }
  }
