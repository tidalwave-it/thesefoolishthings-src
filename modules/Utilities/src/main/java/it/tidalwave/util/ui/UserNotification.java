/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
import javax.annotation.concurrent.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import static it.tidalwave.util.BundleUtilities.getMessage;
import static lombok.AccessLevel.PROTECTED;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable
@RequiredArgsConstructor(access = PROTECTED) @ToString
public class UserNotification
  {
    @Getter
    protected final String text;

    @Getter
    protected final String caption;

    /*******************************************************************************************************************
     *
     * Creates a notification with empty caption and text.
     *
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Nonnull
    public static UserNotification notification()
      {
        return new UserNotification("", "");
      }

    /*******************************************************************************************************************
     *
     * Associates a caption to the notification.
     *
     * @param  caption       the caption
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Nonnull
    public UserNotification withCaption (final @Nonnull String caption)
      {
        return new UserNotification(text, caption);
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
    @Nonnull
    public UserNotification withCaption (final @Nonnull Class<?> bundleClass,
                                         final @Nonnull String resourceName,
                                         final @Nonnull Object ... params)
      {
        return new UserNotification(text, getMessage(bundleClass, resourceName, params));
      }

    /*******************************************************************************************************************
     *
     * Associates a text to the notification.
     *
     * @param  text          the text
     * @return               the notification
     *
     ******************************************************************************************************************/
    @Nonnull
    public UserNotification withText (final @Nonnull String text)
      {
        return new UserNotification(text, caption);
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
    @Nonnull
    public UserNotification withText (final @Nonnull Class<?> bundleClass,
                                      final @Nonnull String resourceName,
                                      final @Nonnull Object ... params)
      {
        return new UserNotification(getMessage(bundleClass, resourceName, params), caption);
      }
  }
