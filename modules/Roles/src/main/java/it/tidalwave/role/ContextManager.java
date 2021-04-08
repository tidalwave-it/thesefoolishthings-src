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
package it.tidalwave.role;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.Task;
import it.tidalwave.role.spi.ContextManagerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A facility to register and unregister global and local DCI contexts.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface ContextManager
  {
    // We must compile with Java 7
    public static interface Supplier<V>
      {
        public V get();
      }

    /*******************************************************************************************************************
     *
     * A locator for the {@link ContextManager} which uses the {@link ServiceLoader} facility to be independent of
     * any DI framework.
     *
     * This locator caches the internal reference and this is ok for production use; during tests, since multiple
     * contexts are typically created and destroyed for each test, you should call {@link #reset()} after each test
     * has been completed.
     *
     ******************************************************************************************************************/
    @Slf4j @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Locator
      {
        @CheckForNull
        private static ContextManager contextManager;

        @CheckForNull
        private static ContextManagerProvider contextManagerProvider;

        @Nonnull
        public static synchronized ContextManager find ()
          {
            if (contextManager == null)
              {
                if (contextManagerProvider == null)
                  {
                    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    final Iterator<ContextManagerProvider> i =
                            ServiceLoader.load(ContextManagerProvider.class, classLoader).iterator();

                    if (!i.hasNext())
                      {
                        throw new RuntimeException("No ServiceProvider for ContextManagerProvider");
                      }

                    contextManagerProvider = Objects.requireNonNull(i.next());
                    assert contextManagerProvider != null : "contextManagerProvider is null";
                    log.trace("ContextManagerProvider instantiated from META-INF: {}", contextManagerProvider);
                  }

                contextManager = Objects.requireNonNull(contextManagerProvider.getContextManager(),
                                                        "Cannot find ContextManager");
              }

            assert contextManager != null : "contextManager is null";
            return contextManager;
          }

        /***************************************************************************************************************
         *
         * <b>This method is for testing only.</b> Sets the global {@link ContextManagerProvider}. See note about
         * {@link #reset()}.
         *
         * @param   provider    the provider
         * @see     #reset()
         *
         **************************************************************************************************************/
        public static void set (@Nonnull final ContextManagerProvider provider)
          {
            contextManager = null;
            contextManagerProvider = provider;
          }

        /***************************************************************************************************************
         *
         * <b>This method is for testing only.</b> Resets the global {@link ContextManagerProvider}; it must be called
         * at the test completion whenever {@link #set(ContextManagerProvider)} has been called, to avoid polluting the
         * context of further tests.
         *
         * @see     #set(ContextManagerProvider)
         *
         **************************************************************************************************************/
        public static void reset()
          {
            contextManager = null;
            contextManagerProvider = null;
          }
      }

    /*******************************************************************************************************************
     *
     * Returns the list of current contexts, ordered by their priority.
     *
     * @return  the list of current contexts
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<Object> getContexts();

    /*******************************************************************************************************************
     *
     * Finds a current context instance of the given type.
     *
     * @param   <T>                the static context type
     * @param   contextType        the dynamic context type
     * @return                     the requested context
     * @throws  NotFoundException  if no context of that type is found
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T findContextOfType (@Nonnull Class<T> contextType)
      throws NotFoundException;

    /*******************************************************************************************************************
     *
     * Adds a global context.
     *
     * @param  context             the new context
     *
     ******************************************************************************************************************/
    public void addGlobalContext (@Nonnull Object context);

    /*******************************************************************************************************************
     *
     * Removes a global context.
     *
     * @param  context            the context
     *
     ******************************************************************************************************************/
    public void removeGlobalContext (@Nonnull Object context);

    /*******************************************************************************************************************
     *
     * Adds a local context.
     *
     * @param  context            the new context
     *
     ******************************************************************************************************************/
    public void addLocalContext (@Nonnull Object context);

    /*******************************************************************************************************************
     *
     * Removes a local context.
     *
     * @param  context            the context
     *
     ******************************************************************************************************************/
    public void removeLocalContext (@Nonnull Object context);

    /*******************************************************************************************************************
     *
     * Runs a {@link Task} associated with a new local context.
     *
     * @param  <V>                the type of the returned value
     * @param  <T>                the type of the exception that can be thrown
     * @param  context            the context
     * @param  task               the task
     * @return                    the value produced by the task
     * @throws T                  the exception(s) thrown by the task
     *
     ******************************************************************************************************************/
    public <V, T extends Throwable> V runWithContext (@Nonnull Object context, @Nonnull Task<V, T> task)
      throws T;

    /*******************************************************************************************************************
     *
     * Runs a {@link Task} associated with a new bunch of local contexts.
     *
     * @param  <V>                the type of the returned value
     * @param  <T>                the type of the exception that can be thrown
     * @param  contexts           the contexts
     * @param  task               the task
     * @return                    the value produced by the task
     * @throws T                  the exception(s) thrown by the task
     *
     ******************************************************************************************************************/
    public <V, T extends Throwable> V runWithContexts (@Nonnull List<Object> contexts, @Nonnull Task<V, T> task)
      throws T;

    /*******************************************************************************************************************
     *
     * Runs a task associated with a new local context. This variant fits functional interfaces.
     *
     * @param  <V>                the type of the returned value of the task
     * @param  context            the context
     * @param  task               the task
     * @return                    the value produced by the task
     *
     ******************************************************************************************************************/
    public <V> V runWithContext (@Nonnull Object context, @Nonnull Supplier<V> task);

    /*******************************************************************************************************************
     *
     * Runs a task associated with a new bunch of local contexts. This variant fits functional interfaces.
     *
     * @param  <V>                the type of the returned value
     * @param  contexts           the context
     * @param  task               the task
     * @return                    the value produced by the task
     *
     ******************************************************************************************************************/
    public <V> V runWithContexts (@Nonnull List<Object> contexts, @Nonnull Supplier<V> task);
  }
