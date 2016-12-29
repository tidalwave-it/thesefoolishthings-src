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
package it.tidalwave.util.spi;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import lombok.NoArgsConstructor;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class FinderSupportTest
  {
    @NoArgsConstructor
    public static class UnderTest extends FinderSupport<String, UnderTest>
      {
        private static final long serialVersionUID = -886814904140064237L;

        public UnderTest (final UnderTest other, final Object override)
          {
            super(other, override);
          }

        @Override
        protected List<? extends String> computeResults()
          {
            final List<String> result = new ArrayList<>();

            for (int i = 0; i < 100; i++)
              {
                result.add("" + i);
              }

            return result;
          }
      }

    @Test
    public void fix_for_THESEFOOLISHTHINGS_172()
      {
        // given
        final UnderTest underTest = new UnderTest();
        // when
        List<? extends String> results = underTest.from(11).results();
        // then
        assertThat(results.size(), is(100 - 11));
      }

    @Test
    public void fix_for_THESEFOOLISHTHINGS_176()
      {
        // given
        final UnderTest underTest = new UnderTest();
        // when
        List<? extends String> results = underTest.from(101).results();
        // then
        assertThat(results.size(), is(0));
      }
  }
