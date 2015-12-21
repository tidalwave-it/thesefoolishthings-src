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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import it.tidalwave.role.StringRenderable;

/***********************************************************************************************************************
 *
 * A support class for implementing {@link Renderable}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class StringRenderableSupport implements StringRenderable
  {
    @Override @Nonnull
    public abstract String render (@Nonnull Object ... args);

    @Override
    public void renderTo (final @Nonnull StringBuilder stringBuilder, final @Nonnull Object ... args)
      {
        stringBuilder.append(render(args));
      }

    @Override
    public void renderTo (final @Nonnull PrintWriter printWriter, final @Nonnull Object ... args)
      {
        printWriter.print(render(args));
      }
  }
