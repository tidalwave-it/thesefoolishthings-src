/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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

import java.util.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ConcurrentHashMapWithOptionalsTest
  {
    private ConcurrentHashMapWithOptionals<String, String> underTest;

    @BeforeMethod
    public void setup()
      {
        underTest = new ConcurrentHashMapWithOptionals<>();
      }

    @Test
    public void must_return_the_new_key_when_a_new_pair_is_put_1()
      {
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey("key", "new value");
        // then
        assertThat(underTest.containsKey("key"), is(true));
        assertThat(underTest.get("key"), is("new value"));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("key"));
      }

    @Test
    public void must_return_empty_Optional_when_no_new_pair_is_put_1()
      {
        // given
        underTest.put("key", "old value");
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey("key", "new value");
        // then
        assertThat(underTest.containsKey("key"), is(true));
        assertThat(underTest.get("key"), is("old value"));
        assertThat(result.isPresent(), is(false));
      }

    @Test
    public void must_return_the_new_key_when_a_new_pair_is_put_2()
      {
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey(Optional.of("key"), "new value");
        // then
        assertThat(underTest.containsKey("key"), is(true));
        assertThat(underTest.get("key"), is("new value"));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("key"));
      }

    @Test
    public void must_return_empty_Optional_when_no_new_pair_is_put_2()
      {
        // given
        underTest.put("key", "old value");
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey(Optional.of("key"), "new value");
        // then
        assertThat(underTest.containsKey("key"), is(true));
        assertThat(underTest.get("key"), is("old value"));
        assertThat(result.isPresent(), is(false));
      }

    @Test
    public void must_return_the_new_key_when_a_new_pair_is_put_3()
      {
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey(Optional.empty(), "new value");
        // then
        assertThat(underTest.containsKey("key"), is(false));
        assertThat(result.isPresent(), is(false));
      }

    @Test
    public void must_return_empty_Optional_when_no_new_pair_is_put_3()
      {
        // given
        underTest.put("key", "old value");
        // when
        final Optional<String> result = underTest.putIfAbsentAndGetNewKey(Optional.empty(), "new value");
        // then
        assertThat(underTest.containsKey("key"), is(true));
        assertThat(underTest.get("key"), is("old value"));
        assertThat(result.isPresent(), is(false));
      }
  }
