/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role
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
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.Displayable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Comparator;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DisplayableComparatorTest
  {
    private final Displayable a  = new DefaultDisplayable("a", "a");
    private final Displayable b  = new DefaultDisplayable("b", "b");
    private final Displayable c1 = new DefaultDisplayable("c", "c");
    private final Displayable c2 = new DefaultDisplayable("c", "c");

    @Test(dataProvider = "data")
    public void test (@Nonnull final Displayable d1, @Nonnull final Displayable d2, final int expectedResult)
      {
        // given
        final Comparator<Displayable> underTest = Displayable.comparing();
        // when
        final int actualResult = underTest.compare(d1, d2);
        // then
        assertThat(actualResult, is(expectedResult));
      }

    @DataProvider
    private Object[][] data()
      {
        return new Object[][]
          {
            {  a,  b, -1 },
            {  b,  a, +1 },
            {  a,  a,  0 },
            { c1, c2,  0 },
            { c2, c1,  0 }
          };
      }
  }
