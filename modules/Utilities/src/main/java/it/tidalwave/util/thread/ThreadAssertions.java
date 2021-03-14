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
package it.tidalwave.util.thread;

import javax.annotation.Nonnull;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.*;

/***********************************************************************************************************************
 *
 * Facility class for asserting that the current thread is of some kind.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = PRIVATE)
public final class ThreadAssertions
  {
    /*******************************************************************************************************************
     *
     * Asserts that the current thread is of the specified type.
     *
     * @param  threadType    the thread type
     *
     ******************************************************************************************************************/
    public static void assertThread (final @Nonnull ThreadType threadType)
      {
        assert doAssertThread(threadType); // trick so we skip everything if assertions are disabled
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private static boolean doAssertThread (final @Nonnull ThreadType threadType)
      {
        switch (threadType)
          {
            case UI:
                // FIXME

            case NOT_UI:
                // FIXME
          }

        return true;
      }
  }
