/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-util
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.util.asexamples;

import javax.annotation.Nonnull;
import java.util.Collection;
import it.tidalwave.util.As;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class Datum1 implements As
  {
    final String status;

    @Nonnull
    public <T> T as (@Nonnull final Class<T> clazz)
      {
        return AsExtensions.as(this, clazz);
      }

    @Nonnull
    public <T> T as (@Nonnull final Class<T> clazz, @Nonnull final As.NotFoundBehaviour<T> notFoundBehaviour)
      {
        return AsExtensions.as(this, clazz, notFoundBehaviour);
      }

    @Nonnull
    public <T> Collection<T> asMany (@Nonnull final Class<T> clazz)
      {
        return AsExtensions.asMany(this, clazz);
      }
  }
