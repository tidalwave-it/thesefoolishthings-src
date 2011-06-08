/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class UserNotificationWithFeedbackMatcher extends BaseMatcher<UserNotificationWithFeedback>
  {
    @Nonnull
    private final String title;
    
    @Nonnull
    private final String message;

    @Nonnull
    public static UserNotificationWithFeedbackMatcher question (final @Nonnull String title, final @Nonnull String message)
      {
        return new UserNotificationWithFeedbackMatcher(title, message);  
      }
    
    public boolean matches (final @Nullable Object item) 
      {
        if (!(item instanceof UserNotificationWithFeedback))
          {
            return false;  
          }
        
        final UserNotificationWithFeedback userNotification = (UserNotificationWithFeedback)item;
        
        return Pattern.matches(title, userNotification.getCaption()) &&
               Pattern.matches(message, userNotification.getText());
      }

    public void describeTo (final @Nonnull Description description) 
      {
        description.appendText(new UserNotification(title, message).toString());
      }  
  }
