/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import static it.tidalwave.util.FunctionalCheckedExceptionWrappers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@SuppressWarnings({"SameReturnValue", "RedundantThrows"}) @Slf4j
public class FunctionalCheckedExceptionWrappersTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_function_wrapper()
      {
        final var r = _f(this::sampleFunction).apply("good");
        assertThat(r, is("good"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = RuntimeException.class)
    public void test_function_wrapper_with_exception()
      {
        final var r = _f(this::sampleFunctionWithException).apply("bad");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_consumer_wrapper()
      {
        _c(this::sampleConsumer).accept("good");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = RuntimeException.class)
    public void test_consumer_wrapper_with_exception()
      {
        _c(this::sampleConsumerWithException).accept("foo");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_supplier_wrapper()
      {
        final var r = _s(this::sampleSupplier).get();
        assertThat(r, is("foo"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = RuntimeException.class)
    public void test_supplier_wrapper_with_exception()
      {
        final var r = _s(this::sampleSupplierWithException).get();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void test_predicate_wrapper()
      {
        final var r = _p(this::samplePredicate).test("foo");
        assertThat(r, is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = RuntimeException.class)
    public void test_predicate_wrapper_with_exception()
      {
        final var r = _p(this::samplePredicateWithException).test("foo");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test // this is just to see a code example with Streams
    public void test_with_Stream()
      {
        final var numbers = IntStream.rangeClosed(1, 10)
                                     .boxed()
                                     .filter(_p(this::matchEven))
                                     .collect(Collectors.toList());
        log.info("Even numbers: {}", numbers);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(expectedExceptions = RuntimeException.class) // this is just to see a code example with Streams
    public void test_with_Stream_with_exception()
      {
        final var numbers = IntStream.rangeClosed(1, 20)
                                     .boxed()
                                     .filter(_p(this::matchEven))
                                     .collect(Collectors.toList());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_not_wrap_RuntimeException()
      {
        final var e = new RuntimeException();
        final var we = wrappedException(e);
        assertThat(we, is(sameInstance(e)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_wrap_checked_exceptions()
      {
        final var e = new InterruptedException();
        final var we = wrappedException(e);
        assertThat(we.getCause(), is(sameInstance(e)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_wrap_IOException_with_UncheckedIOException()
      {
        final var e = new IOException();
        final var we = wrappedException(e);
        assertThat(we.getCause(), is(sameInstance(e)));
        assertThat(we, is(instanceOf(UncheckedIOException.class)));
      }

    /*******************************************************************************************************************
     *
     * These can be static, but in this way :: operators above look shorted.
     *
     ******************************************************************************************************************/
    @Nonnull
    private String sampleFunction (@Nonnull final String arg)
            throws Exception
      {
        return arg;
      }

    @Nonnull
    private String sampleFunctionWithException (@Nonnull final String arg)
            throws Exception
      {
        throw new Exception();
      }

    @SuppressWarnings("EmptyMethod")
    private void sampleConsumer (@Nonnull final String arg)
            throws Exception
      {
      }

    private void sampleConsumerWithException (@Nonnull final String arg)
            throws Exception
      {
        throw new Exception();
      }

    @Nonnull
    private String sampleSupplier()
            throws Exception
      {
        return "foo";
      }

    @Nonnull
    private String sampleSupplierWithException()
            throws Exception
      {
        throw new Exception();
      }

    private boolean samplePredicate (@Nonnull final String arg)
            throws Exception
      {
        return true;
      }

    private boolean samplePredicateWithException (@Nonnull final String arg)
            throws Exception
      {
        throw new Exception();
      }

    private boolean matchEven (final int number)
            throws Exception
      {
        if (number == 13)
          {
            throw new Exception("13!");
          }

        return number % 2 == 0;
      }
  }
