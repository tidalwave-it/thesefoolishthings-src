/***********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://solidblue.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/solidblue-src
 *
 **********************************************************************************************************************/
package it.tidalwave.messagebus.impl.netbeans;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import org.openide.util.lookup.ServiceProvider;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.spi.MessageBusSupport;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An implementation of {@link MessageBus} for the NetBeans Platform. FIXME: indeed this is not related to the Platform!
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=MessageBus.class)
@ThreadSafe @Slf4j
public class NetBeansPlatformMessageBus extends MessageBusSupport
  {
//    private final RequestProcessor executor = RequestProcessor.getDefault(); FIXME
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected Executor getExecutor() 
      {
        return executor;
      }
  }