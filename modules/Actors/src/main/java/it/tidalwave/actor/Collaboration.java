/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.actor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/***************************************************************************************************************************************************************
 *
 * Represents a single task that is possibly decomposed in multiple subtasks and provides support for waiting for its
 * completion.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public interface Collaboration
  {
    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    public static final Collaboration NULL_COLLABORATION = new Collaboration()
      {
        @Override @Nonnull
        public Object getOriginatingMessage()
          {
            return new Object();
          }

        @Override
        public boolean isCompleted()
          {
            return true;
          }

        @Override
        public void waitForCompletion()
          {
          }

        @Override @Nonnull
        public ZonedDateTime getStartTime()
          {
            return ZonedDateTime.of(
                    0, 0, 0, 0, 0, 0, 0, ZoneId.systemDefault());
          }

        @Override @Nonnull
        public Duration getDuration()
          {
            return Duration.of(0, ChronoUnit.SECONDS);
          }

        @Override
        public Object suspend()
          {
            return UUID.randomUUID();
          }

        @Override
        public void resume (@Nonnull final Object suspensionToken, @Nonnull final Runnable runnable)
          {
          }

        @Override
        public void resumeAndDie (@Nonnull final Object suspensionToken)
          {
          }

        @Override
        public boolean isSuspended()
          {
            return false;
          }

        @Override
        public int getDeliveringMessagesCount()
          {
            return 0;
          }

        @Override
        public int getPendingMessagesCount()
          {
            return 0;
          }

        @Override
        public int getRunningThreadsCount()
          {
            return 0;
          }
      };

    /***********************************************************************************************************************************************************
     * A provider of a {@link Collaboration}.
     **********************************************************************************************************************************************************/
    public static interface Provider
      {
        /***************************************************************************************************************
         *
         * Returns the {@link Collaboration}.
         *
         * @return   the {@code Collaboration}
         *
         **************************************************************************************************************/
        @Nonnull
        public Collaboration getCollaboration();
      }

    /***********************************************************************************************************************************************************
     * Returns the message that originated this {@code Collaboration}.
     *
     * @return  the message
     **********************************************************************************************************************************************************/
    @Nonnull
    public Object getOriginatingMessage();

    /***********************************************************************************************************************************************************
     * Returns {@code true} if the {@code Collaboration} has been completed.
     *
     * @return  {@code true} if the {@code Collaboration} has been completed
     **********************************************************************************************************************************************************/
    public boolean isCompleted();

    /***********************************************************************************************************************************************************
     * Waits for the completion of this {@code Collaboration}.
     *
     * @throws  InterruptedException  if the wait is interrupted
     **********************************************************************************************************************************************************/
    public void waitForCompletion()
            throws InterruptedException;

    /***********************************************************************************************************************************************************
     * Return the time when this {@code Collaboration} has been created.
     *
     * @return  the creation time
     **********************************************************************************************************************************************************/
    @Nonnull
    public ZonedDateTime getStartTime();

    /***********************************************************************************************************************************************************
     * Return the duration of this {@code Collaboration}.
     *
     * @return  the duration
     **********************************************************************************************************************************************************/
    @Nonnull
    public Duration getDuration();

    /***********************************************************************************************************************************************************
     * Sometimes a {@code Collaboration} must coordinate with the external world, waiting for an external asynchronous
     * event that cannot be modeled with agents. For instance, a user intervention (e.g. by clicking a button) or an
     * external piece of software that is not part of the {@code Collaboration} model. In this case, it can be marked
     * as 'suspended' and in this case it won't be considered completed, even though there are no related pending
     * messages or working threads. When the external event occurs, call
     * {@link #resume(java.lang.Object, java.lang.Runnable)}.
     *
     * In order to support multiple reasons for suspension, a token is generated and returned. It must be passed to
     * {@code resume()} for resuming.
     *
     * @see #resume(java.lang.Object, java.lang.Runnable)
     * @see #isSuspended()
     *
     * @return   a token representing the reason for the suspension
     **********************************************************************************************************************************************************/
    public Object suspend();

    /***********************************************************************************************************************************************************
     * Resumes a suspended {@code Collaboration}. It executes the given {@link Runnable} which is expected to send new
     * messages.
     *
     * @see #suspend()
     * @see #isSuspended()
     *
     * @param  suspensionToken  the token representing the reason for the suspension
     * @param  resumerTask      the code which resumes the {@code Collaboration}
     **********************************************************************************************************************************************************/
    public void resume (@Nonnull Object suspensionToken, @Nonnull Runnable resumerTask);

    /***********************************************************************************************************************************************************
     * Resumes a suspended {@code Collaboration} and lets it terminate without any further operation.
     *
     * @see #suspend()
     * @see #resume(java.lang.Object, java.lang.Runnable)
     * @see #isSuspended()
     *
     * @param  suspensionToken  the token representing the reason for the suspension
     **********************************************************************************************************************************************************/
    public void resumeAndDie (@Nonnull Object suspensionToken);

    /***********************************************************************************************************************************************************
     * Returns {@code true} when the current {@code Collaboration} is suspended.
     *
     * @see #suspend()
     * @see #resume(java.lang.Object, java.lang.Runnable)
     *
     * @return {@code true} when it's suspended
     **********************************************************************************************************************************************************/
    public boolean isSuspended();

    /***********************************************************************************************************************************************************
     * Returns the number of messages related to this {@code Collaboration} not yet delivered.
     *
     * @return  the number of messages not yet delivered
     **********************************************************************************************************************************************************/
    @Nonnegative
    public int getDeliveringMessagesCount();

    /***********************************************************************************************************************************************************
     * Returns the number of messages related to this {@code Collaboration} not yet consumed.
     *
     * @return  the number of messages not yet consumed
     **********************************************************************************************************************************************************/
    @Nonnegative
    public int getPendingMessagesCount();

    /***********************************************************************************************************************************************************
     * Returns the number of running threads assigned to this {@code Collaboration}.
     *
     * @return  the number of threads
     **********************************************************************************************************************************************************/
    @Nonnegative
    public int getRunningThreadsCount();
  }
