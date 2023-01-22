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

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.*;
import static org.testng.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class KeyTest
  {
    @BeforeMethod
    private void setup()
      {
        Key.INSTANCES.clear();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test()
      {
        // when
        final var key1 = Key.of("key1", String.class);
        final var key2 = Key.of("key2", String.class);
        final var key3 = Key.of("key3", Integer.class);
        final var key1b = Key.of("key1", String.class);
        final var key1c = Key.of("key1", LocalDateTime.class); // existing name, different type
        final var allKeys = Key.allKeys();
        // then
        assertThat(key1.getName(), is("key1"));
        assertEquals(key1.getType(), String.class);
        assertThat(key2.getName(), is("key2"));
        assertEquals(key2.getType(), String.class);
        assertThat(key3.getName(), is("key3"));
        assertEquals(key3.getType(), Integer.class);
        assertThat(key1b.getName(), is("key1"));
        assertEquals(key1b.getType(), String.class);
        assertThat(key1c.getName(), is("key1"));
        assertEquals(key1c.getType(), LocalDateTime.class);
        assertThat(key1b, is(sameInstance(key1)));
        assertThat(key1c, is(not(sameInstance(key1))));
        assertThat(allKeys.stream().map(Key::getName).collect(toList()),
                   is(List.of("key1", "key1", "key2", "key3")));
        assertThat(allKeys.stream().map(Key::getType).collect(toList()),
                   is(List.of(String.class, LocalDateTime.class, String.class, Integer.class)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "keysAndExpectedTypes")
    public <T> void must_return_the_correct_dynamic_type (@Nonnull final Key<T> key, @Nonnull final Class<T> expectedType)
      {
        // when
        final var actualType = key.getType();
        // then
        assertEquals(actualType, expectedType);
      }

    /*******************************************************************************************************************
     *
     * Test for deprecated constructor, don't use Key.of().
     *
     ******************************************************************************************************************/
    @DataProvider
    private static Object[][] keysAndExpectedTypes()
      {
        return new Object[][]
          {
            { new Key<String>("string") {},   String.class  },
            { new Key<Integer>("integer") {}, Integer.class },
            { new Key<Date>("date") {},       Date.class    }
          };
      }
  }
