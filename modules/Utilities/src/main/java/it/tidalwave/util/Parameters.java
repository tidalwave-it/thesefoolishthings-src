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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * This class provides a few static utility methods to manipulate arguments to methods.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Parameters
  {
    /*******************************************************************************************************************
     *
     * A convenience method for transforming a varargs of objects to a {@link Collection}. It supports concatenating
     * collections: that is, each varargs item that is a {@link Collection} is flattened.
     *
     * @param   objects                   the objects as varargs
     * @return                            the objects as collection
     * @since                             3.2-ALPHA-3
     * @it.tidalwave.javadoc.experimental
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Collection<Object> r (@Nonnull final Object... objects)
      {
        // Don't use streams() for performance reasons.
        final List<Object> result = new ArrayList<>();

        for (final Object object : objects)
          {
            if (!(object instanceof Collection))
              {
                result.add(object);
              }
            else
              {
                result.addAll((Collection<?>)object);
              }
          }

        return result;
      }

    /*******************************************************************************************************************
     *
     * Extracts a singled-value parameter of the given type from an array. If the parameter is not found, the default
     * value is returned. If more than a single parameter is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param   <T>                       the static type of the parameter
     * @param   parameterClass            the dynamic type of the parameter
     * @param   defaultOption             the default value of the parameter
     * @param   parameters                the array of parameters
     * @return                            the value of the parameter
     * @throws IllegalArgumentException   if more than a single value is found
     *
     ******************************************************************************************************************/
    @CheckForNull
    public static <T> T find (@Nonnull final Class<T> parameterClass,
                              @CheckForNull final T defaultOption,
                              @Nonnull final Object... parameters)
            throws IllegalArgumentException
      {
        // Don't use streams() for performance reasons.
        final Collection<T> c = find(parameterClass, parameters);

        if (c.size() > 1)
          {
            throw new IllegalArgumentException(String.format("Multiple values for %s have been specified",
                                                             parameterClass.getSimpleName()));
          }

        return c.isEmpty() ? defaultOption : c.iterator().next();
      }

    /*******************************************************************************************************************
     *
     * Extracts multiple-value parameters of the given type from an array. If the parameter is not found, an empty
     * collection is returned.
     *
     * @param   <T>                       the static type of the parameter
     * @param   parameterClass            the class of the parameter to retrieve
     * @param   parameters                the array of parameters
     * @return                            the value of the parameter
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Collection<T> find (@Nonnull final Class<T> parameterClass, @Nonnull final Object... parameters)
      {
        // Don't use streams() for performance reasons.
        final Collection<T> result = new ArrayList<>();

        for (final Object parameter : parameters)
          {
            if (parameterClass.isAssignableFrom(parameter.getClass()))
              {
                result.add(parameterClass.cast(parameter));
              }
          }

        return result;
      }

    /*******************************************************************************************************************
     *
     * <b>This method is for internal implementation only.</b> Ensures that a given object is neither an array nor a
     * collection.
     *
     * @param   object    the object to check
     * @param   message   the message to put in the exception
     * @return            the object itself for calling this method as a function
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Object mustNotBeArrayOrCollection (@Nonnull final Object object, @Nonnull final String message)
      {
        if (object instanceof Collection)
          {
            throw new IllegalArgumentException(message + " can't be a Collection");
          }

        if (object.getClass().isArray())
          {
            throw new IllegalArgumentException(message + " can't be an array");
          }

        return object;
      }
  }
