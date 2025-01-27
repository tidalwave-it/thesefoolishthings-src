/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util;

import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Collections.singletonList;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class TypeSafeMultiMapTest
  {
    private static final Key<String> K_STRING = Key.of("string", String.class);
    private static final Key<String> K_STRING2 = Key.of("string2", String.class);
    private static final Key<Integer> K_INTEGER = Key.of("integer", Integer.class);
    private static final Key<LocalDateTime> K_DATETIME = Key.of("datetime", LocalDateTime.class);
    public static final LocalDateTime LOCAL_DATE = LocalDateTime.now();

    /**********************************************************************************************************************************************************/
    @Test
    public void test_newInstance()
      {
        // when
        final var underTest = TypeSafeMultiMap.newInstance();
        // then
        assertThat(underTest).isEmpty();
        assertThat(underTest.keySet()).isEmpty();
        assertThat(underTest.asMap()).isEmpty();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_ofCloned()
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMultiMap.ofCloned(map);
        // then
        assertThat(underTest).hasSize(3);
        assertThat(underTest.keySet()).containsOnly(K_STRING, K_INTEGER, K_DATETIME);
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(underTest.get(K_STRING)).containsOnly("1");
        assertThat(underTest.get(K_INTEGER)).containsOnly(2);
        assertThat(underTest.get(K_DATETIME)).containsOnly(LOCAL_DATE);
        assertThat(underTest.get(K_STRING2)).isEmpty();
        assertThat(underTest.containsKey(K_STRING)).isTrue();
        assertThat(underTest.containsKey(K_INTEGER)).isTrue();
        assertThat(underTest.containsKey(K_DATETIME)).isTrue();
        assertThat(underTest.containsKey(K_STRING2)).isFalse();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_with_from_empty_map()
      {
        // given
        final var firstMap = TypeSafeMultiMap.newInstance();
        // when
        final var underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest).isNotSameAs(firstMap);
        assertThat(firstMap).isEmpty();
        assertThat(underTest).hasSize(1);
        assertThat(firstMap.keySet()).isEmpty();
        assertThat(underTest.keySet()).containsOnly(K_STRING2);
        final Map<Key<?>, Object> map = new HashMap<>();
        map.put(K_STRING2, singletonList("2"));
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(underTest.get(K_STRING2)).containsOnly("2");
        assertThat(underTest.containsKey(K_STRING2)).isTrue();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_with()
      {
        // given
        final var map = createSampleMap();
        final var firstMap = TypeSafeMultiMap.ofCloned(map);
        // when
        final var underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest).isNotSameAs(firstMap);
        assertThat(firstMap).hasSize(3);
        assertThat(underTest).hasSize(4);
        assertThat(firstMap.keySet()).containsOnly(K_STRING, K_INTEGER, K_DATETIME);
        assertThat(underTest.keySet()).containsOnly(K_STRING, K_INTEGER, K_DATETIME, K_STRING2);
        map.put(K_STRING2, singletonList("2"));
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(underTest.get(K_STRING2)).containsOnly("2");
        assertThat(underTest.containsKey(K_STRING2)).isTrue();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void test_with_and_existing_key()
      {
        // given
        final var map = createSampleMap();
        final var firstMap = TypeSafeMultiMap.ofCloned(map);
        // when
        final var underTest = firstMap.with(K_STRING, "1+"); // STRING key is already present
        // then
        assertThat(underTest).isNotSameAs(firstMap);
        assertThat(firstMap).hasSize(3);
        assertThat(underTest).hasSize(3);
        assertThat(firstMap.keySet()).containsOnly(K_STRING, K_INTEGER, K_DATETIME);
        assertThat(underTest.keySet()).containsOnly(K_STRING, K_INTEGER, K_DATETIME);
        map.put(K_STRING, List.of("1", "1+"));
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(firstMap.get(K_STRING)).containsExactly("1");
        assertThat(underTest.get(K_STRING)).containsExactly("1", "1+");
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void asMap_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMultiMap.ofCloned(map);
        final var map1 = underTest.asMap();
        final var map2 = underTest.asMap();
        map1.clear();
        // then
        assertThat(map1).isNotSameAs(map2);
        assertThat(underTest).isNotEmpty();
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void getKeys_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMultiMap.ofCloned(map);
        final var set1 = underTest.keySet();
        final var set2 = underTest.keySet();
        set1.clear();
        // then
        assertThat(set1).isNotSameAs(set2);
        assertThat(underTest).isNotEmpty();
      }

    /**********************************************************************************************************************************************************/
    @Test @SuppressWarnings("unchecked")
    public void test_forEach()
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMultiMap.ofCloned(map);
        final List<Pair<Key<?>, Collection<?>>> pairs = new ArrayList<>();
        underTest.forEach((k, v) -> pairs.add(Pair.of(k, v)));
        // then
        assertThat(pairs).contains(underTest.entrySet()
                                                      .stream()
                                                      .map(e -> Pair.of(e.getKey(), e.getValue()))
                                                      .toArray(Pair[]::new));
      }

    /**********************************************************************************************************************************************************/
    @Test @SuppressWarnings("RedundantExplicitVariableType") // keep explicit type for the code sample
    public void codeSamples()
      {
          // START SNIPPET TypeSafeMultiMap
          final Key<String> k1 = Key.of("Key 1", String.class);
          final Key<Integer> k2 = Key.of("Key 2", Integer.class);
          final var m = TypeSafeMultiMap.newInstance()
                                        .with(k1, "Value 1")
                                        .with(k1, "Value 2")
                                        .with(k2, 1)
                                        .with(k2, 2);
          final Collection<String> v1 = m.get(k1);
          final Collection<Integer> v2 = m.get(k2);
          assertThat(v1).containsExactly("Value 1", "Value 2");
          assertThat(v2).containsExactly(1, 2);
          // END SNIPPET TypeSafeMultiMap
      }

    /**********************************************************************************************************************************************************/
    @Nonnull
    private static Map<Key<?>, Collection<?>> createSampleMap()
      {
        return new HashMap<>(Map.of(K_STRING, List.of("1"),
                                    K_INTEGER, List.of(2),
                                    K_DATETIME, List.of(LOCAL_DATE)));
      }
  }
