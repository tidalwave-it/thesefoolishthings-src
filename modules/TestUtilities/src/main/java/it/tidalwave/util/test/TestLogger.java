/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.test;

import java.util.Arrays;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void onTestStart (final @Nonnull ITestResult result) 
      {
        String args = "";
        
        final Object[] parameters = result.getParameters();
        
        if ((parameters != null) && parameters.length > 0)
          {
            args = Arrays.toString(parameters);
          }
        
        final String separator = SEPARATOR.substring(0, Math.min(Math.max(args.length() + 5, result.getName().length() + 7), SEPARATOR.length()));
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
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST FAILED in {} msec - {}", result.getEndMillis() - result.getStartMillis(), result.getThrowable());
        log.info("");
      }

    @Override
    public void onTestSkipped (final @Nonnull ITestResult result) 
      {
        onTestStart(result);
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST SKIPPED");
        log.info("");
      }

    @Override
    public void onTestSuccess (final @Nonnull ITestResult result)
      {
        final Logger log = LoggerFactory.getLogger(result.getTestClass().getRealClass());
        log.info("TEST PASSED in {} msec", result.getEndMillis() - result.getStartMillis());
        log.info("");
      }
  } 
