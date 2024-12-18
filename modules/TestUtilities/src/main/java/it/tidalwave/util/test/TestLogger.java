/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class TestLogger extends TestListenerAdapter
  {
    private static final String S = "***********************************************";
    private static final String SEPARATOR = S + S + S + S;

    @Override
    public void onStart (@Nonnull final ITestContext testContext)
      {
        super.onStart(testContext);
        final var testClass = testContext.getCurrentXmlTest().getClasses().get(0).getName();
        final var log = LoggerFactory.getLogger(testClass);
        log.info("STARTING TESTS OF {}", testClass);
      }

    @Override
    public void onFinish (@Nonnull final ITestContext testContext)
      {
        super.onFinish(testContext);
        final var testClass = testContext.getCurrentXmlTest().getClasses().get(0).getName();
        final var log = LoggerFactory.getLogger(testClass);
        log.info("FINISHED TESTS OF {}", testClass);
      }

//    @Override
//    public void onConfigurationSuccess (final @Nonnull ITestResult result)
//      {
//        super.onConfigurationSuccess(result);
//        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
//        log.info("====== ON CONFIG SUCCESS");
//      }

    @Override
    public void onConfigurationSkip (@Nonnull final ITestResult result)
      {
        super.onConfigurationSkip(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final var throwable = result.getThrowable();
        log.warn("CONFIGURATION SKIPPED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.warn("CONFIGURATION SKIPPED", result.getThrowable());
          }
      }

    @Override
    public void onConfigurationFailure (@Nonnull final ITestResult result)
      {
        super.onConfigurationFailure(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final var throwable = result.getThrowable();
        log.error("CONFIGURATION FAILED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.error("CONFIGURATION FAILED", result.getThrowable());
          }
      }

    @Override
    public void onTestStart (@Nonnull final ITestResult result)
      {
        super.onTestStart(result);
        final var args = getArgs(result);
        final var max = Math.max(args.length() + 5, result.getName().length() + 7);
        final var separator = SEPARATOR.substring(0, Math.min(max, SEPARATOR.length()));
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());

        log.info(separator);
        log.info("TEST \"{}\"", result.getName().replace('_', ' '));

        if (!args.isEmpty())
          {
            log.info("ARGS {}", args);
          }

        log.info(separator);
      }

    @Override
    public void onTestFailure (@Nonnull final ITestResult result)
      {
        super.onTestFailure(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final var throwable = result.getThrowable();
        final var args = getArgs(result);

        log.error("TEST FAILED in {} msec - {}{} - {}",
                 result.getEndMillis() - result.getStartMillis(),
                 result.getName().replace('_', ' '),
                 args.isEmpty() ? "" : " " + args,
                 getMessage(throwable));

        if (throwable != null)
          {
            log.error("TEST FAILED", result.getThrowable());
          }

        log.error("");
      }

    @Override
    public void onTestFailedButWithinSuccessPercentage (@Nonnull final ITestResult result)
      {
        super.onTestFailedButWithinSuccessPercentage(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST FAILED WITHIN SUCCESS PERCENTAGE in {} msec", result.getEndMillis() - result.getStartMillis());
        log.info("");
      }

    @Override
    public void onTestSkipped (@Nonnull final ITestResult result)
      {
        super.onTestSkipped(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final var throwable = result.getThrowable();
        log.info("TEST SKIPPED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.info("TEST SKIPPED", result.getThrowable());
          }

        log.info("");
      }

    @Override
    public void onTestSuccess (@Nonnull final ITestResult result)
      {
        super.onTestSuccess(result);
        final var log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST PASSED in {} msec", result.getEndMillis() - result.getStartMillis());
        log.info("");
      }

    @Nonnull
    private String getArgs (@Nonnull final ITestResult result)
      {
        var args = "";

        final var parameters = result.getParameters();

        if ((parameters != null) && parameters.length > 0)
          {
            args = Arrays.toString(parameters);
          }

        return args;
      }

    @Nonnull
    private static String getMessage (@Nullable final Throwable throwable)
      {
        return (throwable == null) ? "" : throwable.toString();
//        return (throwable == null) ? "" : throwable.toString().replaceAll("\n*", "");
      }
  }
