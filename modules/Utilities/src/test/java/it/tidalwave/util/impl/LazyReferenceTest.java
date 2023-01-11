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
package it.tidalwave.util.impl;

import java.util.stream.IntStream;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class LazyReferenceTest
  {
    @Test
    public void must_not_call_supplier_before_get()
      {
        // when
        final LazyReference<Object> underTest = LazyReference.of(Object::new);
        // then
        assertThat(underTest.ref, is((Object)null));
      }

    @Test
    public void must_call_supplier_only_once()
      {
        // given
        final LazyReference<Object> underTest = LazyReference.of(Object::new);
        // when
        final Object o1 = underTest.get();
        final Object o2 = underTest.get();
        final Object o3 = underTest.get();
        // then
        assertThat(o2, sameInstance(o1));
        assertThat(o3, sameInstance(o1));
      }

    @Test
    public void must_call_supplier_only_once_multithreaded()
      {
        // given
        final LazyReference<Object> underTest = LazyReference.of(Object::new);
        // when
        final long count = IntStream.range(0, 10_000_000).parallel().mapToObj(__ -> underTest.get()).distinct().count();
        // then
        assertThat(count, is(1L));
      }
  }
