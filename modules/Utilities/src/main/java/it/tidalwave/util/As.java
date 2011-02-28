/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @stable
 *
 **********************************************************************************************************************/
public interface As
  {
    /***************************************************************************
     *
     * @stable
     *
     **************************************************************************/
    public static interface NotFoundBehaviour<T>
      {
        @Nonnull
        public T run (@Nullable final Throwable t);
      }

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull Class<T> clazz);

    /***************************************************************************
     *
     *
     **************************************************************************/
    @Nonnull
    public <T> T as (@Nonnull Class<T> clazz, @Nonnull NotFoundBehaviour<T> notFoundBehaviour);

    public final static class Defaults
      {
        private Defaults()
          {
          }

        public static <X> NotFoundBehaviour<X> throwAsException (final @Nonnull Class<X> clazz)
          {
            return new NotFoundBehaviour<X>()
              {
//                @Override
                @Nonnull
                public X run (final @Nonnull Throwable t)
                  {
                    throw new AsException(clazz, t);
                  }
              };
          }
      }
  }
