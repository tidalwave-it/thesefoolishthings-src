/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import it.tidalwave.util.Callback;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.With;
import static it.tidalwave.util.BundleUtilities.getMessage;

/***************************************************************************************************************************************************************
 *
 * This class models a user notification where a feedback is expected (confirmation or cancellation).
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Getter @Immutable
@ToString(callSuper = true)
public class UserNotificationWithFeedback extends UserNotification
  {
    /***********************************************************************************************************************************************************
     * This class provides a few callback methods to notify a choice from the user.
     **********************************************************************************************************************************************************/
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Feedback
      {
        @With
        private final Callback onConfirm;

        @With
        private final Callback onCancel;

        /***************************************************************************************************************
         *
         * Checks whether the current instance has a callback for confirmation.
         *
         * @return  {@code true} if the instance has the callback
         * @since   3.2-ALPHA-1
         *
         **************************************************************************************************************/
        public boolean canConfirm()
          {
            return onConfirm != Callback.EMPTY;
          }

        /***************************************************************************************************************
         *
         * Checks whether the current instance has a callback for cancellation.
         *
         * @return  {@code true} if the instance has the callback
         * @since   3.2-ALPHA-1
         *
         **************************************************************************************************************/
        public boolean canCancel()
          {
            return onCancel != Callback.EMPTY;
          }

        /***************************************************************************************************************
         *
         * Callback method invoked when the user confirms an operation.
         *
         * @throws  Exception  in cases of error
         *
         **************************************************************************************************************/
        @SuppressWarnings("RedundantThrows")
        @SneakyThrows(Throwable.class)
        private void onConfirm()
                throws Exception
          {
            onConfirm.call();
          }

        /***************************************************************************************************************
         *
         * Callback method invoked when the user cancels an operation.
         *
         * @throws  Exception  in cases of error
         *
         **************************************************************************************************************/
        @SuppressWarnings("RedundantThrows")
        @SneakyThrows(Throwable.class)
        private void onCancel()
                throws Exception
          {
            onCancel.call();
          }
      }

    protected final Feedback feedback;

    /***********************************************************************************************************************************************************
     * @param text          the notification text
     * @param caption       the notification caption
     * @param feedback      the feedback
     **********************************************************************************************************************************************************/
    protected UserNotificationWithFeedback (@Nonnull final String text,
                                            @Nonnull final String caption,
                                            @Nonnull final Feedback feedback)
      {
        super(text, caption);
        this.feedback = feedback;
      }

    /***********************************************************************************************************************************************************
     * Creates a notification with empty caption and text.
     *
     * @return               the notification
     **********************************************************************************************************************************************************/
    @Nonnull
    public static UserNotificationWithFeedback notificationWithFeedback()
      {
        return new UserNotificationWithFeedback("", "", feedback());
      }

    /***********************************************************************************************************************************************************
     * Associates a caption to the notification.
     *
     * @param  caption       the caption
     * @return               the notification
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (@Nonnull final String caption)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /***********************************************************************************************************************************************************
     * Associates a caption to the notification, retrieved from a resource bundle.
     *
     * @param  bundleClass   the class where to search the resource bundle from
     * @param  resourceName  the resource name of the caption in the bundle
     * @param  params        some (optional) parameters to the resource
     * @return               the notification
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (@Nonnull final Class<?> bundleClass,
                                                     @Nonnull final String resourceName,
                                                     @Nonnull final Object ... params)
      {
        return new UserNotificationWithFeedback(text, getMessage(bundleClass, resourceName, params), feedback);
      }

    /***********************************************************************************************************************************************************
     * Associates a text to the notification.
     *
     * @param  text          the text
     * @return               the notification
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withText (@Nonnull final String text)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /***********************************************************************************************************************************************************
     * Associates a text to the notification, retrieved from a resource bundle.
     *
     * @param  bundleClass   the class where to search the resource bundle from
     * @param  resourceName  the resource name of the text in the bundle
     * @param  params        some (optional) parameters to the resource
     * @return               the notification
     **********************************************************************************************************************************************************/
    @Override @Nonnull
    public UserNotificationWithFeedback withText (@Nonnull final Class<?> bundleClass,
                                                  @Nonnull final String resourceName,
                                                  @Nonnull final Object ... params)
      {
        return new UserNotificationWithFeedback(getMessage(bundleClass, resourceName, params), caption, feedback);
      }

    /***********************************************************************************************************************************************************
     * Associates a {@link Feedback} to the notification.
     *
     * @param  feedback    the {@code Feedback} to associate
     * @return             the notification
     **********************************************************************************************************************************************************/
    @Nonnull
    public UserNotificationWithFeedback withFeedback (@Nonnull final Feedback feedback)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }

    /***********************************************************************************************************************************************************
     * Notifies a confirmation to the user notification.
     *
     * @throws  Exception  in cases of error
     **********************************************************************************************************************************************************/
    public void confirm()
      throws Exception
      {
        feedback.onConfirm();
      }

    /***********************************************************************************************************************************************************
     * Notifies a cancellation to the user notification.
     *
     * @throws  Exception  in cases of error
     **********************************************************************************************************************************************************/
    public void cancel()
      throws Exception
      {
        feedback.onCancel();
      }

    /***********************************************************************************************************************************************************
     * Creates a new {@code Feedback} that does nothing. This method should be chained with {@code withOnConfirm()}
     * and/or {@code withOnCancel(Callback)} to specify the relative callbacks.
     *
     * <pre>
     *   feedback().withOnConfirm(this::doSomething).withOnCancel(this::doSomethingElse);
     * </pre>
     *
     * @return    a feedback that does nothing in any case
     * @since     3.2-ALPHA-1 (was previously on {@code Feedback8}
     **********************************************************************************************************************************************************/
    @Nonnull
    public static Feedback feedback()
      {
        return new Feedback(Callback.EMPTY, Callback.EMPTY);
      }
  }
