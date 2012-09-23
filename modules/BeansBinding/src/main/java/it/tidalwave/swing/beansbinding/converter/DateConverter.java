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
package it.tidalwave.swing.beansbinding.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author fritz
 */
public class DateConverter extends Converter<Date, String>
  {
    private static final String DEFAULT_PATTERN = "yyyy/MM/dd";
    
    private String pattern = DEFAULT_PATTERN;

    private Locale locale = Locale.getDefault();

    private SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);

    public String getPattern()
      {
        return pattern;
      }

    public void setPattern (final String pattern)
      {
        this.pattern = pattern;
        dateFormat = new SimpleDateFormat(pattern, locale);
        // TODO: fire event
      }

    public TimeZone getTimeZone()
      {
        return dateFormat.getTimeZone();
      }

    public void setTimeZone (final TimeZone timeZone)
      {
        dateFormat.setTimeZone(timeZone);
      }

    public Locale getLocale()
      {
        return locale;
      }

    public void setLocale (final Locale locale)
      {
        this.locale = locale;
        dateFormat = new SimpleDateFormat(pattern, locale);
        // TODO: fire event
      }

    @Override
    public String convertForward (final Date date)
      {
        return (date != null) ? dateFormat.format(date) : "";
      }

    @Override
    public Date convertReverse (final String string)
      {
        try 
          {
            return dateFormat.parse(string);
          } 
        catch (ParseException ex)
          {
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
          }
      }
  }
