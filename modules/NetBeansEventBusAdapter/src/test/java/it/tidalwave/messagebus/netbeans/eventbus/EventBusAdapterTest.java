/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus.netbeans.eventbus;

import javax.annotation.Nonnull;
import org.netbeans.platformx.eventbus.api.EventBus;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.EventBusAdapterTestSupport;
import static it.tidalwave.messagebus.netbeans.eventbus.EventBusAdapter.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class EventBusAdapterTest extends EventBusAdapterTestSupport
  {
    @Override
    protected void publish (final @Nonnull Object message) 
      {
        EventBus.getDefault().publish(message);
      }

    @Override @Nonnull
    protected MessageBusHelper.AdapterFactory getAdapterFactory() 
      {
        return PLATFORMX_EVENTBUS_ADAPTER;
      }
  }
