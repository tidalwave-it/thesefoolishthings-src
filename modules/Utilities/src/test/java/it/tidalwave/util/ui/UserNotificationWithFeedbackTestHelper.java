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
package it.tidalwave.util.ui;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;
import java.awt.EventQueue;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static lombok.AccessLevel.PRIVATE;

/***************************************************************************************************************************************************************
 *
 * A factory class to create testing matchers.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@NoArgsConstructor(access = PRIVATE) @Slf4j
public final class UserNotificationWithFeedbackTestHelper
  {
    /***********************************************************************************************************************************************************
     * Creates a {@link UserNotification} matcher for the given caption and text.
     *
     * @param   captionRegex    the regular expression that should match the caption
     * @param   textRegex       the regular expression that should match the text
     * @return                  the matcher
     **********************************************************************************************************************************************************/
    @Nonnull
    public static UserNotificationMatcher notification (@Nonnull final String captionRegex,
                                                        @Nonnull final String textRegex)
      {
        return new UserNotificationMatcher(captionRegex, textRegex);
      }

    /***********************************************************************************************************************************************************
     * Creates a {@link UserNotificationWithFeedback} matcher for the given caption and text.
     *
     * @param   captionRegex    the regular expression that should match the caption
     * @param   textRegex       the regular expression that should match the text
     * @return                  the matcher
     **********************************************************************************************************************************************************/
    @Nonnull
    public static UserNotificationWithFeedbackMatcher notificationWithFeedback (@Nonnull final String captionRegex,
                                                                                @Nonnull final String textRegex)
      {
        return new UserNotificationWithFeedbackMatcher(captionRegex, textRegex);
      }

    /***********************************************************************************************************************************************************
     * An {@link Answer} that triggers a confirmation to a {@link UserNotificationWithFeedback}.
     *
     * @return  the {@code Answer}
     **********************************************************************************************************************************************************/
    public static Answer<Void> confirm()
      {
        return CONFIRM;
      }

    /***********************************************************************************************************************************************************
     * An {@link Answer} that does a thing and then triggers a confirmation to a {@link UserNotificationWithFeedback}.
     *
     * @param   thingToDo   the thing to do
     * @return              the {@code Answer}
     * @since               3.2-ALPHA-24
     **********************************************************************************************************************************************************/
    public static Answer<Void> doAndConfirm (@Nonnull final Consumer<? super InvocationOnMock> thingToDo)
      {
        return invocationOnMock ->
          {
            thingToDo.accept(invocationOnMock);
            return CONFIRM.answer(invocationOnMock);
          };
      }

    /***********************************************************************************************************************************************************
     * An {@link Answer} that triggers a cancellation to a {@link UserNotificationWithFeedback}.
     *
     * @return  the {@code Answer}
     **********************************************************************************************************************************************************/
    public static Answer<Void> cancel()
      {
        return CANCEL;
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    private static final Answer<Void> CONFIRM = invocation ->
      {
        // FIXME: use Platform.runLater() and wait
        EventQueue.invokeAndWait(() ->
          {
            try
              {
                final var notification = findNotification(invocation);
                log.info(">>>> mock UI confirming {}...", notification);
                notification.confirm();
              }
            catch (Exception e)
              {
                log.debug("", e);
              }
          });

        return null;
      };

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    private static final Answer<Void> CANCEL = invocation ->
      {
        // FIXME: use Platform.runLater() and wait
        EventQueue.invokeAndWait(() ->
          {
            try
              {
                final var notification = findNotification(invocation);
                log.info(">>>> mock UI cancelling {}...", notification);
                notification.cancel();
              }
            catch (Exception e)
              {
                log.debug("", e);
              }
          });

        return null;
      };

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Nonnull
    private static UserNotificationWithFeedback findNotification (@Nonnull final InvocationOnMock invocation)
      {
        return (UserNotificationWithFeedback)Arrays.stream(invocation.getArguments())
                     .filter(a -> a instanceof UserNotificationWithFeedback)
                     .findFirst()
                     .orElseThrow();
      }
  }
