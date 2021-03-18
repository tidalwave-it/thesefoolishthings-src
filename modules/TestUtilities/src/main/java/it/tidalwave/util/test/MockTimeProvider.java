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
package it.tidalwave.util.test;

import javax.annotation.Nonnull;
import java.time.Instant;
import lombok.Setter;
import it.tidalwave.util.TimeProvider;

/***********************************************************************************************************************
 *
 * A mock implementation of {@link TimeProvider} which returns a fixed value that can be set (the zero epoch instant
 * by default).
 * 
 * @see     TimeProvider
 * @author  Fabrizio Giudici
 * @since   1.39
 *
 **********************************************************************************************************************/
public class MockTimeProvider implements TimeProvider
  {
    @Setter
    private Instant instant = Instant.ofEpochMilli(0);

    @Override @Nonnull
    public Instant currentInstant()
      {
        return instant;
      }
  }