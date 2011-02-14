/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @stable
 *
 **********************************************************************************************************************/
public class NotFoundException extends Exception
  {
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public NotFoundException()
      {
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull String message)
      {
        super(message);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull Throwable throwable)
      {
        super(throwable);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public NotFoundException (final @Nonnull String message, final @Nonnull Throwable throwable)
      {
        super(message, throwable);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull // needed for binary backward compatibility
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
     *
     ******************************************************************************************************************/
    @Nonnull // needed for binary backward compatibility
    public static <T extends Collection> T throwWhenEmpty (final @Nullable T collection,
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
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends Collection> T throwWhenEmpty (final @Nullable T collection,
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
