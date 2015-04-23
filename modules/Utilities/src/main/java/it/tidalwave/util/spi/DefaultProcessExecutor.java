/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.spi;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import it.tidalwave.util.ProcessExecutor;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @since   1.39
 *
 **********************************************************************************************************************/
@ThreadSafe @NoArgsConstructor(access=AccessLevel.PRIVATE) @Slf4j
public class DefaultProcessExecutor implements ProcessExecutor
  {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor(access=AccessLevel.PACKAGE)
    public class DefaultConsoleOutput implements ConsoleOutput
      {
        @Nonnull
        private final String name;

        @Nonnull
        private final InputStream input;

        @Getter
        private final List<String> content = Collections.synchronizedList(new ArrayList<String>());

        private volatile String latestLine;

        private volatile int li = 0;

        private final AtomicBoolean started = new AtomicBoolean();
        
        @CheckForNull @Setter @Getter
        private Listener listener;

        /***************************************************************************************************************
         *
         *
         ***************************************************************************************************************/
        private final Runnable reader = new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    read();
                  }
                catch (IOException e)
                  {
                    log.warn("while reading from " + name, e);
                  }
              }
          };

        /***************************************************************************************************************
         *
         *
         ***************************************************************************************************************/
        private final Runnable logger = new Runnable()
          {
            @Override
            public void run()
              {
                int l = 0;

                for (;;)
                  {
                    try
                      {
                        if ((l != li) && (latestLine != null))
                          {
                            log.trace(">>>>>>>> {} {}", name, latestLine);
                          }

                        l = li;
                        Thread.sleep(500);
                      }
                    catch (InterruptedException e)
                      {
                        return;
                      }
                    catch (Throwable e)
                      {
                        return;
                      }
                  }
              }
          };

        /***************************************************************************************************************
         *
         * Should not be used by the programmer.
         *
         ***************************************************************************************************************/
        @Nonnull
        public ConsoleOutput start()
          {
            if (started.getAndSet(true))
              {
                throw new IllegalStateException("Already started");
              }

            log.info("{} - started", name);
            executorService.submit(reader);
            executorService.submit(logger);
            return this;
          }

        /***************************************************************************************************************
         *
         * {@inheritDoc}
         *
         ***************************************************************************************************************/
        @Override
        public boolean latestLineMatches (final @Nonnull String regexp)
          {
            String s = null;

            if (latestLine != null)
              {
                s = latestLine;
              }
            else if (!content.isEmpty())
              {
                s = content.get(content.size() - 1);
              }

            log.trace(">>>> testing '{}' for '{}'", s, regexp);
            return (s == null) ? false : Pattern.compile(regexp).matcher(s).matches();
            // FIXME: sync
          }

        /***************************************************************************************************************
         *
         * {@inheritDoc}
         *
         ***************************************************************************************************************/
        @Override @Nonnull
        public Scanner filteredAndSplitBy (final @Nonnull String filterRegexp, final @Nonnull String delimiterRegexp)
          {
            final String string = filteredBy(filterRegexp).get(0);
            return new Scanner(string).useDelimiter(Pattern.compile(delimiterRegexp));
          }

        /***************************************************************************************************************
         *
         * {@inheritDoc}
         *
         ***************************************************************************************************************/
        @Override @Nonnull
        public List<String> filteredBy (final @Nonnull String regexp)
          {
            final Pattern pattern = Pattern.compile(regexp);
            final List<String> result = new ArrayList<String>();
            final ArrayList<String> strings = new ArrayList<String>(content);

            // TODO: sync
            if (latestLine != null)
              {
                strings.add(latestLine);
              }

            for (final String s : strings)
              {
//                log.trace(">>>>>>>> matching '{}' with '{}'...", s, filter);
                final Matcher m = pattern.matcher(s);

                if (m.matches())
                  {
                    result.add(m.group(1));
                  }
              }

            return result;
          }

        /***************************************************************************************************************
         *
         * {@inheritDoc}
         *
         ***************************************************************************************************************/
        @Override @Nonnull
        public ConsoleOutput waitFor (final @Nonnull String regexp)
          throws InterruptedException, IOException
          {
            log.debug("{} - waitFor({})", name, regexp);

            while (filteredBy(regexp).isEmpty())
              {
                try
                  {
                    final int exitValue = process.exitValue();
                    throw new IOException("Process exited with " + exitValue);
                  }
                catch (IllegalThreadStateException e) // ok, process not terminated yet
                  {
                    synchronized (this)
                      {
                        wait(50); // FIXME: polls because it doesn't get notified
                      }
                  }
              }

            return this;
          }

        /***************************************************************************************************************
         *
         * {@inheritDoc}
         *
         ***************************************************************************************************************/
        @Override
        public void clear()
          {
            content.clear();
            latestLine = null;
          }

        /***************************************************************************************************************
         *
         *
         ***************************************************************************************************************/
        private void read()
          throws IOException
          {
            final @Cleanup InputStreamReader is = new InputStreamReader(input);
            StringBuilder l = new StringBuilder();

            for (;;)
              {
                int c = is.read();

                if (c < 0)
                  {
                    break;
                  }

//                if (c == 10)
//                  {
//                    continue;
//                  }

                if ((c == 13) || (c == 10))
                  {
                    latestLine = l.toString();
                    li++;
                    content.add(latestLine);
                    l = new StringBuilder();
                    log.trace(">>>>>>>> {} {}", name, latestLine);
                    
                    if (listener != null)
                      {
                        listener.onReceived(latestLine);
                      }
                  }
                else
                  {
                    l.append((char)c);
                    latestLine = l.toString();
                    li++;
                  }

                synchronized (this)
                  {
                    notifyAll();
                  }
              }

            log.debug(">>>>>> {} closed", name);
            is.close();
          }
      }

    private final List<String> arguments = new ArrayList<String>();

    private Process process;

    @Getter
    private ConsoleOutput stdout;

    @Getter
    private ConsoleOutput stderr;

    private PrintWriter stdin;

    /*******************************************************************************************************************
     *
     * Factory method for associating an executable. It returns an intermediate executor that must be configured and
     * later started. Under Windows, the '.exe' suffix is automatically appended to the name of the executable.
     * 
     * @see #start() 
     * 
     * @param       executable      the executable (with the full path)
     * @return                      the executor
     *
     ******************************************************************************************************************/
    @Nonnull
    public static DefaultProcessExecutor forExecutable (final @Nonnull String executable)
      {
        final DefaultProcessExecutor executor = new DefaultProcessExecutor();
        executor.arguments.add(new File(executable + (isWindows() ? ".exe" : "")).getAbsolutePath());
        return executor;
      }

//    /*******************************************************************************************************************
//     *
//     *
//     ******************************************************************************************************************/
//    @Nonnull
//    private static String findPath (final @Nonnull String executable)
//      throws NotFoundException
//      {
//        for (final String path : System.getenv("PATH").split(File.pathSeparator))
//          {
//            final File file = new File(new File(path), executable);
//
//            if (file.canExecute())
//              {
//                return file.getAbsolutePath();
//              }
//          }
//
//        throw new NotFoundException("Can't find " + executable + " in PATH");
//      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DefaultProcessExecutor withArgument (final @Nonnull String argument)
      {
        arguments.add(argument);
        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DefaultProcessExecutor withArguments (final @Nonnull String ... arguments)
      {
        this.arguments.addAll(Arrays.asList(arguments));
        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DefaultProcessExecutor start()
      throws IOException
      {
        log.info(">>>> executing {} ...", arguments);

        final List<String> environment = new ArrayList<String>();

        for (final Entry<String, String> e : System.getenv().entrySet())
          {
            environment.add(String.format("%s=%s", e.getKey(), e.getValue()));
          }

        log.info(">>>> environment: {}", environment);
        process = Runtime.getRuntime().exec(arguments.toArray(new String[0]),
                                            environment.toArray(new String[0]));

        stdout = new DefaultConsoleOutput("out", process.getInputStream()).start();
        stderr = new DefaultConsoleOutput("err", process.getErrorStream()).start();
        stdin  = new PrintWriter(process.getOutputStream(), true);

        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void stop()
      {
        log.info("stop()");
        process.destroy();
        executorService.shutdownNow();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DefaultProcessExecutor waitForCompletion()
      throws IOException, InterruptedException
      {
        if (process.waitFor() != 0)
          {
//            throw new IOException("Process exited with " + process.exitValue()); FIXME
          }

        return this;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public DefaultProcessExecutor send (final @Nonnull String string)
      throws IOException
      {
        log.debug(">>>> sending '{}'...", string.replaceAll("\n", "<CR>"));
        stdin.print(string);
        stdin.flush();
        return this;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private static boolean isWindows()
      {
        return System.getProperty ("os.name").toLowerCase().startsWith("windows");
      }
  }
