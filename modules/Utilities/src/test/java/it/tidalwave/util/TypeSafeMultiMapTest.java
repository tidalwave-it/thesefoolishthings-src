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

import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class TypeSafeMultiMapTest
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
        final TypeSafeMultiMap underTest = TypeSafeMultiMap.newInstance();
        // then
        assertThat(underTest.size(), is(0));
        assertThat(underTest.keySet(), is(Collections.emptySet()));
        assertThat(underTest.asMap(), is(Collections.emptyMap()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_ofCloned()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        // when
        final TypeSafeMultiMap underTest = TypeSafeMultiMap.ofCloned(map);
        // then
        assertThat(underTest.size(), is(3));
        assertThat(underTest.keySet(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING), is(Collections.singletonList("1")));
        assertThat(underTest.get(K_INTEGER), is(Collections.singletonList(2)));
        assertThat(underTest.get(K_DATETIME), is(Collections.singletonList(LOCAL_DATE)));
        assertThat(underTest.get(K_STRING2), is(Collections.emptyList()));
        assertThat(underTest.containsKey(K_STRING), is(true));
        assertThat(underTest.containsKey(K_INTEGER), is(true));
        assertThat(underTest.containsKey(K_DATETIME), is(true));
        assertThat(underTest.containsKey(K_STRING2), is(false));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_with_from_empty_map()
      {
        // given
        final TypeSafeMultiMap firstMap = TypeSafeMultiMap.newInstance();
        // when
        final TypeSafeMultiMap underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest, is(not(sameInstance(firstMap))));
        assertThat(firstMap.size(), is(0));
        assertThat(underTest.size(), is(1));
        assertThat(firstMap.keySet(), is(Collections.emptySet()));
        assertThat(underTest.keySet(), is(Collections.singleton(K_STRING2)));
        final Map<Key<?>, Object> map = new HashMap<>();
        map.put(K_STRING2, Collections.singletonList("2"));
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING2), is(Collections.singletonList("2")));
        assertThat(underTest.containsKey(K_STRING2), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_with()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        final TypeSafeMultiMap firstMap = TypeSafeMultiMap.ofCloned(map);
        // when
        final TypeSafeMultiMap underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest, is(not(sameInstance(firstMap))));
        assertThat(firstMap.size(), is(3));
        assertThat(underTest.size(), is(4));
        assertThat(firstMap.keySet(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        assertThat(underTest.keySet(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME, K_STRING2))));
        map.put(K_STRING2, Collections.singletonList("2"));
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING2), is(Collections.singletonList("2")));
        assertThat(underTest.containsKey(K_STRING2), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_with_and_existing_key()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        final TypeSafeMultiMap firstMap = TypeSafeMultiMap.ofCloned(map);
        // when
        final TypeSafeMultiMap underTest = firstMap.with(K_STRING, "1+"); // STRING key is already present
        // then
        assertThat(underTest, is(not(sameInstance(firstMap))));
        assertThat(firstMap.size(), is(3));
        assertThat(underTest.size(), is(3));
        assertThat(firstMap.keySet(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        assertThat(underTest.keySet(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, K_DATETIME))));
        map.put(K_STRING, Arrays.asList("1", "1+"));
        assertThat(underTest.asMap(), is(map));
        assertThat(firstMap.get(K_STRING), is(Collections.singletonList("1")));
        assertThat(underTest.get(K_STRING), is(Arrays.asList("1", "1+")));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void asMap_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        // when
        final TypeSafeMultiMap underTest = TypeSafeMultiMap.ofCloned(map);
        final Map<Key<?>, Collection<?>> map1 = underTest.asMap();
        final Map<Key<?>, Collection<?>> map2 = underTest.asMap();
        map1.clear();
        // then
        assertThat(map1, is(not(sameInstance(map2))));
        assertThat(underTest.size(), is(not(0)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void getKeys_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        // when
        final TypeSafeMultiMap underTest = TypeSafeMultiMap.ofCloned(map);
        final Set<Key<?>> set1 = underTest.keySet();
        final Set<Key<?>> set2 = underTest.keySet();
        set1.clear();
        // then
        assertThat(set1, is(not(sameInstance(set2))));
        assertThat(underTest.size(), is(not(0)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_forEach()
      {
        // given
        final Map<Key<?>, Collection<?>> map = createSampleMap();
        // when
        final TypeSafeMultiMap underTest = TypeSafeMultiMap.ofCloned(map);
        final List<Pair<Key<?>, Collection<?>>> pairs = new ArrayList<>();
        underTest.forEach((k, v) -> pairs.add(Pair.of(k, v)));
        // then
        assertThat(pairs, containsInAnyOrder(underTest.entrySet()
                                                      .stream()
                                                      .map(e -> Pair.of(e.getKey(), e.getValue()))
                                                      .toArray()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Map<Key<?>, Collection<?>> createSampleMap()
      {
        final Map<Key<?>, Collection<?>> map = new HashMap<>();
        map.put(K_STRING, Collections.singletonList("1"));
        map.put(K_INTEGER, Collections.singletonList(2));
        map.put(K_DATETIME, Collections.singletonList(LOCAL_DATE));
        return map;
      }
  }
