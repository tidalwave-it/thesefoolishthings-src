/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

/***********************************************************************************************************************
 *
 * The role of an object that can be read as a stream of characters.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
public interface TextReadable
  {
    //@bluebook-begin other
    public final static Class<TextReadable> TextReadable = TextReadable.class;

    /*******************************************************************************************************************
     *
     * A default implementation which throws {@link IOException} when opening the stream.
     *
     ******************************************************************************************************************/
    public final static TextReadable DEFAULT = new TextReadable()
      {
        @Override @Nonnull
        public Reader openReader()
          throws IOException
          {
            throw new IOException("Operation not supported");
          }
      };

    /*******************************************************************************************************************
     *
     * Returns a {@link Reader} to read from the object.
     *
     * @return               the {@code Reader}
     * @throws  IOException  if the operation can't be performed
     *
     ******************************************************************************************************************/
    //@bluebook-end other
    @Nonnull
    public Reader openReader()
      throws IOException;
  }
