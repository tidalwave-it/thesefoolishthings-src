/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 *
 **********************************************************************************************************************/
public class LogUtil
  {
    @Nonnull
    public static String shortName (final @Nonnull Class<?> clazz)
      {
        String className = clazz.getName();
        String prefix = "";

        if (className.contains("EnhancerByMockito"))
          {
            prefix = "mock-of-";
            className = className.replaceAll("\\$\\$EnhancerByMockito.*", "");
          }

        final String[] parts = className.split("\\.");
        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++)
          {
            result.append((i < parts.length - 1) ? parts[i].substring(0, 1) + "." : parts[i]);
          }

        return prefix + result.toString();
      }

    @Nonnull
    public static String shortNames (final @Nullable Iterable<Class<?>> classes)
      {
        final StringBuilder result = new StringBuilder();
        String separator = "";

        for (final Class<?> clazz : classes)
          {
            result.append(separator).append(shortName(clazz));
            separator = ", ";
          }

        return "[" + result.toString() + "]";
      }

    @Nonnull
    public static String shortId (final @Nullable Object object)
      {
        return (object == null) ? "null" : String.format("%s@%x", shortName(object.getClass()),
                                                                  System.identityHashCode(object));
      }

    @Nonnull
    public static String shortIds (final @Nullable Iterable<Object> objects)
      {
        final StringBuilder result = new StringBuilder();
        String separator = "";

        for (final Object object : objects)
          {
            result.append(separator).append(shortId(object));
            separator = ", ";
          }

        return "[" + result.toString() + "]";
      }
  }
