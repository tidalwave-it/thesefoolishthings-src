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
package it.tidalwave.util;

import java.util.List;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ShortNamesTest
  {
    static interface AnInterface {}

    static interface AnotherInterface {}

    static class AClass implements AnInterface, AnotherInterface, Serializable {}

    @RequiredArgsConstructor @Getter
    static class WithId
      {
        private final String id;
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortName()
      {
        // given
        final var actualValue = ShortNames.shortName(String.class);
        assertThat(actualValue, is("j.l.String"));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortNames()
      {
        // when
        final var actualValue = ShortNames.shortNames(List.of(String.class, List.class));
        // then
        assertThat(actualValue, is("[j.l.String, j.u.List]"));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortName_expand_interface()
      {
        // when
        final var actualValue = ShortNames.shortName(AClass.class, true);
        // then
        assertThat(actualValue, is(
        "i.t.u.ShortNamesTest$AClass{i.t.u.ShortNamesTest$AnInterface, i.t.u.ShortNamesTest$AnotherInterface}"));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortId_1()
      {
        // given
        final var object = new String();
        // when
        final var actualValue = ShortNames.shortId(object);
        // then
        assertThat(actualValue, is(String.format("j.l.String@%x", System.identityHashCode(object))));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortId_2()
      {
        // given
        final var object = new WithId("foobar");
        // when
        final var actualValue = ShortNames.shortId(object);
        // then
        assertThat(actualValue, is(String.format("i.t.u.ShortNamesTest$WithId@%x/foobar",
                                                 System.identityHashCode(object))));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortId_withNull()
      {
        // when
        final var actualValue = ShortNames.shortId(null);
        // then
        assertThat(actualValue, is("null"));
      }

    /******************************************************************************************************************/
    @Test
    public void test_shortIds()
      {
        // given
        final var object1 = new WithId("foo");
        final var object2 = new WithId("bar");
        // when
        final var actualValue = ShortNames.shortIds(List.of(object1, object2));
        // then
        assertThat(actualValue, is(
            String.format("[i.t.u.ShortNamesTest$WithId@%x/foo, i.t.u.ShortNamesTest$WithId@%x/bar]",
                          System.identityHashCode(object1), System.identityHashCode(object2))));
      }
  }
