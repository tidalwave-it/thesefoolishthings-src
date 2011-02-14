/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.util.test;

import java.io.InputStream;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import it.tidalwave.util.logging.Logger;
import org.incava.util.diff.Diff;
import org.incava.util.diff.Difference;
import static org.junit.Assert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: $
 *
 **********************************************************************************************************************/
public final class FileComparisonUtils
  {
    private static final String CLASS = FileComparisonUtils.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS);

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static void assertSameContents (final @Nonnull File expectedFile, final @Nonnull File actualFile)
      throws IOException
      {
        logger.info("******** Comparing files:"); 
        logger.info(">>>> exp is: %s", expectedFile.getAbsolutePath());
        logger.info(">>>> act is: %s", actualFile.getAbsolutePath());
        assertSameContents(fileToStrings(expectedFile), fileToStrings(actualFile));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static void assertSameContents (final @Nonnull List<String> expected, final @Nonnull List<String> actual)
      {
        final Diff diff = new Diff(expected, actual);
        final List<Difference> differences = diff.diff();

        if (!differences.isEmpty())
          {
            final StringBuilder buffer = new StringBuilder();

            for (final Difference difference : differences)
              {
                int addedStart = difference.getAddedStart();
                int addedEnd = difference.getAddedEnd();
                int deletedStart = difference.getDeletedStart();
                int deletedEnd = difference.getDeletedEnd();

                if (addedStart >= 0)
                  {
                    addedEnd = (addedEnd >= 0) ? addedEnd : actual.size() - 1;

                    for (int i = addedStart; i <= addedEnd; i++)
                      {
                        buffer.append(String.format("-act: %3d: *%s*\n", i + 1, actual.get(i)));
                      }
                  }

                if (deletedStart >= 0)
                  {
                    deletedEnd = (deletedEnd >= 0) ? deletedEnd : expected.size() - 1;

                    for (int i = deletedStart; i <= deletedEnd; i++)
                      {
                        buffer.append(String.format("+exp: %3d: *%s*\n", i + 1, expected.get(i)));
                      }
                  }
              }

            logger.info("%s", buffer);
            fail("Unexpected contents:\n" + buffer);
          }
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> stringToStrings (final @Nonnull String string)
      throws IOException
      {
        //return Arrays.asList(string.split("\n"));
        return fileToStrings(new ByteArrayInputStream(string.getBytes("UTF-8")));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (final @Nonnull File file)
      throws IOException
      {
        return fileToStrings(new FileInputStream(file));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (final @Nonnull String path)
      throws IOException
      {
        final InputStream is = FileComparisonUtils.class.getClassLoader().getResourceAsStream(path);

        if (is == null)
          {
            throw new RuntimeException("Resource not found: " + path);
          }

        return fileToStrings(is);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (final @Nonnull InputStream is)
      throws IOException
      {
        final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final List<String> result = new ArrayList<String>();

        for (;;)
          {
            final String s = br.readLine();

            if (s == null)
              {
                break;
              }

            result.add(s);
          }

        br.close();

        return result;
      }
  }
