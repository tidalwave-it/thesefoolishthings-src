/***********************************************************************************************************************
 *
 * blueBill Mobile - Android - open source birding
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://bluebill.tidalwave.it/mobile
 * SCM: https://java.net/hg/bluebill-mobile~android-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.test;

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
    private static final Logger log = LoggerFactory.getLogger(TestLogger.class);
    
    private static final String SEPARATOR = "************************************************************************************************";
    
    @Override
    public void onTestStart (final @Nonnull ITestResult result) 
      {
        final String separator = SEPARATOR.substring(0, Math.min(result.getName().length() + 7, SEPARATOR.length()));
        log.info("");
        log.info(separator);
        log.info("TEST \"{}\"", result.getName().replace('_', ' '));
        log.info(separator);
      }

    @Override
    public void onTestFailure (final @Nonnull ITestResult result) 
      {
        log.info("TEST FAILED");
      }

    @Override
    public void onTestSkipped (final @Nonnull ITestResult result) 
      {
        log.info("TEST SKIPPED");
      }

    @Override
    public void onTestSuccess (final @Nonnull ITestResult result)
      {
        log.info("TEST PASSED");
      }
  } 
