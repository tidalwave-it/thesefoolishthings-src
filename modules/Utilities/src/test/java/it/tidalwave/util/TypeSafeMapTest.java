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

import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.fail;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class TypeSafeMapTest
  {
    private static final Key<String> K_STRING = Key.of("string", String.class);
    private static final Key<String> K_STRING2 = Key.of("string2", String.class);
    private static final Key<Integer> K_INTEGER = Key.of("integer", Integer.class);
    private static final Key<LocalDateTime> K_DATETIME = Key.of("datetime", LocalDateTime.class);
    public static final LocalDateTime LOCAL_DATE = LocalDateTime.now();

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_newInstance()
      {
        // when
        final TypeSafeMap underTest = TypeSafeMap.newInstance();
        // then
        assertThat(underTest.getSize(), is(0));
        assertThat(underTest.getKeys(), is(Collections.emptySet()));
        assertThat(underTest.asMap(), is(Collections.emptyMap()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_ofCloned()
            throws NotFoundException
      {
        // given
        final Map<Key<?>, Object> map = createSampleMap();
        // when
        final TypeSafeMap underTest = TypeSafeMap.ofCloned(map);
        // then
        assertThat(underTest.getSize(), is(3));
        assertThat(underTest.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING), is("1"));
        assertThat(underTest.get(K_INTEGER), is(2));
        assertThat(underTest.get(K_DATETIME), is(LOCAL_DATE));

        try
          {
            underTest.get(K_STRING2);
            fail("Must throw NotFoundException");
          }
        catch (NotFoundException e)
          {
            // ok
          }

        assertThat(underTest.getOptional(K_STRING), is(Optional.of("1")));
        assertThat(underTest.getOptional(K_INTEGER), is(Optional.of(2)));
        assertThat(underTest.getOptional(K_DATETIME), is(Optional.of(LOCAL_DATE)));
        assertThat(underTest.getOptional(K_STRING2), is(Optional.empty()));
        assertThat(underTest.containsKey(K_STRING), is(true));
        assertThat(underTest.containsKey(K_INTEGER), is(true));
        assertThat(underTest.containsKey(K_DATETIME), is(true));
        assertThat(underTest.containsKey(K_STRING2), is(false));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_with()
            throws NotFoundException
      {
        // given
        final Map<Key<?>, Object> map = createSampleMap();
        final TypeSafeMap firstMap = TypeSafeMap.ofCloned(map);
        // when
        final TypeSafeMap underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest, is(not(sameInstance(firstMap))));
        assertThat(firstMap.getSize(), is(3));
        assertThat(underTest.getSize(), is(4));
        assertThat(firstMap.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        assertThat(underTest.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME, K_STRING2))));
        map.put(K_STRING2, "2");
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING2), is("2"));
        assertThat(underTest.getOptional(K_STRING2), is(Optional.of("2")));
        assertThat(underTest.containsKey(K_STRING2), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void asMap_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final Map<Key<?>, Object> map = createSampleMap();
        // when
        final TypeSafeMap underTest = TypeSafeMap.ofCloned(map);
        final Map<Key<?>, Object> map1 = underTest.asMap();
        final Map<Key<?>, Object> map2 = underTest.asMap();
        map1.clear();
        // then
        assertThat(map1, is(not(sameInstance(map2))));
        assertThat(underTest.getSize(), is(not(0)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void getKeys_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final Map<Key<?>, Object> map = createSampleMap();
        // when
        final TypeSafeMap underTest = TypeSafeMap.ofCloned(map);
        final Set<Key<?>> set1 = underTest.getKeys();
        final Set<Key<?>> set2 = underTest.getKeys();
        set1.clear();
        // then
        assertThat(set1, is(not(sameInstance(set2))));
        assertThat(underTest.getSize(), is(not(0)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Map<Key<?>, Object> createSampleMap ()
      {
        final Map<Key<?>, Object> map = new HashMap<>();
        map.put(K_STRING, "1");
        map.put(K_INTEGER, 2);
        map.put(K_DATETIME, LOCAL_DATE);
        return map;
      }
  }
