/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static void assertSameContents (final @Nonnull File expectedFile, final @Nonnull File actualFile)
      throws IOException
      {
        System.err.println("******** Comparing files");
        System.err.printf(">>>> exp is: %s\n", expectedFile.getAbsolutePath());
        System.err.printf(">>>> act is: %s\n", actualFile.getAbsolutePath());
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

            System.err.println(buffer);
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
