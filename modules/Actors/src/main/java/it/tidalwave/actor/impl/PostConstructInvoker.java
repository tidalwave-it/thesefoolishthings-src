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
package it.tidalwave.actor.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import java.util.List;
import it.tidalwave.messagebus.spi.ReflectionUtils;
import lombok.RequiredArgsConstructor;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@RequiredArgsConstructor
public class PostConstructInvoker extends ReflectionUtils.MethodProcessorSupport
  {
    private static final List<String> POSTCONSTRUCT_CLASS_NAMES = List.of("javax.annotation.PostConstruct", "jakarta.annotation.PostConstruct");

    @Nonnull
    private final Object object;

    @Override
    public void process (@Nonnull final Method method)
      {
        final var classLoader = Thread.currentThread().getContextClassLoader();

        for (final var className : POSTCONSTRUCT_CLASS_NAMES)
          {
            try
              {
                @SuppressWarnings("unchecked")
                final var clazz = (Class<? extends Annotation>)classLoader.loadClass(className);

                if (method.getAnnotation(clazz) != null)
                  {
                    try
                      {
                        method.invoke(object);
                      }
                    catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e)
                      {
                        throw new RuntimeException(e);
                      }
                  }
              }
            catch (ClassNotFoundException ignored)
              {
                // try next
              }
          }
      }
  }
