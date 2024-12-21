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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import it.tidalwave.util.Finder;

/***************************************************************************************************************************************************************
 *
 * The role of a composite object, that is an object which contains children. They are exposed by means of a
 * {@link Finder}.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **************************************************************************************************************************************************************/
@FunctionalInterface
public interface Composite<T, F extends Finder<? extends T>>
  {
    public static final Class<Composite> _Composite_ = Composite.class;

    /***********************************************************************************************************************************************************
     * A default <code>Composite</code> with no children.
     **********************************************************************************************************************************************************/
    public static final Composite<Object, Finder<Object>> DEFAULT = new Composite<>()
      {
        @Override @Nonnull
        public Finder<Object> findChildren()
          {
            return Finder.empty();
          }
      };

    /***********************************************************************************************************************************************************
     * Returns the children of this object.
     *
     * @return  the children
     **********************************************************************************************************************************************************/
    @Nonnull
    public F findChildren();

    /***********************************************************************************************************************************************************
     * Returns a stream of children.
     *
     * @return    the stream
     * @since 3.2-ALPHA-23
     **********************************************************************************************************************************************************/
    @Nonnull
    public default Stream<? extends T> stream()
      {
        return findChildren().stream();
      }

    /***********************************************************************************************************************************************************
     * Iterates through children.
     *
     * @param   consumer  the consumer
     * @since 3.2-ALPHA-23
     **********************************************************************************************************************************************************/
    public default void forEach (@Nonnull final Consumer<? super T> consumer)
      {
        stream().forEach(consumer);
      }

    /***********************************************************************************************************************************************************
     **********************************************************************************************************************************************************/
    @SuppressWarnings("EmptyMethod")
    public static interface Visitor<T, R>
      {
        /***************************************************************************************************************
         *
         * Visits an object. This method is called before visiting children (pre-order).
         *
         * @param  object  the visited object
         *
         **************************************************************************************************************/
        public default void preVisit (@Nonnull final T object)
          {
          }

        /***************************************************************************************************************
         *
         * Visits an object. This method is actually called just after {@link #preVisit(Object)}, it makes sense to
         * implement it when you don't need to distinguish between pre-order and post-order traversal.
         *
         * @param  object  the visited object
         *
         **************************************************************************************************************/
        public default void visit (@Nonnull final T object)
          {
          }

        /***************************************************************************************************************
         *
         * Visits an object. This method is called after visiting children (post-order).
         *
         * @param  object  the visited object
         *
         **************************************************************************************************************/
        public default void postVisit (@Nonnull final T object)
          {
          }

        /***************************************************************************************************************
         *
         * Returns the value of this visitor.
         *
         * @return         the value
         *
         **************************************************************************************************************/
        @Nonnull
        public default Optional<R> getValue()
          {
            return Optional.empty();
          }
      }
  }
