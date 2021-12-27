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

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.Task;
import it.tidalwave.util.impl.LazyReference;
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
        private static final LazyReference<ContextManager> CONTEXT_MANAGER_REF =
                LazyReference.of(Locator::findContextManager);

        private static final LazyReference<ContextManagerProvider> CONTEXT_MANAGER_PROVIDER_REF =
                LazyReference.of(Locator::findContextManagerProvider);

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        public static ContextManager find()
          {
            return CONTEXT_MANAGER_REF.get();
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
            CONTEXT_MANAGER_REF.clear();
            CONTEXT_MANAGER_PROVIDER_REF.set(provider);
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
            CONTEXT_MANAGER_REF.clear();
            CONTEXT_MANAGER_PROVIDER_REF.clear();
          }

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        private static ContextManager findContextManager()
          {
            return Objects.requireNonNull(CONTEXT_MANAGER_PROVIDER_REF.get().getContextManager(),
                                          "Cannot find ContextManager");
          }

        /***************************************************************************************************************
         *
         **************************************************************************************************************/
        @Nonnull
        private static ContextManagerProvider findContextManagerProvider()
          {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Iterator<ContextManagerProvider> i =
                    ServiceLoader.load(ContextManagerProvider.class, classLoader).iterator();

            if (!i.hasNext())
              {
                throw new RuntimeException("No ServiceProvider for ContextManagerProvider");
              }

            final ContextManagerProvider contextManagerProvider = Objects.requireNonNull(i.next(),
                                                                                         "contextManagerProvider is null");
            assert contextManagerProvider != null; // for SpotBugs
            log.info("ContextManagerProvider instantiated from META-INF: {}", contextManagerProvider);
            return contextManagerProvider;
          }
      }

    @FunctionalInterface
    public static interface RunnableWithException<E extends Throwable>
      {
        public void run()
                throws E;
      }

    @FunctionalInterface
    public static interface SupplierWithException<T, E extends Throwable>
      {
        public T get()
                throws E;
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
     * @deprecated Use {@link #runWithContexts(Runnable, Object...)} or {@link #runWithContexts(Supplier, Object...)}
     *
     ******************************************************************************************************************/
    @Deprecated
    public default <V, T extends Throwable> V runWithContext (@Nonnull final Object context,
                                                              @Nonnull final Task<V, T> task)
      throws T
      {
        return runWithContexts(Collections.singletonList(context), task);
      }

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
     * @deprecated Use {@link #runWithContexts(Runnable, Object...)} or {@link #runWithContexts(Supplier, Object...)}
     *
     ******************************************************************************************************************/
    @Deprecated
    public default <V, T extends Throwable> V runWithContexts (@Nonnull final List<Object> contexts,
                                                               @Nonnull final Task<V, T> task)
      throws T
      {
        return runEWithContexts(task::run, contexts.toArray());
      }

    /*******************************************************************************************************************
     *
     * Runs a task associated with a new local context. This variant fits functional interfaces.
     *
     * @param  <V>                the type of the returned value of the task
     * @param  context            the context
     * @param  task               the task
     * @return                    the value produced by the task
     * @deprecated Use {@link #runWithContexts(Runnable, Object...)} or {@link #runWithContexts(Supplier, Object...)}
     *
     ******************************************************************************************************************/
    @Deprecated
    public default <V> V runWithContext (@Nonnull final Object context, @Nonnull final Supplier<V> task)
      {
        return runWithContexts(task, context);
      }

    /*******************************************************************************************************************
     *
     * Runs a task associated with a new bunch of local contexts. This variant fits functional interfaces.
     *
     * @param  <V>                the type of the returned value
     * @param  contexts           the contexts
     * @param  task               the task
     * @return                    the value produced by the task
     * @deprecated Use {@link #runWithContexts(Runnable, Object...)} or {@link #runWithContexts(Supplier, Object...)}
     *
     ******************************************************************************************************************/
    @Deprecated
    public default <V> V runWithContexts (@Nonnull final List<Object> contexts, @Nonnull final Supplier<V> task)
      {
        return runWithContexts(task, contexts.toArray());
      }

    /*******************************************************************************************************************
     *
     * Calls a runnable with some local contexts. This method fits functional interfaces.
     *
     * @param   runnable          the runnable
     * @param   contexts          the contexts
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    public default void runWithContexts (@Nonnull final Runnable runnable, @Nonnull final Object ... contexts)
      {
        final SupplierWithException<Void, RuntimeException> se = () ->{ runnable.run(); return null; };
        runEWithContexts(se, contexts);
      }

    /*******************************************************************************************************************
     *
     * Calls a supplier with some local contexts. This method fits functional interfaces.
     *
     * @param   <T>               the type of the result
     * @param   supplier          the supplier
     * @param   contexts          the contexts
     * @return                    the value returned by the supplier
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default <T> T runWithContexts (@Nonnull final Supplier<T> supplier, @Nonnull final Object ... contexts)
      {
        final SupplierWithException<T, RuntimeException> se = supplier::get;
        return runEWithContexts(se, contexts);
      }

    /*******************************************************************************************************************
     *
     * Calls a runnable with some local contexts. This method fits functional interfaces.
     *
     * @param   <E>               the type of the thrown exception
     * @param   runnable          the runnable to call
     * @param   contexts          the contexts
     * @throws  E                 the original exception thrown by task
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    public default <E extends Throwable> void runEWithContexts (@Nonnull final RunnableWithException<E> runnable,
                                                                @Nonnull final Object ... contexts)
      throws E
      {
        final SupplierWithException<Void, E> se = () ->{ runnable.run(); return null; };
        runEWithContexts(se, contexts);
      }

    /*******************************************************************************************************************
     *
     * Calls a task with some local contexts. This method fits functional interfaces.
     *
     * @param   <T>               the type of the returned value
     * @param   <E>               the type of the thrown exception
     * @param   task              the task to call
     * @param   contexts          the contexts
     * @return                    the value returned by the supplier
     * @throws  E                 the original exception thrown by task
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T, E extends Throwable> T runEWithContexts (@Nonnull SupplierWithException<T, E> task,
                                                        @Nonnull Object ... contexts)
      throws E;

    /*******************************************************************************************************************
     *
     * Creates a binder that makes it possible to bind a local context by means of a try-with-resources instead of a
     * try/finally.
     *
     * <pre>
     * try (final ContextManager.Binder binder = contextManager.binder(context))
     *   {
     *     ...
     *   }
     * </pre>
     *
     * @param   contexts          the contexts
     * @return                    a binder that can be used in try-with-resources
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    @Nonnull
    public default Binder binder (@Nonnull final Object ... contexts)
      {
        return new Binder(this, contexts);
      }

    /*******************************************************************************************************************
     *
     * Used by
     * @since   3.2-ALPHA-12
     *
     ******************************************************************************************************************/
    public static class Binder implements AutoCloseable
      {
        @Nonnull
        private final ContextManager contextManager;

        @Nonnull
        private final Object[] contexts;

        private Binder (@Nonnull final ContextManager contextManager, @Nonnull final Object[] contexts)
          {
            this.contextManager = contextManager;
            this.contexts = contexts;

            for (final Object context : contexts)
              {
                this.contextManager.addLocalContext(context);
              }
          }

        @Override
        public void close()
          {
            for (final Object context : contexts)
              {
                this.contextManager.removeLocalContext(context);
              }
          }
      }
  }
