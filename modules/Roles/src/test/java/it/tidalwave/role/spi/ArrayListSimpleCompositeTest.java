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
package it.tidalwave.role.spi;

import it.tidalwave.util.Finder;
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ArrayListSimpleCompositeTest
  {
    private final List<String> data = Arrays.asList("1", "2", "3", "4", "5");

    @Test
    public void must_produce_valid_Finders()
      {
        final ArrayListSimpleComposite<String> underTest = new ArrayListSimpleComposite<>(data);
        final Finder<String> finder1 = underTest.findChildren();
        final Finder<String> finder2 = finder1.from(3).max(1);

        assertThat((List<String>)finder1.results(), is(data));
        assertThat((List<String>)finder2.results(), is(Arrays.asList("4")));
      }
}
