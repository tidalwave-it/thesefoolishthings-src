/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.As;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AsSupport implements As
  {
    @Nonnull
    private final AsDelegate delegate;

    protected AsSupport()
      {
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(this);
      }

    protected AsSupport (final @Nonnull Object datum)
      {
        delegate = AsDelegateProvider.Locator.find().createAsDelegate(datum);
      }

    @Nonnull
    public <T> T as (final @Nonnull Class<T> type)
      {
        return as(type, As.Defaults.throwAsException(type));
      }

    @Nonnull
    public <T> T as (final @Nonnull Class<T> type, final @Nonnull As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        return delegate.as(type, notFoundBehaviour);
      }
  }