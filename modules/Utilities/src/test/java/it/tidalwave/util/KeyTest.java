/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
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
package it.tidalwave.util;

import java.util.Date;
import javax.annotation.Nonnull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 *
 **********************************************************************************************************************/
public class KeyTest
  {
    @Test(dataProvider = "keysAndExpectedTypes")
    public <T> void must_return_the_correct_dynamic_type (final @Nonnull Key<T> key, final @Nonnull Class<T> expectedType)
      {
        // when
        final Class<T> actualType = key.getType();
        // then
        assertEquals(actualType, expectedType);
      }

    @DataProvider
    private static Object[][] keysAndExpectedTypes()
      {
        return new Object[][]
          {
            { new Key<String>("string") ,  String.class  },
            { new Key<Integer>("integer"), Integer.class },
            { new Key<Date>("date"),       Date.class    },
          };
      }
  }
