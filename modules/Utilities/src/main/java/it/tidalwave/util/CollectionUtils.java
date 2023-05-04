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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static java.util.Collections.emptyList;

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
     * Returns a concatenation of the given {@link Collection}s.
     *
     * @param     <T>         the static type
     * @param     collections the input collections
     * @return                the concatenation
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull @SafeVarargs
    public static <T> List<T> concatAll (@Nonnull final Collection<? extends T>... collections)
      {
        final List<T> result = new ArrayList<>();

        for (final var collection : collections)
          {
            result.addAll(collection);
          }

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
     * Sorts a list. The resulting list is mutable.
     *
     * @param     <T>       the type of list items
     * @param     list      the list
     * @return              the sorted list
     * @since     3.2-ALPHA-13
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends Comparable<? super T>> List<T> sorted (@Nonnull final List<? extends T> list)
      {
        final var result = new ArrayList<T>(list);
        Collections.sort(result);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Sorts a list with a given {@link Comparator}. The resulting list is mutable.
     *
     * @param     <T>         the type of list items
     * @param     list        the list
     * @param     comparator  the comparator
     * @return                the sorted list
     * @since     3.2-ALPHA-13
     *
     * @it.tidalwave.javadoc.stable
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> sorted (@Nonnull final List<? extends T> list,
                                      @Nonnull final Comparator<? super T> comparator)
      {
        final var result = new ArrayList<T>(list);
        result.sort(comparator);
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

    /*******************************************************************************************************************
     *
     * Return a sublist of the original {@link List}, from the given {@code from} and {@code to} index (not included).
     * If the {@code from} index is negative and/or the {@code to} index is lower than the {@code from} index or if an
     * attempt is made to read before the start or past the end of the list, truncation silently occurs.
     *
     * @param     <T>             the static type
     * @param     list            the original list
     * @param     from            the first index (included)
     * @param     to              the last index (excluded)
     * @return                    the sublist
     * @since     3.2-ALPHA-17
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<T> safeSubList (@Nonnull final List<? extends T> list, final int from, final int to)
      {
        final var safeFrom = Math.max(from, 0);
        final var safeTo = Math.min(list.size(), to);
        return (safeFrom >= safeTo) ? emptyList() : new ArrayList<>(list.subList(safeFrom, safeTo));
      }

    /*******************************************************************************************************************
     *
     * Splits a given {@link List} at a set of boundaries. Each boundary is the starting point of a sublist to be
     * returned.
     *
     * @param     <T>             the static type
     * @param     list            the original list
     * @param     boundaries      the boundaries
     * @return                    a list of sublists
     * @since     3.2-ALPHA-17
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> List<List<T>> split (@Nonnull final List<? extends T> list, final int ... boundaries)
      {
        final var result = new ArrayList<List<T>>();

        for (var i = 0; i < boundaries.length - 1; i++)
          {
            result.add(safeSubList(list, boundaries[i], boundaries[i + 1]));
          }

        return result;
      }
  }
