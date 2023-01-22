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
package it.tidalwave.role.spi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.role.Identifiable;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class LogUtil
  {
    @Nonnull
    public static String shortName (@Nonnull final Class<?> clazz)
      {
        return shortName(clazz, false);
      }

    @Nonnull
    public static String shortName (@Nonnull final Class<?> clazz, final boolean expandInterfaces)
        {
          var className = clazz.getName();
          var prefix = "";

        if (className.contains("EnhancerByMockito"))
          {
            prefix = "mock-of-";
            className = className.replaceAll("\\$\\$EnhancerByMockito.*", "");
          }

        final var parts = className.split("\\.");
        final var s = new StringBuilder();

        for (var i = 0; i < parts.length; i++)
          {
            s.append((i < parts.length - 1) ? parts[i].charAt(0) + "." : parts[i]);
          }

        if (expandInterfaces)
          {
            final var interfaces = clazz.getInterfaces();

            if (interfaces.length > 0)
              {
                s.append(Arrays.stream(interfaces)
                               .filter(i -> !i.getPackage().getName().startsWith("java"))
                               .map(LogUtil::shortName).collect(joining(", ", "{", "}")));
              }
          }

        return prefix + s;
      }

    @Nonnull
    public static String shortNames (@Nonnull final Iterable<Class<?>> classes)
      {
        final var result = new StringBuilder();
        var separator = "";

        for (final var clazz : classes)
          {
            result.append(separator).append(shortName(clazz));
            separator = ", ";
          }

        return "[" + result + "]";
      }

    @Nonnull
    public static String shortId (@Nullable final Object object)
      {
        if (object == null)
          {
            return "null";
          }

        final var s = new StringBuilder();
        s.append(String.format("%s@%x", shortName(object.getClass()), System.identityHashCode(object)));

        if (object instanceof Identifiable)
          {
            s.append("/").append(((Identifiable)object).getId());
          }

        return s.toString();
      }

    @Nonnull
    public static String shortIds (@Nonnull final Iterable<?> objects)
      {
        final var result = new StringBuilder();
        var separator = "";

        for (final Object object : objects)
          {
            result.append(separator).append(shortId(object));
            separator = ", ";
          }

        return "[" + result + "]";
      }

    @Nonnull
    public static String shortIds (@Nonnull final Object... objects)
      {
        return shortIds(List.of(objects));
      }
  }
