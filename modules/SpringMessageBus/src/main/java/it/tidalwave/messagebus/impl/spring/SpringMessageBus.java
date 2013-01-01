/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.messagebus.impl.spring;

import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import org.springframework.core.task.TaskExecutor;
import it.tidalwave.messagebus.spi.MessageBusSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A simple implementation of {@link EventBus} based on Spring.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ThreadSafe @Slf4j
public class SpringMessageBus extends MessageBusSupport
  {
    @Getter @Setter @Nonnull
    private TaskExecutor taskExecutor;
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected Executor getExecutor() 
      {
        return taskExecutor;
      }
  }