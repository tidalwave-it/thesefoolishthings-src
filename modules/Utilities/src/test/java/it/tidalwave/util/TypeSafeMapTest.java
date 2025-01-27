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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class TypeSafeMapTest
  {
    private static final Key<String> K_STRING = Key.of("string", String.class);
    private static final Key<String> K_STRING2 = Key.of("string2", String.class);
    private static final Key<Integer> K_INTEGER = Key.of("integer", Integer.class);
    private static final Key<LocalDateTime> K_DATETIME = Key.of("datetime", LocalDateTime.class);
    public static final LocalDateTime LOCAL_DATE = LocalDateTime.now();

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void test_newInstance()
      {
        // when
        final var underTest = TypeSafeMap.newInstance();
        // then
        assertThat(underTest).isEmpty();
        assertThat(underTest.keySet()).isEmpty();
        assertThat(underTest.asMap()).isEmpty();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void test_ofCloned()
            throws NotFoundException
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMap.ofCloned(map);
        // then
        assertThat(underTest).hasSize(3);
        assertThat(underTest.keySet()).containsExactlyInAnyOrder(K_STRING, K_INTEGER, K_DATETIME);
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(underTest.get(K_STRING)).isEqualTo("1");
        assertThat(underTest.get(K_INTEGER)).isEqualTo(2);
        assertThat(underTest.get(K_DATETIME)).isEqualTo(LOCAL_DATE);

        try
          {
            underTest.get(K_STRING2);
            fail("Must throw NotFoundException");
          }
        catch (NotFoundException e)
          {
            // ok
          }

        assertThat(underTest.getOptional(K_STRING)).contains("1");
        assertThat(underTest.getOptional(K_INTEGER)).contains(2);
        assertThat(underTest.getOptional(K_DATETIME)).contains(LOCAL_DATE);
        assertThat(underTest.getOptional(K_STRING2)).isEmpty();
        assertThat(underTest.containsKey(K_STRING)).isTrue();
        assertThat(underTest.containsKey(K_INTEGER)).isTrue();
        assertThat(underTest.containsKey(K_DATETIME)).isTrue();
        assertThat(underTest.containsKey(K_STRING2)).isFalse();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void test_with()
            throws NotFoundException
      {
        // given
        final var map = createSampleMap();
        final var firstMap = TypeSafeMap.ofCloned(map);
        // when
        final var underTest = firstMap.with(K_STRING2, "2");
        // then
        assertThat(underTest).isNotSameAs(firstMap);
        assertThat(firstMap).hasSize(3);
        assertThat(underTest).hasSize(4);
        assertThat(firstMap.keySet()).containsExactlyInAnyOrder(K_STRING, K_INTEGER, K_DATETIME);
        assertThat(underTest.keySet()).containsExactlyInAnyOrder(K_STRING, K_INTEGER, K_DATETIME, K_STRING2);
        map.put(K_STRING2, "2");
        assertThat(underTest.asMap()).isEqualTo(map);
        assertThat(underTest.get(K_STRING2)).isEqualTo("2");
        assertThat(underTest.getOptional(K_STRING2)).contains("2");
        assertThat(underTest.containsKey(K_STRING2)).isTrue();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void asMap_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final var underTest = TypeSafeMap.ofCloned(createSampleMap());
        // when
        final var map1 = underTest.asMap();
        final var map2 = underTest.asMap();
        map1.clear();
        // then
        assertThat(map1).isNotSameAs(map2);
        assertThat(underTest).isNotEmpty();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void getKeys_must_return_different_mutable_instances_detached_from_internal_state()
      {
        // given
        final var underTest = TypeSafeMap.ofCloned(createSampleMap());
        // when
        final var set1 = underTest.keySet();
        final var set2 = underTest.keySet();
        set1.clear();
        // then
        assertThat(set1).isNotSameAs(set2);
        assertThat(underTest).isNotEmpty();
      }


    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test @SuppressWarnings("RedundantExplicitVariableType") // keep explicit type for the code sample
    public void codeSamples()
      {
        // START SNIPPET TypeSafeMap
        final Key<String> k1 = Key.of("Key 1", String.class);
        final Key<Integer> k2 = Key.of("Key 2", Integer.class);
        final var m = TypeSafeMap.newInstance()
                                 .with(k1, "Value 1")
                                 .with(k2, 1);
        final Optional<String> v1 = m.getOptional(k1);
        final Optional<Integer> v2 = m.getOptional(k2);
        assertThat(v1).contains("Value 1");
        assertThat(v2).contains(1);
        // END SNIPPET TypeSafeMap
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test @SuppressWarnings("unchecked")
    public void test_forEach()
      {
        // given
        final var map = createSampleMap();
        // when
        final var underTest = TypeSafeMap.ofCloned(map);
        final var pairs = new ArrayList<Pair<Key<?>, Object>>();
        underTest.forEach((k, v) -> pairs.add(Pair.of(k, v)));
        // then
        assertThat(pairs).contains(underTest.entrySet()
                                                      .stream()
                                                      .map(e -> Pair.of(e.getKey(), e.getValue()))
                                                      .toArray(Pair[]::new));
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Nonnull
    private static Map<Key<?>, Object> createSampleMap()
      {
        final Map<Key<?>, Object> map = new HashMap<>();
        map.put(K_STRING, "1");
        map.put(K_INTEGER, 2);
        map.put(K_DATETIME, LOCAL_DATE);
        return map;
      }
  }
