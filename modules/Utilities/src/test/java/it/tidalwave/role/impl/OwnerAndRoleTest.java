/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.role.impl;

import jakarta.annotation.Nonnull;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static it.tidalwave.util.spi.Mocks.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class OwnerAndRoleTest
  {
    @Test(dataProvider = "provider")
    public void testGetSuper (@Nonnull final OwnerAndRole underTest, @Nonnull final List<OwnerAndRole> expected)
      {
        // when
        final var actual = underTest.getSuper();
        // then
        assertThat(actual, is(expected));
      }

    @DataProvider
    private static Object[][] provider()
      {
        return new Object[][]
          {
              {
                      new OwnerAndRole(CA1.class, R1.class), asList(new OwnerAndRole(CA1.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class))
              },
              {
                      new OwnerAndRole(CA2.class, R1.class), asList(new OwnerAndRole(CA2.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class),
                                                                    new OwnerAndRole(IA2.class, R1.class))
              },
              {
                      new OwnerAndRole(CA3.class, R1.class), asList(new OwnerAndRole(CA3.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class),
                                                                    new OwnerAndRole(IA3.class, R1.class))
              },
              {
                      new OwnerAndRole(CB1.class, R1.class), asList(new OwnerAndRole(CB1.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class),
                                                                    new OwnerAndRole(IB2.class, R1.class),
                                                                    new OwnerAndRole(IA2.class, R1.class),
                                                                    new OwnerAndRole(IA3.class, R1.class))
              },
              {
                      new OwnerAndRole(CB2.class, R1.class), asList(new OwnerAndRole(CB2.class, R1.class),
                                                                    new OwnerAndRole(CA2.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class),
                                                                    new OwnerAndRole(IA2.class, R1.class),
                                                                    new OwnerAndRole(IB1.class, R1.class),
                                                                    new OwnerAndRole(IA1.class, R1.class))
              },
              {
                      new OwnerAndRole(CB3.class, R1.class), asList(new OwnerAndRole(CB3.class, R1.class),
                                                                    new OwnerAndRole(CA1.class, R1.class),
                                                                    new OwnerAndRole(Object.class, R1.class))
              }
          };
      }
  }
