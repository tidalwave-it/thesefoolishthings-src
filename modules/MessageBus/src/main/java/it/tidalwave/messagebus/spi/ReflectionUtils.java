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
package it.tidalwave.messagebus.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ReflectionUtils
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static interface MethodProcessor
      {
        enum FilterResult { ACCEPT, IGNORE }

        public FilterResult filter (@Nonnull Class<?> clazz);

        public void process (@Nonnull Method method);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static class MethodProcessorSupport implements MethodProcessor
      {
        @Override @Nonnull
        public FilterResult filter (@Nonnull final Class<?> clazz)
          {
            return FilterResult.ACCEPT;
          }

        @Override
        public void process (@Nonnull final Method method)
          {
          }
      }

    /*******************************************************************************************************************
     *
     * Navigates the hierarchy of the given object, top down, and applies the {@link MethodProcessor} to all the
     * methods of each class, if not filtered out by the processor itself.
     *
     * @param object            the object at the bottom of the hierarchy
     * @param processor         the processor
     *
     ******************************************************************************************************************/
    public static void forEachMethodInTopDownHierarchy (@Nonnull final Object object,
                                                        @Nonnull final MethodProcessor processor)
      {
        for (final var clazz : getClassHierarchy(object.getClass()))
          {
            for (final var method : clazz.getDeclaredMethods())
              {
                processor.process(method);
              }
          }
      }

    /*******************************************************************************************************************
     *
     * Navigates the hierarchy of the given object, bottom up, and applies the {@link MethodProcessor} to all the
     * methods of each class, if not filtered out by the processor itself.
     *
     * @param object            the object at the bottom of the hierarchy
     * @param processor         the processor
     *
     ******************************************************************************************************************/
    public static void forEachMethodInBottomUpHierarchy (@Nonnull final Object object,
                                                         @Nonnull final MethodProcessor processor)
      {
        final var hierarchy = getClassHierarchy(object.getClass());
        Collections.reverse(hierarchy);

        for (final var clazz : hierarchy)
          {
            if (processor.filter(clazz) == MethodProcessor.FilterResult.ACCEPT)
              {
                for (final var method : clazz.getDeclaredMethods())
                  {
                    processor.process(method);
                  }
              }
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the hierarchy of the given class, top down.
     *
     * @param   clazz               the clazz at the bottom of the hierarchy
     * @return                      the hierarchy
     *
     ******************************************************************************************************************/
    @Nonnull
    private static List<Class<?>> getClassHierarchy (@Nonnull final Class<?> clazz)
      {
        final List<Class<?>> hierarchy = new ArrayList<>();

        for (var ancestor = clazz; ancestor != null; ancestor = ancestor.getSuperclass())
          {
            hierarchy.add(0, ancestor);
          }

        return hierarchy;
      }

    /*******************************************************************************************************************
     *
     * Checks whether an array of annotations contains an annotation of the given type.
     *
     * @param   annotations         the annotations to check
     * @param   annotationClass     the type of the required annotation
     * @return                      true if the annotation is found
     *
     ******************************************************************************************************************/
    public static boolean containsAnnotation (@Nonnull final Annotation[] annotations,
                                              @Nonnull final Class<?> annotationClass)
      {
        for (final var annotation : annotations)
          {
            if (annotationClass.isAssignableFrom(annotation.getClass()))
              {
                return true;
              }
          }

        return false;
      }
  }
