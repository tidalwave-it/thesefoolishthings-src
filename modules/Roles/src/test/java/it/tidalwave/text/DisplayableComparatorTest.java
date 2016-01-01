/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.text;

import javax.annotation.Nonnull;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.Displayable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public class DisplayableComparatorTest
  {
    final Displayable a  = new DefaultDisplayable("a", "a");
    final Displayable b  = new DefaultDisplayable("b", "b");
    final Displayable c1 = new DefaultDisplayable("c", "c");
    final Displayable c2 = new DefaultDisplayable("c", "c");

    @Test(dataProvider = "data")
    public void test (final @Nonnull Displayable d1, final @Nonnull Displayable d2, final int expectedResult)
      {
        // when
        final int actualResult = DisplayableComparator.getInstance().compare(d1, d2);
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
