/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.stopwatch;

import java.util.Stack;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class StopWatch
  {
    private StopWatchStats stats;
    private long startTime;
    private long partialStartTime;
    private long accumulated;
    
    private final static ThreadLocal<Stack<StopWatch>> stack = new ThreadLocal<Stack<StopWatch>>()
      {
        @Override
        protected Stack<StopWatch> initialValue() 
          {
            return new Stack<StopWatch>();
          }
      };
    
    public static final StopWatch DUMMY_STOPWATCH = new StopWatch()
      {
        @Override
        public void cancel() 
          {
          }

        @Override
        public void stop() 
          {
          }
      };
    
    public static StopWatch create (final Class<?> clazz, final String name)
      {
        return new StopWatch(clazz, name);
      }
    
    protected StopWatch()
      {
        partialStartTime = 0;  
      }
    
    protected StopWatch (final Class<?> clazz, final String name)
      {
        final String baseName = clazz.getName().replace('.', '/') + "/" + name;
        // Create here the reference to stats, in order to avoid overhead in stop()
        stats = StopWatchStats.find(baseName);
        final Stack<StopWatch> currentStack = stack.get();
        
        if (!currentStack.isEmpty())
          {
            currentStack.peek().suspend();
          }
        
        currentStack.push(this);
        startTime = partialStartTime = System.nanoTime();
      }
    
    public void stop()
      {
        final long now = System.nanoTime();
        stats.addTimeSample(now - partialStartTime + accumulated, now - startTime);
        cancel();
      }
    
    public void cancel()
      {
        stats = null;  
        final Stack<StopWatch> currentStack = stack.get();
        currentStack.pop();
        
        if (!currentStack.isEmpty())
          {
            currentStack.peek().resume();
          }
      }
    
    protected void suspend()
      {
        accumulated += System.nanoTime() - partialStartTime;
      }
    
    protected void resume()
      {
        partialStartTime = System.nanoTime();
      }
  }
