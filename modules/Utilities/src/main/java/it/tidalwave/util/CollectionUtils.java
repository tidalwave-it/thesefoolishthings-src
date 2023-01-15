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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * This class contains a bunch of utility methods for manipulating lists.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-13
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils
  {
    /*******************************************************************************************************************
     *
     * Appends a list to an object. The resulting list is mutable.
     *
     * @param     <T>       the type of list items
     * @param     list      the list
     * @param     object    the list to append
     * @return              the list with the appended object
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> concat (@Nonnull final List<? extends T> list, @Nonnull final T object)
      {
        final List<T> result = new ArrayList<>(list);
        result.add(object);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Appends a list to another. The resulting list is mutable.
     *
     * @param     <T>       the type of list items
     * @param     list1     the former list
     * @param     list2     the latter list
     * @return              the list with the appended object
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> concat (@Nonnull final List<? extends T> list1, @Nonnull List<? extends T> list2)
      {
        final List<T> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Reverses a list. The resulting list is mutable.
     *
     * @param     <T>       the type of list items
     * @param     list      the list
     * @return              the reversed list
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> reversed (@Nonnull final List<? extends T> list)
      {
        final List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Returns the (optional) first element of a list.
     *
     * @param     <T>       the type of list items
     * @param     list      the list
     * @return              the first element
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Optional<T> optionalHead (@Nonnull final List<? extends T> list)
      {
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
      }

    /*******************************************************************************************************************
     *
     * Returns the first element of a list.
     *
     * @param     <T>       the type of list items
     * @param     list      the list (cannot be empty)
     * @return              the first element
     * @throws    IllegalArgumentException  if the list is empty
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> T head (@Nonnull final List<? extends T> list)
      {
        if (list.isEmpty())
          {
            throw new IllegalArgumentException("List is empty");
          }

        return list.get(0);
      }

    /*******************************************************************************************************************
     *
     * Returns the tail element of a list, that is a list without the first element. The tail of an empty list is an
     * empty list. The resulting list is mutable.
     *
     * @param     <T>       the type of list items
     * @param     list      the list
     * @return              the tail of the list
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> tail (@Nonnull final List<? extends T> list)
      {
        return new ArrayList<>(list.subList(1, list.size()));
      }
  }
