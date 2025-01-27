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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import jakarta.annotation.Nonnull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/***************************************************************************************************************************************************************
 *
 * @author Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class ReflectionUtilsTest
  {
    static class Bean1 {}
    static class Bean2 {}
    static class Bean3 {}

    @AllArgsConstructor
    static class WithConstructor
      {
        private final Bean1 bean1;
        private final Bean2 bean2;
        private final Bean3 bean3;
      }

    @Getter
    static class WithAnnotation
      {
        @javax.inject.Inject private Bean1 bean1;
        @javax.inject.Inject private Bean2 bean2;
        @jakarta.inject.Inject private Bean3 bean3;
      }

    /******************************************************************************************************************/
    @Test
    public void test_instantiateWithDependencies()
      {
        // given
        final var beanMap = Map.of(Bean1.class, new Bean1(),
                                   Bean2.class, new Bean2(),
                                   Bean3.class, new Bean3());
        // when
        final var object = ReflectionUtils.instantiateWithDependencies(WithConstructor.class, beanMap);
        // then
        // assertThat(object, isNotNull());
        assertThat(object.bean1).isSameAs(beanMap.get(Bean1.class));
        assertThat(object.bean2).isSameAs(beanMap.get(Bean2.class));
        assertThat(object.bean3).isSameAs(beanMap.get(Bean3.class));
      }

    /******************************************************************************************************************/
    @Test
    public void test_injectDependencies()
      {
        // given
        final var beanMap = Map.of(Bean1.class, new Bean1(),
                                   Bean2.class, new Bean2(),
                                   Bean3.class, new Bean3());
        final var object = new WithAnnotation();
        // when
        ReflectionUtils.injectDependencies(object, beanMap);
        // then
        assertThat(object.bean1).isSameAs(beanMap.get(Bean1.class));
        assertThat(object.bean2).isSameAs(beanMap.get(Bean2.class));
        assertThat(object.bean3).isSameAs(beanMap.get(Bean3.class));
      }

    /******************************************************************************************************************/
    @Test(dataProvider = "dataProvider")
    public void test_getClass (@Nonnull final Type type, @Nonnull final Class<?> expectedResult)
      {
        final var actualResult = ReflectionUtils.getClass(type);
        assertThat(actualResult).isEqualTo(expectedResult);
      }

    /******************************************************************************************************************/
    @DataProvider
    public static Object[][] dataProvider()
      {
        final var genericOfInteger = mock(ParameterizedType.class);
        when(genericOfInteger.getRawType()).thenReturn(Integer.class);

        final var arrayOfInt = mock(GenericArrayType.class);
        when(arrayOfInt.getGenericComponentType()).thenReturn(int.class);

        return new Object[][]
          {
            { String.class, String.class },
            { String[].class, String[].class },
            { genericOfInteger, Integer.class},
            { arrayOfInt, int[].class }
          };
      }
  }
