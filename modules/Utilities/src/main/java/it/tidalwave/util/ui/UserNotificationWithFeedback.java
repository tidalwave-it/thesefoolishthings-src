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
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import lombok.Getter;
import lombok.ToString;
import static it.tidalwave.util.BundleUtilities.getMessage;

/***********************************************************************************************************************
 *
 * This class models a user notification where a feedback is expected (confirmation or cancellation).
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
@ToString(callSuper = true)
public class UserNotificationWithFeedback extends UserNotification
  {
    /*******************************************************************************************************************
     *
     * This class provides a few callback methods to notify a choice from the user.
     *
     ******************************************************************************************************************/
    public static class Feedback
      {
        /***************************************************************************************************************
         *
         * Callback method invoked when the user confirms an operation.
         *
         * @throws  Exception  in cases of error
         *
         **************************************************************************************************************/
        public void onConfirm()
          throws Exception
          {
          }

        /***************************************************************************************************************
         *
         * Callback method invoked when the user cancels an operation.
         *
         * @throws  Exception  in cases of error
         *
         **************************************************************************************************************/
        public void onCancel()
          throws Exception
          {
          }
      }

    @Getter
    protected final Feedback feedback;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    protected UserNotificationWithFeedback (final @Nonnull String text,
                                            final @Nonnull String caption,
                                            final @Nonnull Feedback feedback)
      {
        super(text, caption);
        this.feedback = feedback;
      }

    /*******************************************************************************************************************
     *
     * Creates a notification with empty caption and text.
     *
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserNotificationWithFeedback notificationWithFeedback()
      {
        return new UserNotificationWithFeedback("", "", new Feedback());
      }

    /*******************************************************************************************************************
     *
     * Associates a caption to the notification.
     *
     * @param  caption       the caption
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (final @Nonnull String caption)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /*******************************************************************************************************************
     *
     * Associates a caption to the notification, retrieved from a resource bundle.
     *
     * @param  bundleClass   the class where to search the resource bundle from
     * @param  resourceName  the resource name of the caption in the bundle
     * @param  params        some (optional) parameters to the resource
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (final @Nonnull Class<?> bundleClass,
                                                     final @Nonnull String resourceName,
                                                     final @Nonnull Object ... params)
      {
        return new UserNotificationWithFeedback(text, getMessage(bundleClass, resourceName, params), feedback);
      }

    /*******************************************************************************************************************
     *
     * Associates a text to the notification.
     *
     * @param  text          the text
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withText (final @Nonnull String text)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /*******************************************************************************************************************
     *
     * Associates a text to the notification, retrieved from a resource bundle.
     *
     * @param  bundleClass   the class where to search the resource bundle from
     * @param  resourceName  the resource name of the text in the bundle
     * @param  params        some (optional) parameters to the resource
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withText (final @Nonnull Class<?> bundleClass,
                                                  final @Nonnull String resourceName,
                                                  final @Nonnull Object ... params)
      {
        return new UserNotificationWithFeedback(getMessage(bundleClass, resourceName, params), caption, feedback);
      }

    /*******************************************************************************************************************
     *
     * Associates a {@link Feedback} to the notification.
     *
     * @param  feedback    the {@code Feedback} to associate
     * @return             the notification
     *
     ******************************************************************************************************************/
    @Nonnull
    public UserNotificationWithFeedback withFeedback (final @Nonnull Feedback feedback)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /*******************************************************************************************************************
     *
     * Notifies a confirmation to the user notification.
     *
     * @throws  Exception  in cases of error
     *
     ******************************************************************************************************************/
    public void confirm()
      throws Exception
      {
        feedback.onConfirm();
      }

    /*******************************************************************************************************************
     *
     * Notifies a cancellation to the user notification.
     *
     * @throws  Exception  in cases of error
     *
     ******************************************************************************************************************/
    public void cancel()
      throws Exception
      {
        feedback.onCancel();
      }
  }
