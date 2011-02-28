/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.text;

import javax.annotation.Nonnull;
import it.tidalwave.util.As;
import it.tidalwave.role.DefaultDisplayable;
import it.tidalwave.role.Displayable;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public class AsDisplayableComparatorTest
  {
    @Test
    public void test()
      {
        final As a  = createAs(new DefaultDisplayable("a", "a"));
        final As b  = createAs(new DefaultDisplayable("b", "b"));
        final As c1 = createAs(new DefaultDisplayable("c", "c"));
        final As c2 = createAs(new DefaultDisplayable("c", "c"));

        assertThat(AsDisplayableComparator.getInstance().compare(a, b), is(-1));
        assertThat(AsDisplayableComparator.getInstance().compare(b, a), is(+1));
        assertThat(AsDisplayableComparator.getInstance().compare(a, a), is(0));
        assertThat(AsDisplayableComparator.getInstance().compare(c1, c2), is(0));
        assertThat(AsDisplayableComparator.getInstance().compare(c2, c1), is(0));
      }

    @Nonnull
    private static As createAs (final @Nonnull Displayable displayable)
      {
        final As as = mock(As.class);
        when(as.as(eq(Displayable.class))).thenReturn(displayable);

        return as;
      }
  }
