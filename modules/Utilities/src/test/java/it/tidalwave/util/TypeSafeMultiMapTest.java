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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class TypeSafeMultiMapTest
  {
    private final static Key<String> K_STRING = new Key<String>("string") {};
    private final static Key<String> K_STRING2 = new Key<String>("string2") {};
    private final static Key<Integer> K_INTEGER = new Key<Integer>("integer") {};
    private final static Key<LocalDateTime> DATETIME = new Key<LocalDateTime>("datetime") {};
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
        assertThat(underTest.getSize(), is(0));
        assertThat(underTest.getKeys(), is(Collections.emptySet()));
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
        assertThat(underTest.getSize(), is(3));
        assertThat(underTest.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, DATETIME))));
        assertThat(underTest.asMap(), is(map));
        assertThat(underTest.get(K_STRING), is(Collections.singletonList("1")));
        assertThat(underTest.get(K_INTEGER), is(Collections.singletonList(2)));
        assertThat(underTest.get(DATETIME), is(Collections.singletonList(LOCAL_DATE)));
        assertThat(underTest.get(K_STRING2), is(Collections.emptyList()));
        assertThat(underTest.containsKey(K_STRING), is(true));
        assertThat(underTest.containsKey(K_INTEGER), is(true));
        assertThat(underTest.containsKey(DATETIME), is(true));
        assertThat(underTest.containsKey(K_STRING2), is(false));
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
        assertThat(firstMap.getSize(), is(3));
        assertThat(underTest.getSize(), is(4));
        assertThat(firstMap.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, DATETIME))));
        assertThat(underTest.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, DATETIME, K_STRING2))));
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
        assertThat(firstMap.getSize(), is(3));
        assertThat(underTest.getSize(), is(3));
        assertThat(firstMap.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, DATETIME))));
        assertThat(underTest.getKeys(), is(new HashSet<Key<?>>(Arrays.asList(K_STRING, K_INTEGER, DATETIME))));
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
        assertThat(underTest.getSize(), is(not(0)));
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
    private static Map<Key<?>, Collection<?>> createSampleMap ()
      {
        final Map<Key<?>, Collection<?>> map = new HashMap<>();
        map.put(K_STRING, Collections.singletonList("1"));
        map.put(K_INTEGER, Collections.singletonList(2));
        map.put(DATETIME, Collections.singletonList(LOCAL_DATE));
        return map;
      }
  }
