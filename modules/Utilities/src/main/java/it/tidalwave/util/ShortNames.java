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
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * A utility that returns short qualified names for class literals and objects.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-17
 *
 **********************************************************************************************************************/
@Slf4j
public class ShortNames
  {
    /*******************************************************************************************************************
     *
     * Returns the short name for a class literal.
     *
     * @param     clazz     the class
     * @return              the short name
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String shortName (@Nonnull final Class<?> clazz)
      {
        return shortName(clazz, false);
      }

    /*******************************************************************************************************************
     *
     * Returns the short name for a class literal, eventually adding interface names (but not those in the java.*
     * package).
     *
     * @param     clazz               the class
     * @param     withInterfaces      whether the interfaces must be listed
     * @return                        the short name
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String shortName (@Nonnull final Class<?> clazz, final boolean withInterfaces)
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

        if (withInterfaces)
          {
            final var interfaces = clazz.getInterfaces();

            if (interfaces.length > 0)
              {
                s.append(Arrays.stream(interfaces)
                               .filter(i -> !i.getPackage().getName().startsWith("java"))
                               .map(ShortNames::shortName).collect(joining(", ", "{", "}")));
              }
          }

        return prefix + s;
      }

    /*******************************************************************************************************************
     *
     * Returns the short name for class literals, eventually adding interface names (but not those in the java.*
     * package).
     *
     * @param     classes   the classes
     * @return              the short names
     *
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     *
     * Return the short name for an object. If the object contains a method named {@code getId()}, the id is part of
     * the result.
     *
     * @param     object    the object
     * @return              the short name
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String shortId (@Nullable final Object object)
      {
        if (object == null)
          {
            return "null";
          }

        final var s = new StringBuilder();
        s.append(String.format("%s@%x", shortName(object.getClass()), System.identityHashCode(object)));

        try
          {
            final var id = object.getClass().getMethod("getId").invoke(object);
            s.append("/").append(id != null ? id.toString() : "null");
          }
        catch (Exception e)
          {
            // log.trace("While invoking 'getId(): {}", e.toString());
          }

        return s.toString();
      }

    /*******************************************************************************************************************
     *
     * Return the short names for some objects.
     *
     * @param     objects   the objects
     * @return              the short names
     *
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     *
     * Return the short names for some objects.
     *
     * @param     objects   the objects
     * @return              the short names
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String shortIds (@Nonnull final Object... objects)
      {
        return shortIds(List.of(objects));
      }
  }
