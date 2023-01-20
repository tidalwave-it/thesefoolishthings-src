/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role;

import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.Finder;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class SimpleCompositeTest
  {
    private final List<String> data = List.of("1", "2", "3", "4", "5");

    @Test
    public void must_produce_valid_Finders()
      {
        // given
        final var underTest = SimpleComposite.ofCloned(data);
        // when
        final var finder1 = underTest.findChildren();
        final var finder2 = finder1.from(3).max(1);
        final var results1 = finder1.results();
        final var results2 = finder2.results();
        // then
        assertThat(results1, is(data));
        assertThat(results2, is(List.of("4")));
      }
}
