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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/***********************************************************************************************************************
 *
 * Notifies that a searched object couldn't be found.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public class NotFoundException extends Exception
  {
    /*******************************************************************************************************************
     *
     * Creates an empty exception.
     *
     ******************************************************************************************************************/
    public NotFoundException()
      {
      }

    /*******************************************************************************************************************
     *
     * Creates an exception with a message.
     *
     * @param  message    the message
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull String message)
      {
        super(message);
      }

    /*******************************************************************************************************************
     *
     * Creates an exception with a cause.
     *
     * @param  cause    the cause
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull Throwable cause)
      {
        super(cause);
      }

    /*******************************************************************************************************************
     *
     * Creates an exception with a message and a cause.
     *
     * @param  message    the message
     * @param  cause    the cause
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull String message, final @Nonnull Throwable cause)
      {
        super(message, cause);
      }

    /*******************************************************************************************************************
     *
     * Throws the {@code NotFoundException} when the passed object is {@code null}. The method returns the object
     * itself and thus it can be used with fluent interfaces.
     *
     * @param  object             the object to be tested
     * @param  message            the error message to be thrown
     * @return                    the object
     * @throws NotFoundException  if the object is null
     *
     ******************************************************************************************************************/
    @Nonnull // needed for binary backward compatibility (this method interprets % in message in verbatim mode)
    public static <T> T throwWhenNull (final @Nullable T object,
                                       final @Nonnull String message)
      throws NotFoundException
      {
        if (object == null)
          {
            throw new NotFoundException(message);
          }

        return object;
      }

    /*******************************************************************************************************************
     *
     * Throws the {@code NotFoundException} when the passed object is {@code null}. The method returns the object
     * itself and thus it can be used with fluent interfaces.
     *
     * @param  object             the object to be tested
     * @param  message            the error message to be thrown (formatted as in {@link String#format}
     * @param  args               the arguments to format the error message
     * @return                    the object
     * @throws NotFoundException  if the object is null
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> T throwWhenNull (final @Nullable T object,
                                       final @Nonnull String message,
                                       final @Nonnull Object... args)
      throws NotFoundException
      {
        if (object == null)
          {
            throw new NotFoundException(String.format(message, args));
          }

        return object;
      }

    /*******************************************************************************************************************
     *
     * Throws the {@code NotFoundException} when the passed collection is {@code null} or empty. The method returns the
     * collection itself and thus it can be used with fluent interfaces.
     *
     * @param  collection         the collection to be tested
     * @param  message            the error message to be thrown
     * @return                    the collection
     * @throws NotFoundException  if the collection is null or empty
     *
     ******************************************************************************************************************/
    @Nonnull // needed for binary backward compatibility (this method interprets % in message in verbatim mode)
    public static <T extends Collection<?>> T throwWhenEmpty (final @Nullable T collection,
                                                              final @Nonnull String message)
      throws NotFoundException
      {
        if ((collection == null) || collection.isEmpty())
          {
            throw new NotFoundException(message);
          }

        return collection;
      }

    /*******************************************************************************************************************
     *
     * Throws the {@code NotFoundException} when the passed collection is {@code null} or empty. The method returns the
     * collection itself and thus it can be used with fluent interfaces.
     *
     * @param  collection         the collection to be tested
     * @param  message            the error message to be thrown (formatted as in {@link String#format}
     * @param  args               the arguments to format the error message
     * @return                    the collection
     * @throws NotFoundException  if the collection is null or empty
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends Collection<?>> T throwWhenEmpty (final @Nullable T collection,
                                                              final @Nonnull String message,
                                                              final @Nonnull Object... args)
      throws NotFoundException
      {
        if ((collection == null) || collection.isEmpty())
          {
            throw new NotFoundException(String.format(message, args));
          }

        return collection;
      }
  }
