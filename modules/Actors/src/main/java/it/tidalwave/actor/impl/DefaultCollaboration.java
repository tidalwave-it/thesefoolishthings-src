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
package it.tidalwave.actor.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.eaio.uuid.UUID;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.CollaborationCompletedMessage;
import it.tidalwave.actor.CollaborationStartedMessage;
import it.tidalwave.actor.annotation.Message;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access=PRIVATE) @EqualsAndHashCode(of="id") @Slf4j @ToString(of={"id"})
public class DefaultCollaboration implements Serializable, Collaboration
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private final static DefaultCollaboration NULL_DEFAULT_COLLABORATION = new DefaultCollaboration(new Object())
      {
        @Override
        public void bindToCurrentThread()
          {
          }

        @Override
        public void unbindFromCurrentThread()
          {
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor @ToString
    private static class IdentityWrapper
      {
        @Nonnull
        private final Object object;

        @Override
        public boolean equals (final Object object)
          {
            if ((object == null) || (getClass() != object.getClass()))
              {
                return false;
              }

            final IdentityWrapper other = (IdentityWrapper)object;
            return this.object == other.object;
          }

        @Override
        public int hashCode()
          {
            return object.hashCode();
          }
      }

    private final static ThreadLocal<DefaultCollaboration> THREAD_LOCAL = new ThreadLocal<DefaultCollaboration>();

    private final UUID id = new UUID();

    @Nonnull @Getter
    private final Object originatingMessage;

    private final long startTime = System.currentTimeMillis();

    @Getter
    private boolean completed;

    private final List<Object> suspensionTokens = new ArrayList<Object>();

    /** List of threads currently working for this Collaboration. */
    // No need for being weak, since objects are explicitly removed
    private final List<Thread> runningThreads = new ArrayList<Thread>();

    /** List of messages currently being delivered as part of this Collaboration. */
    // No need for being weak, since objects are explicitly removed
    private final List<Object> deliveringMessages = new ArrayList<Object>();

    /** List of messages pending to be consumed as part of this Collaboration. */
    // No need for being weak, since objects are explicitly removed
    private final List<IdentityWrapper> pendingMessages = new ArrayList<IdentityWrapper>();

    private boolean collaborationStartedMessageSent = false;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static DefaultCollaboration getCollaboration (final @Nonnull Object object)
      {
        return (object instanceof Provider) ? (DefaultCollaboration)((Provider)object).getCollaboration()
                                            : NULL_DEFAULT_COLLABORATION;
      }

    /*******************************************************************************************************************
     *
     * Gets the {@link Collaboration} bound to the current thread or creates a new one.
     *
     * @return  the {@code Collaboration}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static DefaultCollaboration getOrCreateCollaboration (final @Nonnull Object source)
      {
        DefaultCollaboration collaboration = THREAD_LOCAL.get();

        if (collaboration == null)
          {
            collaboration = new DefaultCollaboration(source);
          }

        return collaboration;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DateTime getStartTime()
      {
        return new DateTime(startTime);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Duration getDuration()
      {
        return new Duration(startTime, System.currentTimeMillis());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public synchronized void waitForCompletion()
      throws InterruptedException
      {
        while (!isCompleted())
          {
            wait();
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public synchronized int getDeliveringMessagesCount()
      {
        return deliveringMessages.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public synchronized int getPendingMessagesCount()
      {
        return pendingMessages.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnegative
    public synchronized int getRunningThreadsCount()
      {
        return runningThreads.size();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void resume (final @Nonnull Object suspensionToken, final @Nonnull Runnable runnable)
      {
        try
          {
            bindToCurrentThread();
            runnable.run();
            suspensionTokens.remove(suspensionToken);
          }
        finally
          {
            unbindFromCurrentThread();
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public synchronized void resumeAndDie (final @Nonnull Object suspensionToken)
      {
        resume(suspensionToken, new Runnable()
          {
            @Override
            public void run()
              {
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public synchronized Object suspend()
      {
        final Object suspensionToken = new UUID();
        suspensionTokens.add(suspensionToken);
        return suspensionToken;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public synchronized boolean isSuspended()
      {
        return !suspensionTokens.isEmpty();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void bindToCurrentThread()
      {
        log.trace("bindToCurrentThread()");
        THREAD_LOCAL.set(this);
        runningThreads.add(Thread.currentThread());
        notifyAll();
        log();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void unbindFromCurrentThread()
      {
        log.trace("unbindFromCurrentThread()");
        runningThreads.remove(Thread.currentThread());
        THREAD_LOCAL.remove();
        notifyAll();
        log();
        eventuallySendCompletionMessage(null);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void registerDeliveringMessage (final @Nonnull Object message)
      {
        log.trace("registerDeliveringMessage({})", message);

        final Message annotation = message.getClass().getAnnotation(Message.class);

        if (annotation == null)
          {
            throw new IllegalArgumentException("Message must be annotated with @Message: " + message.getClass());
          }

        if (annotation.daemon())
          {
            deliveringMessages.add(message);

            // Do this *after* enlisting message in deliveringMessages
            if (!collaborationStartedMessageSent && !(message instanceof CollaborationStartedMessage))
              {
                CollaborationStartedMessage.forCollaboration(this).send();
                collaborationStartedMessageSent = true;
              }

            notifyAll();
            log();
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void unregisterDeliveringMessage (final @Nonnull Object message)
      {
        log.trace("unregisterDeliveringMessage({})", message);

        if (message.getClass().getAnnotation(Message.class).daemon())
          {
            deliveringMessages.remove(message);
            notifyAll();
            log();
            eventuallySendCompletionMessage(message);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void registerPendingMessage (final @Nonnull Object message)
      {
        log.trace("registerPendingMessage({})", message);

        if (message.getClass().getAnnotation(Message.class).daemon())
          {
            pendingMessages.add(new IdentityWrapper(message));
            notifyAll();
            log();
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public synchronized void unregisterPendingMessage (final @Nonnull Object message)
      {
        log.trace("unregisterPendingMessage({})", message);

        if (message.getClass().getAnnotation(Message.class).daemon())
          {
            pendingMessages.remove(new IdentityWrapper(message));
            notifyAll();
            log();
            eventuallySendCompletionMessage(message);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void eventuallySendCompletionMessage (final @Nonnull Object message)
      {
        final int enqueuedMessageCount = deliveringMessages.size()
                                       + pendingMessages.size()
                                       + runningThreads.size()
                                       + suspensionTokens.size();

        if (!completed && (enqueuedMessageCount == 0))
          {
            log.debug(">>>> sending completion message for {}", this);
            completed = true;
            THREAD_LOCAL.remove();
            CollaborationCompletedMessage.forCollaboration(this).send();
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void log() // FIXME: drop or move out of synchronized
      {
//        log.trace("{}: delivering messages: {}, pending messages: {}, running threads: {}, suspension tokens: {}",
//                  new Object[] {this, deliveringMessages.size(), pendingMessages.size(), runningThreads.size(), suspensionTokens.size()});
//
//        if (pendingMessages.size() < 2)
//          {
//            log.trace(">>>> pending messages: {}", pendingMessages);
//          }
      }
  }
