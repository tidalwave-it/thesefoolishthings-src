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
package it.tidalwave.role.io;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface TextWritable 
  {
    //@bluebook-begin other
    public final static Class<TextWritable> TextWritable = TextWritable.class;
    
    public final static TextWritable DEFAULT = new TextWritable() 
      {
        @Override @Nonnull
        public Writer openWriter() 
          throws IOException 
          {
            throw new IOException();
          }
      };
    
    //@bluebook-end other
    @Nonnull
    public Writer openWriter()
      throws IOException;
  }
