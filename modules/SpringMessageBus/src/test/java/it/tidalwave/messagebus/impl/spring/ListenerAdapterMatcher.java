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
package it.tidalwave.messagebus.impl.spring;

import it.tidalwave.messagebus.impl.spring.MessageBusAdapterFactory.MessageBusListenerAdapter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.mockito.ArgumentMatcher;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(staticName = "listenerAdapter") @AllArgsConstructor @ToString
public class ListenerAdapterMatcher implements ArgumentMatcher<MessageBusListenerAdapter>
  {
    @With
    public String methodName;

    @With
    public Object owner;

    @With
    public Class<?> topic;

    @Override
    public boolean matches (final MessageBusListenerAdapter item)
      {
//        if (! (item instanceof MessageBusListenerAdapter))
//          {
//            return false;
//          }
//
        final MessageBusListenerAdapter listener = (MessageBusListenerAdapter)item;

        return (methodName.equals(listener.getMethod().getName())
                && (owner == listener.getOwner())
                && topic.equals(listener.getTopic()));
      }

//    @Override FIXME!
//    public void describeTo (final @Nonnull Description description)
//      {
//        description.appendText(toString());
//      }
  }
