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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Locale;
import it.tidalwave.util.mock.Mock;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 *
 *
 **********************************************************************************************************************/
public class BundleUtilitiesTest
  {
    @Test(dataProvider = "dataTest")
    public void test (final @Nonnull Locale locale,
                      final @Nonnull String resourceName,
                      final @Nonnull Object[] params,
                      final @Nonnull String expectedResult)
      {
        // when
        final String actualResult = BundleUtilities.getMessage(Mock.class, locale, resourceName, params);
        // then
        assertThat(actualResult, is(expectedResult));
      }

    @DataProvider
    private Object[][] dataTest()
      {
        return new Object[][]
          {
            { Locale.US,    "res1", new Object[0],            "message 1"              },
            { Locale.ITALY, "res1", new Object[0],            "messaggio 1"            },
            { Locale.US,    "res2", new Object[] { "x", 1 },  "message 2 with x and 1" },
            { Locale.ITALY, "res2", new Object[] { "x", 1 },  "messaggio 2 con x e 1"  }
          };
      };
  }
