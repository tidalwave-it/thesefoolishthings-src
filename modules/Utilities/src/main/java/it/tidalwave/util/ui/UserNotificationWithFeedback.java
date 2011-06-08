/***********************************************************************************************************************
 *
 * blueBill Core - open source birding
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
 * WWW: http://bluebill.tidalwave.it
 * SCM: https://kenai.com/hg/bluebill~core-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import lombok.Getter;
import lombok.ToString;
import org.openide.util.NbBundle;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @ToString(callSuper=true)
public class UserNotificationWithFeedback extends UserNotification
  {
    public static class Feedback
      {
        public void onConfirm()
          throws Exception
          {  
          }

        public void onCancel()
          throws Exception
          {        
          }
      }

    @Getter
    protected final Feedback feedback;
    
    protected UserNotificationWithFeedback (final @Nonnull String text,
                                            final @Nonnull String caption,
                                            final @Nonnull Feedback feedback)
      {
        super(text, caption);
        this.feedback = feedback;
      }
    
    @Nonnull
    public static UserNotificationWithFeedback notificationWithFeedback()
      {
        return new UserNotificationWithFeedback("", "", new Feedback());
      }
    
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (final @Nonnull String caption)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }
            
    @Override @Nonnull
    public UserNotificationWithFeedback withText (final @Nonnull String text)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }
    
    @Override @Nonnull
    public UserNotificationWithFeedback withCaption (final @Nonnull Class<?> bundleClass, 
                                                     final @Nonnull String resourceName,
                                                     final @Nonnull Object ... params)
      {
        return new UserNotificationWithFeedback(text, NbBundle.getMessage(bundleClass, resourceName, params), feedback);
      }
            
    @Override @Nonnull
    public UserNotificationWithFeedback withText (final @Nonnull Class<?> bundleClass, 
                                                  final @Nonnull String resourceName,
                                                  final @Nonnull Object ... params)
      {
        return new UserNotificationWithFeedback(NbBundle.getMessage(bundleClass, resourceName, params), caption, feedback);
      }
    
    @Nonnull
    public UserNotificationWithFeedback withFeedback (final @Nonnull Feedback feedback)
      {
        return new UserNotificationWithFeedback(text, caption, feedback);
      }
    
    public void confirm()
      throws Exception
      {
        feedback.onConfirm();  
      }
    
    public void cancel()
      throws Exception
      {
        feedback.onCancel();  
      }
  } 
