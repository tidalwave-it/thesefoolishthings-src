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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * A collections of utility methods for simplifying the syntax of lambda expressions with APIs that don't accept
 * checked exceptions (such as {@link java.util.stream.Stream}): they provide wrapped functions that have no checked
 * exception in the signature and whose implementation delegates to the original function wrapping an eventual checked
 * exception into a {@link RuntimeException}. For instance, given the following method that could not be used as a
 * {@link java.util.stream.Stream#filter(Predicate)} argument:
 *
 * <pre>
 *   private boolean matchEven (final int number)
 *           throws Exception
 *     {
 *       if (number == 13)
 *         {
 *           throw new Exception("13!");
 *         }
 *
 *       return number % 2 == 0;
 *     }
 * </pre>
 *
 * working code can be written as:
 *
 * <pre>
 *   try
 *     {
 *       List&lt;Integer&gt; numbers = IntStream.rangeClosed(1, 20)
 *                                        .mapToObj(Integer::valueOf)
 *                                        .filter(_p(this::matchEven)) // note the wrapper here
 *                                        .collect(Collectors.toList());
 *       ...
 *     }
 *   catch (RuntimeException e)
 *     {
 *       ...
 *     }
 * </pre>
 *
 * Any checked exception is wrapped by a {@code RuntimeException}, but {@link IOException} is wrapped by a
 * {@link UncheckedIOException}.
 *
 * @author Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 * @since 3.2-ALPHA-1
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = PRIVATE)
public final class FunctionalCheckedExceptionWrappers
  {
    /*******************************************************************************************************************
     *
     * A variant of {@link Function} that might throw an {@link Exception}. This interface must not be directly used,
     * it's defined to let the compiler infer functional equivalence.
     *
     * @param <T>         the type of the function argument
     * @param <R>         the type of the function return value
     *
     ******************************************************************************************************************/
    @FunctionalInterface
    public static interface FunctionWithException<T, R>
      {
        public R apply (T t)
                throws Exception;
      }

    /*******************************************************************************************************************
     *
     * A variant of {@link Consumer} that might throw an {@link Exception}. This interface must not be directly used,
     * it's defined to let the compiler infer functional equivalence.
     *
     * @param <T>         the type of the {@code Consumer} argument
     *
     ******************************************************************************************************************/
    @FunctionalInterface
    public static interface ConsumerWithException<T>
      {
        public void accept (T t)
                throws Exception;
      }

    /*******************************************************************************************************************
     *
     * A variant of {@link Supplier} that might throw an {@link Exception}. This interface must not be directly used,
     * it's defined to let the compiler infer functional equivalence.
     *
     * @param <T>         the type of the {@code Supplier} argument
     *
     ******************************************************************************************************************/
    @FunctionalInterface
    public static interface SupplierWithException<T>
      {
        public T get ()
                throws Exception;
      }

    /*******************************************************************************************************************
     *
     * A variant of {@link Predicate} that might throw an {@link Exception}. This interface must not be directly used,
     * it's defined to let the compiler infer functional equivalence.
     *
     * @param <T>         the type of the {@code Predicate} argument
     *
     ******************************************************************************************************************/
    @FunctionalInterface
    public static interface PredicateWithException<T>
      {
        public boolean test (T t)
                throws Exception;
      }

    /*******************************************************************************************************************
     *
     * A wrapper for a {@link Function} that catches exceptions and wraps them into {@link RuntimeException}s.
     *
     * @param function    the {@code Function} to wrap.
     * @param <T>         the type of the function argument
     * @param <R>         the type of the function return value
     * @return            the wrapped {@code Function}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T, R> Function<T, R> _f (@Nonnull final FunctionWithException<T, R> function)
      {
        return t ->
          {
            try
              {
                return function.apply(t);
              }
            catch (Exception e)
              {
                throw wrappedException(e);
              }
          };
      }

    /*******************************************************************************************************************
     *
     * A wrapper for a {@link Consumer} that catches exceptions and wraps them into {@link RuntimeException}s.
     *
     * @param consumer    the {@code Consumer} to wrap.
     * @param <T>         the type of the {@code Consumer} argument
     * @return            the wrapped {@code Consumer}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Consumer<T> _c (@Nonnull final ConsumerWithException<T> consumer)
      {
        return t ->
          {
            try
              {
                consumer.accept(t);
              }
            catch (Exception e)
              {
                throw wrappedException(e);
              }
          };
      }

    /*******************************************************************************************************************
     *
     * A wrapper for a {@link Supplier} that catches exceptions and wraps them into {@link RuntimeException}s.
     *
     * @param supplier    the {@code Supplier} to wrap.
     * @param <T>         the type of the {@code Supplier} argument
     * @return            åthe wrapped {@code Supplier}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Supplier<T> _s (@Nonnull final SupplierWithException<T> supplier)
      {
        return () ->
          {
            try
              {
                return supplier.get();
              }
            catch (Exception e)
              {
                throw wrappedException(e);
              }
          };
      }

    /*******************************************************************************************************************
     *
     * A wrapper for a {@link Predicate} that catches exceptions and wraps them into {@link RuntimeException}s.
     *
     * @param predicate   the {@code Predicate} to wrap.
     * @param <T>         the type of the {@code Predicate} argument
     * @return            åthe wrapped {@code Predicate}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Predicate<T> _p (@Nonnull final PredicateWithException<T> predicate)
      {
        return t ->
          {
            try
              {
                return predicate.test(t);
              }
            catch (Exception e)
              {
                throw wrappedException(e);
              }
          };
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    static RuntimeException wrappedException (@Nonnull final Exception e)
      {
        if (e instanceof RuntimeException)
          {
            return (RuntimeException)e;
          }

        if (e instanceof IOException)
          {
            return new UncheckedIOException((IOException)e);
          }

        return new RuntimeException(e);
      }
  }
