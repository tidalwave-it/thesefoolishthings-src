/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import it.tidalwave.util.spi.DefaultProcessExecutor;

/***********************************************************************************************************************
 *
 * A facility that provides means for launching an external process, scraping its stdout and stderr in real-time and 
 * sending commands by means of its stdin.
 * 
 * @see     DefaultProcessExecutor
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 * @since   1.39
 *
 **********************************************************************************************************************/
@ThreadSafe
public interface ProcessExecutor
  {
    /*******************************************************************************************************************
     *
     * This interface provides operations that can be performed on the stdout or stderr consoles attached to the 
     * external process.
     *
     ******************************************************************************************************************/
    public static interface ConsoleOutput
      {
        /***************************************************************************************************************
         *
         * A listener that is invoked whenever a line is read from the console.
         *
         **************************************************************************************************************/
        public static interface Listener
          {
            public void onReceived (@Nonnull String string);   
          }
        
        /***************************************************************************************************************
         *
         * Returns {@code true} if the latest received line matches the given regular expression.
         * 
         * @param   regexp      the regular expression
         * @return              {@code true} in case of match
         *
         **************************************************************************************************************/
        public boolean latestLineMatches (@Nonnull String regexp);

        /***************************************************************************************************************
         *
         * Returns a list of lines that match the given regular expression.
         * 
         * @param   regexp      the regular expression
         * @return              the list of matching lines
         *
         **************************************************************************************************************/
        @Nonnull
        public List<String> filteredBy (@Nonnull String regexp);

        /***************************************************************************************************************
         *
         * Returns a {@link Scanner} over the latest line matching a given regular expression, with the specific
         * delimiter regular expression.
         * 
         * @param   regexp              the regular expression for the filter
         * @param   delimiterRegexp     the regular expression for the {@code Scanner}
         * @return                      the list of matching lines
         *
         **************************************************************************************************************/
        @Nonnull
        public Scanner filteredAndSplitBy (@Nonnull String regexp, @Nonnull String delimiterRegexp);

        /***************************************************************************************************************
         *
         * Waits for a line matching the given regular expression to appear.
         * 
         * @param   regexp                  the regular expression
         * @return                          itself for chaining methods
         * @throws  InterruptedException    if the wait has been interrupted
         * @throws  IOException             in case the process has terminated or another I/O error
         *
         **************************************************************************************************************/
        @Nonnull
        public ConsoleOutput waitFor (@Nonnull String regexp)
          throws InterruptedException, IOException;

        /***************************************************************************************************************
         *
         * Clears the buffer of lines. This means that no filtering or waiting operation can be performed on the
         * output produced so far. It will be possible to perform further operations on the output produced from now
         * on.
         *
         **************************************************************************************************************/
        public void clear();
    
        /***************************************************************************************************************
         *
         * Sets a listener.
         * 
         * @see Listener
         * @param   listener        the listener
         *
         **************************************************************************************************************/
        public void setListener (@Nonnull Listener listener);

        /***************************************************************************************************************
         *
         * Returns the set listener
         * 
         * @see Listener
         * @return     the listener
         *
         **************************************************************************************************************/
        @CheckForNull
        public Listener getListener();
    }

    /*******************************************************************************************************************
     *
     * Adds some arguments to pass to the external process.
     * 
     * @param       arguments       the arguments
     * @return                      itself for chaining methods
     *
     ******************************************************************************************************************/
    @Nonnull
    public ProcessExecutor withArguments (@Nonnull String ... arguments);
    
    /*******************************************************************************************************************
     *
     * Adds a single argument to pass to the external process.
     * 
     * @param       argument        the argument
     * @return                      itself for chaining methods
     *
     ******************************************************************************************************************/
    @Nonnull
    public ProcessExecutor withArgument (@Nonnull String argument);

    /*******************************************************************************************************************
     *
     * Starts the external process.
     * 
     * @return                      itself for chaining methods
     * @throws      IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public ProcessExecutor start()
      throws IOException;

    /*******************************************************************************************************************
     *
     * Stops the external process.
     *
     ******************************************************************************************************************/
    public void stop();

    /*******************************************************************************************************************
     *
     * Waits for the termination of the external process.
     *
     * @return                          itself for chaining methods
     * @throws  InterruptedException    if the wait has been interrupted
     * @throws  IOException             in case of I/O error
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ProcessExecutor waitForCompletion()
      throws IOException, InterruptedException;

    /*******************************************************************************************************************
     *
     * Sends a string to the stdin of the running process. If a carriage return is needed, it must be explicitly placed
     * in the string.
     * 
     * @param   string      the string to send
     * @return                          itself for chaining methods
     * @throws  IOException             in case of I/O error
     *
     ******************************************************************************************************************/
    @Nonnull
    public ProcessExecutor send (@Nonnull String string)
      throws IOException;

    /*******************************************************************************************************************
     *
     * Returns the stdout console.
     * 
     * @return      the console
     *
     ******************************************************************************************************************/
    @Nonnull
    public ConsoleOutput getStdout();

    /*******************************************************************************************************************
     *
     * Returns the stderr console.
     * 
     * @return      the console
     *
     ******************************************************************************************************************/
    @Nonnull
    public ConsoleOutput getStderr();
  }

