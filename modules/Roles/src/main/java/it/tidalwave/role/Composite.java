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
package it.tidalwave.role;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import it.tidalwave.util.NotFoundException;

/***********************************************************************************************************************
 *
 * The role of a composite object, that is an object which contains children. They are exposed by means of a
 * {@link Finder}.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Composite<TYPE, SPECIALIZED_FINDER extends Finder<? extends TYPE>>
  {
    //@bluebook-begin other
    public static final Class<Composite> _Composite_ = Composite.class;

    /*******************************************************************************************************************
     *
     * A default <code>Composite</code> with no children.
     *
     ******************************************************************************************************************/
    public static final Composite<Object, Finder<Object>> DEFAULT = new Composite<Object, Finder<Object>>()
      {
        @Override @Nonnull
        public Finder<Object> findChildren()
          {
            return Finder.empty();
          }
      };

    //@bluebook-end other
    /*******************************************************************************************************************
     *
     * Returns the children of this object.
     *
     * @return  the children
     *
     ******************************************************************************************************************/
    @Nonnull
    public SPECIALIZED_FINDER findChildren();
    //@bluebook-begin other
    //@bluebook-begin visitor

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
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
        public void preVisit (@Nonnull T object);

        /***************************************************************************************************************
         *
         * Visits an object. This method is actually called just after {@link #preVisit(Object)}, it makes sense to
         * implement it when you don't need to distinguish between pre-order and post-order traversal.
         *
         * @param  object  the visited object
         *
         **************************************************************************************************************/
        public void visit (@Nonnull T object);

        /***************************************************************************************************************
         *
         * Visits an object. This method is called after visiting children (post-order).
         *
         * @param  object  the visited object
         *
         **************************************************************************************************************/
        public void postVisit (@Nonnull T object);

        /***************************************************************************************************************
         *
         * Returns the value of this visitor.
         *
         * @return                     the value
         * @throws  NotFoundException  when no value has been found
         *
         **************************************************************************************************************/
        @Nonnull
        public R getValue()
          throws NotFoundException;
      }
    //@bluebook-end visitor

    /*******************************************************************************************************************
     *
     * A support class for {@link Visitor} which provides default empty methods.
     *
     ******************************************************************************************************************/
    public static class VisitorSupport<T, R> implements Visitor<T, R>
      {
        /** {@inheritDoc} */
        @Override
        public void preVisit (@Nonnull final T object)
          {
          }

        /** {@inheritDoc} */
        @Override
        public void visit (@Nonnull final T object)
          {
          }

        /** {@inheritDoc} */
        @Override
        public void postVisit (@Nonnull final T object)
          {
          }

        /** {@inheritDoc} */
        @Override @Nonnull
        public R getValue()
          throws NotFoundException
          {
            throw new NotFoundException("Must be implemented by subclasses");
          }
      }
    //@bluebook-end other
  }
