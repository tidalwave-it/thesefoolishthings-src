/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.*;
import static it.tidalwave.util.ShortNames.*;

/***************************************************************************************************************************************************************
 *
 * Adapted from <a href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860">this article</a>
 *
 * @author Ian Robertson
 * @author Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Slf4j
public class ReflectionUtils
  {
    private static final List<String> INJECT_CLASS_NAMES = List.of("javax.inject.Inject", "jakarta.inject.Inject");

    /***********************************************************************************************************************************************************
     * Get the actual type arguments a subclass has used to extend a generic base class.
     *
     * @param   <T>           the static type of the base class
     * @param   baseClass     the base class
     * @param   childClass    the subclass
     * @return                a list of the raw classes for the actual type arguments.
     **********************************************************************************************************************************************************/
    @Nonnull
    public static <T> List<Class<?>> getTypeArguments (@Nonnull final Class<T> baseClass,
                                                       @Nonnull final Class<? extends T> childClass)
      {
        final Map<Type, Type> resolvedTypes = new HashMap<>();
        Type type = childClass;

        // start walking up the inheritance hierarchy until we hit baseClass
        while (!baseClass.equals(getClass(type)))
          {
            if (type instanceof Class<?>)
              {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class<?>)type).getGenericSuperclass();
              }
            else
              {
                final var parameterizedType = (ParameterizedType) type;
                final var rawType = (Class<?>) parameterizedType.getRawType();
                final var actualTypeArguments = parameterizedType.getActualTypeArguments();
                final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();

                for (var i = 0; i < actualTypeArguments.length; i++)
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

        final var typeArgumentsAsClasses = new ArrayList<Class<?>>();

        // resolve types by chasing down type variables.
        for (var baseType : actualTypeArguments)
          {
            while (resolvedTypes.containsKey(baseType))
              {
                baseType = resolvedTypes.get(baseType);
              }

            typeArgumentsAsClasses.add(getClass(baseType));
          }

        return typeArgumentsAsClasses;
      }

    /***********************************************************************************************************************************************************
     * Instantiates an object of the given class performing dependency injections through the constructor.
     *
     * @param <T>     the generic type of the object to instantiate
     * @param type    the dynamic type of the object to instantiate; it is expected to have a single constructor
     * @param beans   the bag of objects to instantiate
     * @return        the new instance
     * @throws        RuntimeException if something fails
     * @since         3.2-ALPHA-17
     **********************************************************************************************************************************************************/
    public static <T> T instantiateWithDependencies (@Nonnull final Class<? extends T> type,
                                                     @Nonnull final Map<Class<?>, Object> beans)
      {
        try
          {
            log.debug("instantiateWithDependencies({}, {})", shortName(type), shortIds(beans.values()));
            final var constructors = type.getConstructors();

            if (constructors.length > 1)
              {
                throw new RuntimeException("Multiple constructors in " + type);
              }

            final var parameters = Arrays.stream(constructors[0].getParameterTypes()).map(beans::get).collect(toList());

            log.trace(">>>> ctor arguments: {}", shortIds(parameters));
            return type.cast(constructors[0].newInstance(parameters.toArray()));
          }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
          {
            throw new RuntimeException(e);
          }
      }

    /***********************************************************************************************************************************************************
     * Performs dependency injection to an object by means of field introspection.
     *
     * @param object  the object
     * @param beans   the bag of objects to instantiate
     * @since         3.2-ALPHA-17
     **********************************************************************************************************************************************************/
    public static void injectDependencies (@Nonnull final Object object, @Nonnull final Map<Class<?>, Object> beans)
      {
        for (final var field : object.getClass().getDeclaredFields())
          {
            if (hasInjectAnnotation(field))
              {
                field.setAccessible(true);
                final var type = field.getType();
                final var dependency = beans.get(type);

                if (dependency == null)
                  {
                    throw new RuntimeException("Can't inject " + object + "." + field.getName());
                  }

                try
                  {
                    field.set(object, dependency);
                  }
                catch (IllegalArgumentException | IllegalAccessException e)
                  {
                    throw new RuntimeException(e);
                  }
              }
          }
      }

    /***********************************************************************************************************************************************************
     * Returns the class literal associated to the given type.
     *
     * @param   type    the type to inspect
     * @return          the class literal; it might be {@code null} if fails
     **********************************************************************************************************************************************************/
    @CheckForNull
    public static Class<?> getClass (@Nonnull final Type type)
      {
        requireNonNull(type, "type");

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
            final var componentType = ((GenericArrayType)type).getGenericComponentType();
            final var componentClass = getClass(componentType);

            if (componentClass == null)
              {
                return null;
              }

            return Array.newInstance(componentClass, 0).getClass();
          }
        else
          {
//            throw new IllegalArgumentException(type.toString());
            return null;
          }
      }

    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    private static boolean hasInjectAnnotation (@Nonnull final Field field)
      {
        final var classLoader = Thread.currentThread().getContextClassLoader();

        for (final var className : INJECT_CLASS_NAMES)
          {
            try
              {
                @SuppressWarnings("unchecked")
                final var clazz = (Class<? extends Annotation>)classLoader.loadClass(className);

                if (field.getAnnotation(clazz) != null)
                  {
                    return true;
                  }
              }
            catch (ClassNotFoundException ignored)
              {
                // try next
              }
          }

        return false;
      }
  }
