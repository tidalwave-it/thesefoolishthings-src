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
package it.tidalwave.util.impl;

import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class EmptyAsDelegateProvider implements AsDelegateProvider
  {
    @Override @Nonnull
    public AsDelegate createAsDelegate (final @Nonnull Object owner)
      {
        return new AsDelegate()
          {
            @Override @Nonnull
            public <T> Collection<T> as (Class<T> type)
              {
                return new ArrayList<T>(); // must be mutable
              }
          };
      }
  }