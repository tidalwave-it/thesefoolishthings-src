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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.text.DiffRowGenerator;
import it.tidalwave.util.Pair;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.UtilityClass;
import static java.util.stream.Collectors.toList;
import static java.nio.charset.StandardCharsets.*;
import static it.tidalwave.util.Pair.indexedPairStream;
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
    private static final String P_BASE_NAME = FileComparisonUtils.class.getName();

    public static final String P_TABULAR_OUTPUT = P_BASE_NAME + ".tabularOutput";
    public static final String P_TABULAR_LIMIT = P_BASE_NAME + ".tabularLimit";

    private static final boolean TABULAR_OUTPUT = Boolean.getBoolean(P_TABULAR_OUTPUT);
    private static final int TABULAR_LIMIT = Integer.getInteger(P_TABULAR_LIMIT, 500);
    private static final String TF = "TEST FAILED";

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
        assertSameContents(expectedFile.toPath(), actualFile.toPath());
      }

    /*******************************************************************************************************************
     *
     * Asserts that two files have the same contents.
     *
     * @param   expectedPath    the file with the expected contents
     * @param   actualPath      the file with the contents to probe
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    public static void assertSameContents (@Nonnull final Path expectedPath, @Nonnull final Path actualPath)
      throws IOException
      {
        log.info("******** Comparing files:");
        logPaths(expectedPath, actualPath, "");
        assertSameContents(fileToStrings(expectedPath), fileToStrings(actualPath), expectedPath, actualPath);
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
     * Checks whether two files have the same contents.
     *
     * @param   expectedPath    the file with the expected contents
     * @param   actualPath      the file with the contents to probe
     * @throws  IOException     in case of error
     * @since   1.2-ALPHA-15
     *
     ******************************************************************************************************************/
    public static boolean checkSameContents (@Nonnull final Path expectedPath, @Nonnull final Path actualPath)
            throws IOException
      {
        return checkSameContents(fileToStrings(expectedPath), fileToStrings(actualPath), expectedPath, actualPath)
                .isEmpty();
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
        //return List.of(string.split("\n"));
        return resourceToStrings(new ByteArrayInputStream(string.getBytes(UTF_8)));
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
    public static List<String> fileToStrings (@Nonnull final Path file)
      throws IOException
      {
        return Files.readAllLines(file);
      }

    /*******************************************************************************************************************
     *
     * Reads a classpath resource (not a regular file) into a list of strings.
     *
     * @param   path            the path of the classpath resource
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> resourceToStrings (@Nonnull final String path)
      throws IOException
      {
        final var is = FileComparisonUtils.class.getClassLoader().getResourceAsStream(path);

        if (is == null)
          {
            throw new RuntimeException("Resource not found: " + path);
          }

        return resourceToStrings(is);
      }

    /*******************************************************************************************************************
     *
     * Reads an input stream into a list of strings. The stream is closed at the end.
     *
     * @param   is              the input stream
     * @return                  the strings
     * @throws  IOException     in case of error
     *
     ******************************************************************************************************************/
    @Nonnull
    public static List<String> resourceToStrings (@Nonnull final InputStream is)
      throws IOException
      {
        try (final var br = new BufferedReader(new InputStreamReader(is, UTF_8)))
          {
            final var result = new ArrayList<String>();

            for (;;)
              {
                final var s = br.readLine();

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
        final var min = Math.min(s1.length(), s2.length());
        var latestSeenSlash = 0;

        for (var i = 0; i < min; i++)
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
                                            @Nullable final Path expectedPath,
                                            @Nullable final Path actualPath)
      {
        var diff = checkSameContents(expected, actual, expectedPath, actualPath);

        if (!diff.isEmpty())
          {
            fail(String.join(System.lineSeparator(), diff));
          }
      }

    /*******************************************************************************************************************
     *
     * Checks whether two collections of strings have the same contents.
     *
     * @param   expected        the expected values
     * @param   actual          the actual values
     * @param   expectedPath    an optional path for expected values
     * @param   actualPath      an optional path for actual values
     * @return                  the differences
     *
     ******************************************************************************************************************/
    private static List<String> checkSameContents (@Nonnull final List<String> expected,
                                                   @Nonnull final List<String> actual,
                                                   @Nullable final Path expectedPath,
                                                   @Nullable final Path actualPath)
      {
        final var deltas = DiffUtils.diff(expected, actual).getDeltas();

        if (deltas.isEmpty())
          {
            return List.of();
          }

        if ((expectedPath != null) && (actualPath != null))
          {
            logPaths(expectedPath, actualPath, "TEST FAILED ");
          }

        final var strings = toStrings(deltas);
        strings.forEach(log::error);

        if (!TABULAR_OUTPUT)
          {
            log.error("{} You can set -D{}=true for tabular output; -D{}=<num> to set max table size",
                      TF, P_TABULAR_OUTPUT, P_TABULAR_LIMIT);
          }
        else
          {
            final var generator = DiffRowGenerator.create()
                                                  .showInlineDiffs(false)
                                                  .inlineDiffByWord(true)
                                                  .lineNormalizer(l -> l)
                                                  .build();
            final var pairs = generator.generateDiffRows(expected, actual)
                                       .stream()
                                       .filter(row -> !row.getNewLine().equals(row.getOldLine()))
                                       .map(row -> Pair.of(row.getOldLine().trim(), row.getNewLine().trim()))
                                       .limit(TABULAR_LIMIT)
                                       .collect(toList());

            final var padA = pairs.stream().mapToInt(p -> p.a.length()).max().getAsInt();
            final var padB = pairs.stream().mapToInt(p -> p.b.length()).max().getAsInt();
            log.error("{} Tabular text is trimmed; row limit set to -D{}={}", TF, P_TABULAR_LIMIT, TABULAR_LIMIT);
            log.error("{} |-{}-+-{}-|", TF, pad("--------", padA, '-'), pad("--------", padB, '-'));
            log.error("{} | {} | {} |", TF, pad("expected", padA, ' '), pad("actual  ", padB, ' '));
            log.error("{} |-{}-+-{}-|", TF, pad("--------", padA, '-'), pad("--------", padB, '-'));
            pairs.forEach(p -> log.error("{} | {} | {} |", TF, pad(p.a, padA, ' '), pad(p.b, padB,' ')));
            log.error("{} |-{}-+-{}-|", TF, pad("--------", padA, '-'), pad("--------", padB, '-'));
          }

        strings.add(0, "Unexpected contents: see log above (you can grep '" + TF + "')");
        return strings;
      }

    /*******************************************************************************************************************
     *
     * Converts deltas to output as a list of strings.
     *
     * @param   deltas  the deltas
     * @return          the strings
     *
     ******************************************************************************************************************/
    @Nonnull
    private static List<String> toStrings (@Nonnull final Iterable<? extends AbstractDelta<String>> deltas)
      {
        final List<String> strings = new ArrayList<>();

        deltas.forEach(delta ->
          {
            final var sourceLines = delta.getSource().getLines();
            final var targetLines = delta.getTarget().getLines();
            final var sourcePosition = delta.getSource().getPosition() + 1;
            final var targetPosition = delta.getTarget().getPosition() + 1;

            switch (delta.getType())
              {
                case CHANGE:
                  indexedPairStream(sourceLines).forEach(p -> strings.add(
                          String.format("%s  exp[%d] *%s*", TF, sourcePosition + p.a, p.b)));
                  indexedPairStream(targetLines).forEach(p -> strings.add(
                          String.format("%s  act[%d] *%s*", TF, targetPosition + p.a, p.b)));
                  break;

                case DELETE:
                  indexedPairStream(sourceLines).forEach(p -> strings.add(
                          String.format("%s -act[%d] *%s*", TF, sourcePosition + p.a, p.b)));
                  break;

                case INSERT:
                  indexedPairStream(targetLines).forEach(p -> strings.add(
                          String.format("%s +act[%d] *%s*", TF, targetPosition + p.a, p.b)));
                  break;

                default:
              }
          });

        return strings;
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
    private static void logPaths (@Nonnull final Path expectedPath,
                                  @Nonnull final Path actualPath,
                                  @Nonnull final String prefix)
      {
        final var expectedPathAsString = expectedPath.toAbsolutePath().toString();
        final var actualPathAsString = expectedPath.toAbsolutePath().toString();
        final var commonPath = commonPrefix(expectedPathAsString, actualPathAsString);
        log.info("{}>>>> path is: {}", prefix, commonPath);
        log.info("{}>>>> exp is:  {}", prefix, expectedPathAsString.substring(commonPath.length()));
        log.info("{}>>>> act is:  {}", prefix, actualPathAsString.substring(commonPath.length()));
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
