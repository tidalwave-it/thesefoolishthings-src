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
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import java.awt.EventQueue;
import lombok.NoArgsConstructor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static lombok.AccessLevel.*;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = PRIVATE) @Slf4j
public final class UserNotificationWithFeedbackTestHelper
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // FIXME: make private
    public static final Answer<Void> CONFIRM = new Answer<Void>()
      {
        public Void answer (final @Nonnull InvocationOnMock invocation)
          throws Exception
          {
            EventQueue.invokeAndWait(new Runnable() // FIXME: use Platform.runLater() and wait
              {
                @Override
                public void run()
                  {
                    try
                      {
                        final UserNotificationWithFeedback notification =
                                (UserNotificationWithFeedback)invocation.getArguments()[0];
                        log.info(">>>> mock UI confirming {}...", notification);
                        notification.confirm();
                      }
                    catch (Exception e)
                      {
                        log.debug("", e);
                      }
                  }
              });
            return null;
          }
      };

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // FIXME: make private
    public static final Answer<Void> CANCEL = new Answer<Void>()
      {
        public Void answer (final @Nonnull InvocationOnMock invocation)
          throws Exception
          {
            EventQueue.invokeAndWait(new Runnable() // FIXME: use Platform.runLater() and wait
              {
                @Override
                public void run()
                  {
                    try
                      {
                        final UserNotificationWithFeedback notification =
                                (UserNotificationWithFeedback)invocation.getArguments()[0];
                        log.info(">>>> mock UI cancelling {}...", notification);
                        notification.cancel();
                      }
                    catch (Exception e)
                      {
                        log.debug("", e);
                      }
                  }
              });
            return null;
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserNotificationMatcher notification (final @Nonnull String caption, final @Nonnull String text)
      {
        return new UserNotificationMatcher(caption, text);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserNotificationWithFeedbackMatcher notificationWithFeedback (final @Nonnull String caption,
                                                                                final @Nonnull String text)
      {
        return new UserNotificationWithFeedbackMatcher(caption, text);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static final Answer<Void> confirm()
      {
        return CONFIRM;
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static final Answer<Void> cancel()
      {
        return CANCEL;
      }
  }
