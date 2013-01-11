/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.io.PrintWriter;

/***********************************************************************************************************************
 *
 * The role of an object that can be rendered into a {@link String} as HTML markup.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface StringRenderable
  {
    public static final Class<StringRenderable> StringRenderable = StringRenderable.class;

    /*******************************************************************************************************************
     *
     * Renders the attached object into a {@link String}. The method accepts optional parameters that can be used to
     * control the format of the rendering; they are usually specific of the object attached to this role.
     *
     * @param  args  optional rendering parameters
     * @return       the string
     *
     ******************************************************************************************************************/
    @Nonnull
    public String render (@Nonnull Object ... args);

    /*******************************************************************************************************************
     *
     * Renders the attached object appending to a {@link StringBuilder}. The method accepts optional parameters that can
     * be used to control the format of the rendering; they are usually specific of the object attached to this role.
     *
     * @param  args  optional rendering parameters
     * @return       the string
     *
     ******************************************************************************************************************/
    public void renderTo (@Nonnull StringBuilder stringBuilder, @Nonnull Object ... args);

    /*******************************************************************************************************************
     *
     * Renders the attached object printing to a {@link PrintWriter}. The method accepts optional parameters that can
     * be used to control the format of the rendering; they are usually specific of the object attached to this role.
     *
     * @param  args  optional rendering parameters
     * @return       the string
     *
     ******************************************************************************************************************/
    public void renderTo (@Nonnull PrintWriter printWriter, @Nonnull Object ... args);
  }
