/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-util
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * A factory class for localized {@link DateTimeFormatter}s in various flavours, specified by the {@link FormatStyle}.
 * This class is especially useful for migration to JDK 9+, where the default behaviour of
 * {@code DateTimeFormatter.ofLocalizedDate/DateTime(...)} has changed.
 *
 * At the moment only locales for English and Italian are supported.
 *
 * @since   3.1-ALPHA-4
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocalizedDateTimeFormatters
  {
    /*******************************************************************************************************************
     *
     * Returns a formatter.
     *
     * @param   style   the style
     * @param   locale  the locale
     * @return          the formatter
     *
     ******************************************************************************************************************/
    @Nonnull
    public static DateTimeFormatter getDateTimeFormatterFor (@Nonnull final FormatStyle style,
                                                             @Nonnull final Locale locale)
      {
        final String resourceName = "dateTimeFormatterPattern." + style.name();
        final String pattern = BundleUtilities.getMessage(LocalizedDateTimeFormatters.class, locale, resourceName);
        return DateTimeFormatter.ofPattern(pattern).withLocale(locale);
      }
  }
