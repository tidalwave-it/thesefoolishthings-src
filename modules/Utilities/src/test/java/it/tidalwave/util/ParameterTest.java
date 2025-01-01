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
package it.tidalwave.util;

import java.util.Collection;
import java.util.List;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class ParameterTest
  {
    @Test
    public void test()
      {
        // given
        final Object r1 = "r1";
        final Object r2 = "r2";
        final Object r3 = "r3";
        final Object r4 = "r4";
        final Object r5 = "r5";
        final Object r6 = "r6";
        final Object r7 = "r7";
        final Object r8 = "r8";
        final Object r9 = "r9";

        final Collection<Object> rc1 = List.of(r4, r5);
        final Collection<Object> rc2 = List.of(r7, r8);
        // when
        final var result = Parameters.r(r1, r2, r3, rc1, r6, rc2, r9);
        // then
        final Collection<Object> expected = List.of(r1, r2, r3, r4, r5, r6, r7, r8, r9);
        assertThat(result, is(expected));
      }
  }
