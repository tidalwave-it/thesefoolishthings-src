/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.io;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;

/***************************************************************************************************************************************************************
 *
 * The role of an object that can be written as a stream of characters.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **************************************************************************************************************************************************************/
@FunctionalInterface
public interface TextWritable
  {
    /** Shortcut for {@link it.tidalwave.util.As}. */
    public static final Class<TextWritable> _TextWritable_ = TextWritable.class;

    /***********************************************************************************************************************************************************
     * A default implementation which throws {@link IOException} when opening the stream.
     **********************************************************************************************************************************************************/
    public static final TextWritable DEFAULT = new TextWritable()
      {
        @Override @Nonnull
        public Writer openWriter()
          throws IOException
          {
            throw new IOException("Operation not supported");
          }
      };

    /***********************************************************************************************************************************************************
     * {@return  a {@link Writer} to write into the object.}
     * @throws  IOException  if the operation can't be performed
     **********************************************************************************************************************************************************/
    @Nonnull
    public Writer openWriter()
      throws IOException;
  }
