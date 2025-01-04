/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util.test;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.nio.charset.StandardCharsets.UTF_8;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static lombok.AccessLevel.PRIVATE;

/***************************************************************************************************************************************************************
 *
 * A facility that provides some common tasks for testing, such as manipulating test files.
 *
 * @author  Fabrizio Giudici
 * @since 3.2-ALPHA-18
 *
 **************************************************************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class BaseTestHelper
  {
    @Nonnull
    protected final Object test;

    /***********************************************************************************************************************************************************
     * Returns a {@link Path} for a resource file. The resource should be placed under
     * {@code src/test/resources/test-class-simple-name/test-resources/resource-name}. Note that the file actually
     * loaded is the one under {@code target/test-classes} copied there (and eventually filtered) by Maven.
     *
     * @param   resourceName    the resource name
     * @return the {@code Path}
     **********************************************************************************************************************************************************/
    @Nonnull
    public Path resourceFileFor (@Nonnull final String resourceName)
      {
        final var testName = test.getClass().getSimpleName();
        return Paths.get("target/test-classes", testName, "test-resources", resourceName);
      }

    /***********************************************************************************************************************************************************
     * Reads the content from the resource file as a single string. See {@link #resourceFileFor(String)} for
     * further info.
     *
     * @param   resourceName    the resource name
     * @return the string
     * @throws IOException     in case of error
     **********************************************************************************************************************************************************/
    @Nonnull
    public String readStringFromResource (@Nonnull final String resourceName)
            throws IOException
      {
        final var file = resourceFileFor(resourceName);
        final var buffer = new StringBuilder();
        var separator = "";

        for (final var string : Files.readAllLines(file, UTF_8))
          {
            buffer.append(separator).append(string);
            separator = "\n";
          }

        return buffer.toString();
//            return String.join("\n", Files.readAllLines(path, UTF_8)); TODO JDK 8
      }

    /***********************************************************************************************************************************************************
     * Create a {@link TestResource} for the given name. The actual file will be created under
     * {@code target/test-artifacts/test-class-simple-name/resourceName}. The expected file should be
     * placed in {@code src/test/resources/test-class-simple-name/expected-results/resource-name}. Note that the file
     * actually loaded is the one under {@code target/test-classes} copied there (and eventually filtered) by Maven.
     * The {@code test-class-simple-name} is tried first with the current test, and then with its eventual
     * super-classes; this allows to extend existing test suites. Note that if the resource files for a super class are
     * not in the current project module, they should be explicitly copied here (for instance, by means of the
     * Maven dependency plugin).
     *
     * @param   resourceName    the name
     * @return the {@code TestResource}
     * @throws IOException     in case of error
     **********************************************************************************************************************************************************/
    @Nonnull
    public TestResource testResourceFor (@Nonnull final String resourceName)
            throws IOException
      {
        final var testName = test.getClass().getSimpleName();
        final var expectedFile = findExpectedFilePath(resourceName);
        final var actualFile = Paths.get("target/test-artifacts", testName, resourceName);
        Files.createDirectories(actualFile.getParent());
        return new TestResource(resourceName, actualFile, expectedFile);
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Nonnull
    private Path findExpectedFilePath (@Nonnull final String resourceName)
            throws IOException
      {
        for (var testClass = test.getClass(); testClass != null; testClass = testClass.getSuperclass())
          {
            final var expectedFile =
                    Paths.get("target/test-classes", testClass.getSimpleName(), "expected-results", resourceName);

            if (Files.exists(expectedFile))
              {
                return expectedFile;
              }
          }

        throw new FileNotFoundException("Expected file for test " + resourceName);
      }

    /***********************************************************************************************************************************************************
     * A manipulator of a pair of (actual file, expected file).
     **********************************************************************************************************************************************************/
    @RequiredArgsConstructor(access = PRIVATE)
    public final class TestResource
      {
        @Nonnull
        private final String name;

        @Nonnull
        private final Path actualFile;

        @Nonnull
        private final Path expectedFile;

        /***************************************************************************************************************
         *
         * Assert that the content of the actual file are the same as the expected file.
         *
         * @throws IOException     in case of error
         *
         **************************************************************************************************************/
        public void assertActualFileContentSameAsExpected ()
                throws IOException
          {
            assertSameContents(expectedFile.toFile(), actualFile.toFile());
          }

        /***************************************************************************************************************
         *
         * Writes the given strings to the actual file.
         *
         * @param   strings         the strings
         * @throws IOException     in case of error
         *
         **************************************************************************************************************/
        public void writeToActualFile (@Nonnull final String... strings)
                throws IOException
          {
            writeToActualFile(List.of(strings));
          }

        /***************************************************************************************************************
         *
         * Writes the given strings to the actual file.
         *
         * @param   strings         the strings
         * @throws IOException     in case of error
         *
         **************************************************************************************************************/
        public void writeToActualFile (@Nonnull final Iterable<String> strings)
                throws IOException
          {
            Files.write(actualFile, strings, UTF_8);
          }

        /***************************************************************************************************************
         *
         * Writes the given bytes to the actual file.
         *
         * @param   bytes           the bytes
         * @throws IOException     in case of error
         *
         **************************************************************************************************************/
        public void writeToActualFile (@Nonnull final byte[] bytes)
                throws IOException
          {
            Files.write(actualFile, bytes);
          }

        /***************************************************************************************************************
         *
         * Reads the content from the resource file as a single string.
         *
         * @return the string
         * @throws IOException     in case of error
         *
         **************************************************************************************************************/
        @Nonnull
        public String readStringFromResource ()
                throws IOException
          {
            return BaseTestHelper.this.readStringFromResource(name);
          }
      }
  }
