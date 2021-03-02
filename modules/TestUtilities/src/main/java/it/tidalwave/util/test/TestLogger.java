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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.test;

import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class TestLogger extends TestListenerAdapter
  {
    private static final String S = "***********************************************";
    private static final String SEPARATOR = S + S + S + S;

    @Override
    public void onStart (final @Nonnull ITestContext testContext)
      {
        super.onStart(testContext);
        final String testClass = testContext.getCurrentXmlTest().getClasses().get(0).getName();
        final Logger log = LoggerFactory.getLogger(testClass);
        log.info("STARTING TESTS OF {}", testClass);
      }

    @Override
    public void onFinish (final @Nonnull ITestContext testContext)
      {
        super.onFinish(testContext);
        final String testClass = testContext.getCurrentXmlTest().getClasses().get(0).getName();
        final Logger log = LoggerFactory.getLogger(testClass);
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
    public void onConfigurationSkip (final @Nonnull ITestResult result)
      {
        super.onConfigurationSkip(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final Throwable throwable = result.getThrowable();
        log.info("CONFIGURATION SKIPPED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.info("CONFIGURATION SKIPPED", result.getThrowable());
          }
      }

    @Override
    public void onConfigurationFailure (final @Nonnull ITestResult result)
      {
        super.onConfigurationFailure(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final Throwable throwable = result.getThrowable();
        log.info("CONFIGURATION FAILED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.info("CONFIGURATION FAILED", result.getThrowable());
          }
      }

    @Override
    public void onTestStart (final @Nonnull ITestResult result)
      {
        super.onTestStart(result);
        String args = "";

        final Object[] parameters = result.getParameters();

        if ((parameters != null) && parameters.length > 0)
          {
            args = Arrays.toString(parameters);
          }

        final int max = Math.max(args.length() + 5, result.getName().length() + 7);
        final String separator = SEPARATOR.substring(0, Math.min(max, SEPARATOR.length()));
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());

        log.info(separator);
        log.info("TEST \"{}\"", result.getName().replace('_', ' '));

        if (!"".equals(args))
          {
            log.info("ARGS {}", args);
          }

        log.info(separator);
      }

    @Override
    public void onTestFailure (final @Nonnull ITestResult result)
      {
        super.onTestFailure(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final Throwable throwable = result.getThrowable();
        log.info("TEST FAILED in {} msec - {}", result.getEndMillis() - result.getStartMillis(),
                                                getMessage(throwable));
        if (throwable != null)
          {
            log.info("TEST FAILED", result.getThrowable());
          }

        log.info("");
      }

    @Override
    public void onTestFailedButWithinSuccessPercentage (final @Nonnull ITestResult result)
      {
        super.onTestFailedButWithinSuccessPercentage(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST FAILED WITHIN SUCCESS PERCENTAGE in {} msec", result.getEndMillis() - result.getStartMillis());
        log.info("");
      }

    @Override
    public void onTestSkipped (final @Nonnull ITestResult result)
      {
        super.onTestSkipped(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        final Throwable throwable = result.getThrowable();
        log.info("TEST SKIPPED {}", getMessage(throwable));

        if (throwable != null)
          {
            log.info("TEST SKIPPED", result.getThrowable());
          }

        log.info("");
      }

    @Override
    public void onTestSuccess (final @Nonnull ITestResult result)
      {
        super.onTestSuccess(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST PASSED in {} msec", result.getEndMillis() - result.getStartMillis());
        log.info("");
      }

    @Nonnull
    private static String getMessage (final @Nullable Throwable throwable)
      {
        return (throwable == null) ? "" : throwable.toString().replaceAll("\n*", "");
      }
  }
