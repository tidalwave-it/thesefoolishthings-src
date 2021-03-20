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
package it.tidalwave.messagebus.spi;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
     *
     *
     ******************************************************************************************************************/
    public static void forEachMethodInTopDownHierarchy (@Nonnull final Object object,
                                                        @Nonnull final MethodProcessor processor)
      {
        for (final Class<?> clazz : getClassHierarchy(object.getClass()))
          {
            for (final Method method : clazz.getDeclaredMethods())
              {
                processor.process(method);
              }
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static void forEachMethodInBottomUpHierarchy (@Nonnull final Object object,
                                                         @Nonnull final MethodProcessor processor)
      {
        final List<Class<?>> hierarchy = getClassHierarchy(object.getClass());
        Collections.reverse(hierarchy);

        for (final Class<?> clazz : hierarchy)
          {
            if (processor.filter(clazz) == MethodProcessor.FilterResult.ACCEPT)
              {
                for (final Method method : clazz.getDeclaredMethods())
                  {
                    processor.process(method);
                  }
              }
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the hierarchy of the given class, starting from the highest.
     *
     ******************************************************************************************************************/
    @Nonnull
    private static List<Class<?>> getClassHierarchy (@Nonnull final Class<?> clazz)
      {
        final List<Class<?>> hierarchy = new ArrayList<>();

        for (Class<?> ancestor = clazz; ancestor != null; ancestor = ancestor.getSuperclass())
          {
            hierarchy.add(0, ancestor);
          }

        return hierarchy;
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static boolean containsAnnotation (@Nonnull final Annotation[] annotations,
                                              @Nonnull final Class<?> annotationClass)
      {
        for (final Annotation annotation : annotations)
          {
            if (annotationClass.isAssignableFrom(annotation.getClass()))
              {
                return true;
              }
          }

        return false;
      }
  }
