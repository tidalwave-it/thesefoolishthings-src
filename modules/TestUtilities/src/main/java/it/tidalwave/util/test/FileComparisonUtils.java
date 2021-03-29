/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-util-test
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
import org.incava.util.diff.Diff;
import org.incava.util.diff.Difference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import static java.nio.charset.StandardCharsets.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public final class FileComparisonUtils
  {
    private static final Logger log = LoggerFactory.getLogger(FileComparisonUtils.class);

    /*******************************************************************************************************************
     *
     * Asserts that two files have the same contents.
     *
     * @param   expectedFile    the file with the expected contents
     * @param   actualFile      the file with the contents to probe
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    public static void assertSameContents (@Nonnull final File expectedFile, @Nonnull final File actualFile)
      throws IOException
      {
        final String expectedPath = expectedFile.getAbsolutePath();
        final String actualPath = actualFile.getAbsolutePath();
        final String commonPath = commonPrefix(expectedPath, actualPath);
        log.info("******** Comparing files:");
        log.info(">>>> path is: {}", commonPath);
        log.info(">>>> exp is:  {}", expectedPath.substring(commonPath.length()));
        log.info(">>>> act is:  {}", actualPath.substring(commonPath.length()));
        assertSameContents(fileToStrings(expectedFile), fileToStrings(actualFile));
      }

    /*******************************************************************************************************************
     *
     * Asserts that two collections of strings have the same contents.
     *
     * @param   expected        the expected values
     * @param   actual          the actual values
     *
     ******************************************************************************************************************/
    public static void assertSameContents (@Nonnull final List<String> expected, @Nonnull final List<String> actual)
      {
        final Diff diff = new Diff(expected, actual);
        final List<Difference> differences = diff.diff();

        if (!differences.isEmpty())
          {
            final StringBuilder buffer = new StringBuilder();

            for (final Difference difference : differences)
              {
                final int addedStart = difference.getAddedStart();
                int addedEnd = difference.getAddedEnd();
                final int deletedStart = difference.getDeletedStart();
                int deletedEnd = difference.getDeletedEnd();

                if (addedStart >= 0)
                  {
                    addedEnd = (addedEnd >= 0) ? addedEnd : actual.size() - 1;

                    for (int i = addedStart; i <= addedEnd; i++)
                      {
                        buffer.append(String.format("-act: %3d: *%s*%n", i + 1, actual.get(i)));
                      }
                  }

                if (deletedStart >= 0)
                  {
                    deletedEnd = (deletedEnd >= 0) ? deletedEnd : expected.size() - 1;

                    for (int i = deletedStart; i <= deletedEnd; i++)
                      {
                        buffer.append(String.format("+exp: %3d: *%s*%n", i + 1, expected.get(i)));
                      }
                  }
              }

            log.info("{}", buffer);
            fail("Unexpected contents:\n" + buffer);
          }
      }

    /*******************************************************************************************************************
     *
     * Converts a string which contains newlines into a list of strings.
     *
     * @param   string          the source
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> stringToStrings (@Nonnull final String string)
      throws IOException
      {
        //return Arrays.asList(string.split("\n"));
        return fileToStrings(new ByteArrayInputStream(string.getBytes(UTF_8)));
      }

    /*******************************************************************************************************************
     *
     * Reads a file into a list of strings.
     *
     * @param   file            the file
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (@Nonnull final File file)
      throws IOException
      {
        return fileToStrings(new FileInputStream(file));
      }

    /*******************************************************************************************************************
     *
     * Reads a file into a list of strings.
     *
     * @param   path            the path of the file
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (@Nonnull final String path)
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
     * Reads an input stream into a list of strings.
     *
     * @param   is              the input stream
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> fileToStrings (@Nonnull final InputStream is)
      throws IOException
      {
        final BufferedReader br = new BufferedReader(new InputStreamReader(is, UTF_8));
        final List<String> result = new ArrayList<>();

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

    /*******************************************************************************************************************
     *
     * Given a string that represent a path whose segments are separated by the standard separator of the platform,
     * returns the common prefix - which means the common directory parents.
     *
     * @param   s1    the former string
     * @param   s2    the latter string
     * @return        the common prefix
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String commonPrefix (@Nonnull final String s1, @Nonnull final String s2)
      {
        final int min = Math.min(s1.length(), s2.length());
        int latestSeenSlash = 0;

        for (int i = 0; i < min; i++)
          {
            if (s1.charAt(i) != s2.charAt(i))
              {
                return (i == 0) ? "" : s1.substring(0, Math.min(latestSeenSlash + 1, min));
              }
            else
              {
                if (s1.charAt(i) == File.separatorChar)
                  {
                    latestSeenSlash = i;
                  }
              }
          }

        return s1.substring(0, min);
      }
  }
