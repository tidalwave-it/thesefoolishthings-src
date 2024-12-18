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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;
import java.util.Locale;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class LocalizedDateTimeFormattersTest
  {
    @Test(dataProvider = "dates")
    public void must_properly_format_date_and_time (@Nonnull final ZonedDateTime dt,
                                                    @Nonnull final Locale locale,
                                                    @Nonnull final FormatStyle style,
                                                    @Nonnull final String expectedValue)
      {
        // when
        final var actualValue = LocalizedDateTimeFormatters.getDateTimeFormatterFor(style, locale).format(dt);
        // then
        // JDK 8 formats AM/PM, JDK 9+ formats am/pm ...
        assertThat(actualValue.replace(" am", " AM").replace(" pm", " PM"), is(expectedValue));
      }

    @DataProvider(name = "dates")
    private static Object[][] dates()
      {
        final var dt = Instant.ofEpochMilli(1344353463985L).atZone(ZoneId.of("GMT"));

        return new Object[][]
          {
            { dt, Locale.UK,    FormatStyle.SHORT,  "8/7/12 3:31 PM"},
            { dt, Locale.ITALY, FormatStyle.SHORT,  "07/08/12 15:31"},

            { dt, Locale.UK,    FormatStyle.MEDIUM, "Aug 7, 2012 3:31 PM"},
            { dt, Locale.ITALY, FormatStyle.MEDIUM, "7-ago-2012 15:31"},

            { dt, Locale.UK,    FormatStyle.LONG,   "August 7, 2012 3:31:03 PM"},
            { dt, Locale.ITALY, FormatStyle.LONG,   "7 agosto 2012 15:31:03"},

            { dt, Locale.UK,    FormatStyle.FULL,   "Tuesday, August 7, 2012 3:31:03 PM GMT"},
            { dt, Locale.ITALY, FormatStyle.FULL,   "martedì 7 agosto 2012 15:31:03 GMT"},
          };
      }
  }
