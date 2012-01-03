/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.text;

import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.Displayable;
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
    @Test
    public void test()
      {
        final Displayable a  = new DefaultDisplayable("a", "a");
        final Displayable b  = new DefaultDisplayable("b", "b");
        final Displayable c1 = new DefaultDisplayable("c", "c");
        final Displayable c2 = new DefaultDisplayable("c", "c");

        assertThat(DisplayableComparator.getInstance().compare(a, b), is(-1));
        assertThat(DisplayableComparator.getInstance().compare(b, a), is(+1));
        assertThat(DisplayableComparator.getInstance().compare(a, a), is(0));
        assertThat(DisplayableComparator.getInstance().compare(c1, c2), is(0));
        assertThat(DisplayableComparator.getInstance().compare(c2, c1), is(0));
      }
  }
