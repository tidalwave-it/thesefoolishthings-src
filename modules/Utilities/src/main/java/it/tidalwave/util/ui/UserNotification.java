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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.openide.util.NbBundle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import static lombok.AccessLevel.PROTECTED;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(access=PROTECTED) @ToString
public class UserNotification
  {
    @Getter
    protected final String text;
    
    @Getter
    protected final String caption;

    @Nonnull
    public static UserNotification notification()
      {
        return new UserNotification("", "");  
      }
    
    @Nonnull
    public UserNotification withCaption (final @Nonnull String caption)
      {
        return new UserNotification(text, caption);
      }
            
    @Nonnull
    public UserNotification withText (final @Nonnull String text)
      {
        return new UserNotification(text, caption);
      }
    
    @Nonnull
    public UserNotification withCaption (final @Nonnull Class<?> bundleClass, 
                                         final @Nonnull String resourceName,
                                         final @Nonnull Object ... params)
      {
        return new UserNotification(text, NbBundle.getMessage(bundleClass, resourceName, params));
      }
            
    @Nonnull
    public UserNotification withText (final @Nonnull Class<?> bundleClass, 
                                      final @Nonnull String resourceName,
                                      final @Nonnull Object ... params)
      {
        return new UserNotification(NbBundle.getMessage(bundleClass, resourceName, params), caption);
      }
  }