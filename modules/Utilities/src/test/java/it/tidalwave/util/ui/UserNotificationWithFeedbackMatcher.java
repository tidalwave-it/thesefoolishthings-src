/***********************************************************************************************************************
 *
 * blueBill Mobile - Android - open source birding
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://bluebill.tidalwave.it/mobile
 * SCM: https://java.net/hg/bluebill~android-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.ui;

import it.tidalwave.util.ui.UserNotification;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.tidalwave.util.ui.UserNotificationWithFeedback;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
// FIXME: merge with the one in blueBill Core
public class UserNotificationWithFeedbackMatcher extends BaseMatcher<UserNotificationWithFeedback>
  {    
    private static final Logger log = LoggerFactory.getLogger(UserNotificationWithFeedbackMatcher.class);
    
    @Nonnull
    private final String caption;

    @Nonnull
    private final String text;
    
    private String d = "UserNotificationWithFeedback ?";

    private UserNotificationWithFeedback notification;
    
    /*******************************************************************************************************************
     *
     * 
     *
     ******************************************************************************************************************/
    /* package */ UserNotificationWithFeedbackMatcher (final @Nonnull String caption, final @Nonnull String text)
      {
        this.caption = caption;
        this.text = text;
        this.d = String.format("UserNotificationWithFeedback[text=%s, caption=%s]", text, caption);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public boolean matches (final @Nullable Object item)
      {
        if (!(item instanceof UserNotificationWithFeedback))
          {
            return false;
          }

        notification = (UserNotificationWithFeedback)item;

        final boolean b = Pattern.matches(caption, notification.getCaption()) &&
                          Pattern.matches(text, notification.getText());
        
        if (!b)
          {
            d = String.format("%s (instead is %s)", d, item);
          }
        
        return b;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void describeTo (final @Nonnull Description description)
      {
        description.appendText(d);
      }
  }
