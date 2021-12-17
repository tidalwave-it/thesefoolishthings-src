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
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentMatcher;
import static lombok.AccessLevel.PACKAGE;

/***********************************************************************************************************************
 *
 * @stereotype  mockito matcher
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j @RequiredArgsConstructor(access = PACKAGE)
public class UserNotificationWithFeedbackMatcher implements ArgumentMatcher<UserNotificationWithFeedback>
  {
    @Nonnull
    private final String captionRegex;

    @Nonnull
    private final String textRegex;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public boolean matches (@Nullable final UserNotificationWithFeedback notification)
      {
        return (notification != null)
                && Pattern.matches(captionRegex, notification.getCaption())
                && Pattern.matches(textRegex, notification.getText());
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format(
                "UserNotificationWithFeedback(text matching %s, caption matching %s, feedback doesn't matter)",
                textRegex, captionRegex);
      }
  }
