/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
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
package it.tidalwave.actor.spi;

import javax.annotation.Nonnegative;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class ActorActivatorStats implements ActorActivatorStatsMBean
  {
    @Nonnegative @Getter
    private final int poolSize;

    @Getter
    private int pendingMessageCount;

    @Getter
    private int invocationCount;

    @Getter
    private int succesfulInvocationCount;

    @Getter
    private int invocationErrorCount;

    public void changePendingMessageCount (final int delta)
      {
        pendingMessageCount += delta;
      }

    public void incrementInvocationCount()
      {
        invocationCount++;
      }

    public void incrementInvocationErrorCount()
      {
        invocationErrorCount++;
      }

    public void incrementSuccessfulInvocationCount()
      {
        succesfulInvocationCount++;
      }
  }
