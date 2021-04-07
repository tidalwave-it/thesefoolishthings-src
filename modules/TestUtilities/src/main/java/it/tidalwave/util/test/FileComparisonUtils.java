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
package it.tidalwave.util.test;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import it.tidalwave.util.Pair;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.text.DiffRowGenerator;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.UtilityClass;
import static java.util.stream.Collectors.toList;
import static java.nio.charset.StandardCharsets.*;
import static org.junit.Assert.*;

/***********************************************************************************************************************
 *
 * A utility class to compare two text files and assert that they have the same contents.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@UtilityClass @Slf4j
public class FileComparisonUtils
  {
    public static final String P_TABULAR_OUTPUT = FileComparisonUtils.class.getName() + ".tabularOutput";
    public static final String P_TABULAR_LIMIT = FileComparisonUtils.class.getName() + ".tabularLimit";

    private static final boolean TABULAR_OUTPUT = Boolean.getBoolean(P_TABULAR_OUTPUT);
    private static final int TABULAR_LIMIT = Integer.getInteger(P_TABULAR_LIMIT, 500);

    /*******************************************************************************************************************
     *
     * Asserts that two files have the same contents.
     *
     * @param   expectedFile    the file with the expected contents
     * @param   actualFile      the file with the contents to probe
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    public static void assertSameContents (@Nonnull final Path expectedFile, @Nonnull final Path actualFile)
      throws IOException
      {
        assertSameContents(expectedFile.toFile(), actualFile.toFile());
      }

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
        log.info("******** Comparing files:");
        logPaths(expectedPath, actualPath, "");
        assertSameContents(fileToStrings(expectedFile), fileToStrings(actualFile), expectedPath, actualPath);
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
        assertSameContents(expected, actual, null, null);
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
     * Reads a classpath resource into a list of strings.
     *
     * @param   path            the path of the classpath resource
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
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(is, UTF_8)))
          {
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

            return result;
          }
      }

    /*******************************************************************************************************************
     *
     * Given a string that represents a path whose segments are separated by the standard separator of the platform,
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

    /*******************************************************************************************************************
     *
     * Asserts that two collections of strings have the same contents.
     *
     * @param   expected        the expected values
     * @param   actual          the actual values
     * @param   expectedPath    an optional path for expected values
     * @param   actualPath      an optional path for actual values
     *
     ******************************************************************************************************************/
    private static void assertSameContents (@Nonnull final List<String> expected,
                                            @Nonnull final List<String> actual,
                                            @Nullable final String expectedPath,
                                            @Nullable final String actualPath)
      {
        final List<AbstractDelta<String>> deltas = DiffUtils.diff(expected, actual).getDeltas();

        if (!deltas.isEmpty())
          {
            if ((expectedPath != null) && (actualPath != null))
              {
                logPaths(expectedPath, actualPath, "TEST FAILED ");
              }

            deltas.forEach(delta ->
              {
                final String actMiss = String.format("TEST FAILED -act[%d]", delta.getSource().getPosition());
                final String actNExp = String.format("TEST FAILED +act[%d]", delta.getTarget().getPosition());
                final String expLine = String.format("TEST FAILED  exp[%d]", delta.getSource().getPosition());
                final String actLine = String.format("TEST FAILED  act[%d]", delta.getTarget().getPosition());

                switch (delta.getType())
                  {
                    case CHANGE:
                      delta.getSource().getLines().forEach(line -> log.error("{} *{}*", expLine, line));
                      delta.getTarget().getLines().forEach(line -> log.error("{} *{}*", actLine, line));
                      break;

                    case DELETE:
                      delta.getSource().getLines().forEach(line -> log.error("{} *{}*", actMiss, line));
                      break;

                    case INSERT:
                      delta.getTarget().getLines().forEach(line -> log.error("{} *{}*", actNExp, line));
                      break;
                  }
              });

            if (!TABULAR_OUTPUT)
              {
                log.error("TEST FAILED You can set -D{}=true for extra tabular output; -D{}=<num> to set max table size",
                         P_TABULAR_OUTPUT, P_TABULAR_LIMIT);
              }
            else
              {
                final DiffRowGenerator generator = DiffRowGenerator.create()
                        .showInlineDiffs(false)
                        .inlineDiffByWord(true)
                        .lineNormalizer(l -> l)
                        .build();
                final List<Pair<String, String>> pairs = generator.generateDiffRows(expected, actual)
                        .stream()
                        .filter(row -> !row.getNewLine().equals(row.getOldLine()))
                        .map(row -> Pair.of(row.getOldLine().trim(), row.getNewLine().trim()))
                        .limit(TABULAR_LIMIT)
                        .collect(toList());

                final int padA = pairs.stream().mapToInt(p -> p.a.length()).max().getAsInt();
                final int padB = pairs.stream().mapToInt(p -> p.b.length()).max().getAsInt();
                log.error("TEST FAILED Tabular text is trimmed; row limit set to -D{}={}",
                          P_TABULAR_LIMIT, TABULAR_LIMIT);
                log.error("TEST FAILED |-{}-+-{}-|", pad("--------", padA, '-'), pad("--------", padB, '-'));
                log.error("TEST FAILED | {} | {} |", pad("expected", padA, ' '), pad("actual  ", padB, ' '));
                log.error("TEST FAILED |-{}-+-{}-|", pad("--------", padA, '-'), pad("--------", padB, '-'));
                pairs.forEach(p -> log.error("TEST FAILED | {} | {} |", pad(p.a, padA, ' '), pad(p.b, padB,' ')));
                log.error("TEST FAILED |-{}-+-{}-|", pad("--------", padA, '-'), pad("--------", padB, '-'));
              }

            fail("Unexpected contents: see log above (you can grep 'TEST FAILED')");
          }
      }

    /*******************************************************************************************************************
     *
     * Logs info about file comparison paths.
     *
     * @param expectedPath      the expected path
     * @param actualPath        the actual path
     * @param prefix            a log prefix
     *
     ******************************************************************************************************************/
    private static void logPaths (@Nonnull final String expectedPath,
                                  @Nonnull final String actualPath,
                                  @Nonnull final String prefix)
      {
        final String commonPath = commonPrefix(expectedPath, actualPath);
        log.info("{}>>>> path is: {}", prefix, commonPath);
        log.info("{}>>>> exp is:  {}", prefix, expectedPath.substring(commonPath.length()));
        log.info("{}>>>> act is:  {}", prefix, actualPath.substring(commonPath.length()));
      }

    /*******************************************************************************************************************
     *
     * Pads a string to left to fit the given width.
     *
     * @param   string    the string
     * @param   width     the width
     * @return            the padded string
     *
     ******************************************************************************************************************/
    @Nonnull
    private static String pad (@Nonnull final String string, @Nonnegative final int width, final char padding)
      {
        return String.format("%-" + width + "s", string).replace(' ', padding);
      }
  }
