/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import java.util.Comparator;
import it.tidalwave.util.As;
import it.tidalwave.role.ui.Displayable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AsDisplayableComparatorTest
  {
    private final As a  = createAs(new DefaultDisplayable("a", "a"));
    private final As b  = createAs(new DefaultDisplayable("b", "b"));
    private final As c1 = createAs(new DefaultDisplayable("c", "c"));
    private final As c2 = createAs(new DefaultDisplayable("c", "c"));

    @Test(dataProvider = "data")
    public void test (@Nonnull final As a1, @Nonnull final As a2, final int expectedResult)
      {
        // given
        final Comparator<As> underTest = AsDisplayableComparator.getInstance();
        // when
        final int actualResult = underTest.compare(a1, a2);
        // then
        assertThat(actualResult, is(expectedResult));
      }

    @Nonnull
    private static As createAs (@Nonnull final Displayable displayable)
      {
        final As as = mock(As.class);
        when(as.as(eq(Displayable.class))).thenReturn(displayable);

        return as;
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
