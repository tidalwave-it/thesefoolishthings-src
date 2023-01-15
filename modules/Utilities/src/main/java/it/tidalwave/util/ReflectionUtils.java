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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***********************************************************************************************************************
 *
 * Just slightly adapted from <a href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860">this article</a>.
 *
 * @author Ian Robertson
 * @author Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ReflectionUtils
  {
    /*******************************************************************************************************************
     *
     * Get the actual type arguments a subclass has used to extend a generic base class.
     *
     * @param   <T>           the static type of the base class
     * @param   baseClass     the base class
     * @param   childClass    the subclass
     * @return                a list of the raw classes for the actual type arguments.
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<Class<?>> getTypeArguments (@Nonnull final Class<T> baseClass,
                                                       @Nonnull final Class<? extends T> childClass)
      {
        final Map<Type, Type> resolvedTypes = new HashMap<>();
        Type type = childClass;

        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass))
          {
            if (type instanceof Class<?>)
              {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class<?>)type).getGenericSuperclass();
              }
            else
              {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();

                for (int i = 0; i < actualTypeArguments.length; i++)
                  {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                  }

                if (!rawType.equals(baseClass))
                  {
                    type = rawType.getGenericSuperclass();
                  }
              }
          }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        final Type[] actualTypeArguments;

        if (type instanceof Class)
          {
            actualTypeArguments = ((Class<?>)type).getTypeParameters();
          }
        else
          {
            actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
          }

        final List<Class<?>> typeArgumentsAsClasses = new ArrayList<>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments)
          {
            while (resolvedTypes.containsKey(baseType))
              {
                baseType = resolvedTypes.get(baseType);
              }

            typeArgumentsAsClasses.add(getClass(baseType));
          }

        return typeArgumentsAsClasses;
      }

    @Nonnull
    public static Class<?> getClass (@Nonnull final Type type)
      {
        if (type == null)
          {
            throw new IllegalArgumentException("null Type");
          }

        if (type instanceof Class<?>)
          {
            return (Class<?>)type;
          }
        else if (type instanceof ParameterizedType)
          {
            return getClass(((ParameterizedType)type).getRawType());
          }
        else if (type instanceof GenericArrayType)
          {
            final Type componentType = ((GenericArrayType)type).getGenericComponentType();
            final Class<?> componentClass = getClass(componentType);
            return Array.newInstance(componentClass, 0).getClass();
          }

        throw new IllegalArgumentException(type.toString());
      }
  }
