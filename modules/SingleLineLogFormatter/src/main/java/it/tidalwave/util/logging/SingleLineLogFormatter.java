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
package it.tidalwave.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class SingleLineLogFormatter extends Formatter
 {
   //%d{} [%-16t] %-5p %-21c{1} - %m%n
   private static final String PADDER = "                                                                          ";
   
   private Date now = new Date();
   
   private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

   // Line separator string.  This is the value of the line.separator
   // property at the moment that the SimpleFormatter was created.
   private String lineSeparator = "\n";
//       (String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

   private static String padded (String s, int width)
     {
       s = (s == null) ? "" : s;
       int i = s.length() - Math.abs(width);

       if (i < 0)
         {
           i = 0;
         }

       return (width < 0) ? (s + PADDER).substring(0, -width) : (s + PADDER).substring(i, i + width);
     }

   /**
    * Format the given LogRecord.
    * @param record the log record to be formatted.
    * @return a formatted log record
    */
   public synchronized String format (LogRecord record)
     {
       final StringBuilder buffer1 = new StringBuilder();
       // Minimize memory allocations here.
       now.setTime(record.getMillis());
       buffer1.append(dateFormat.format(now));
       buffer1.append(" ");
       buffer1.append("[");
       buffer1.append(padded(Thread.currentThread().getName(), 18));
       buffer1.append("] ");
       buffer1.append(padded(record.getLevel().getName(), 7));
       buffer1.append(" ");
       buffer1.append(padded(record.getLoggerName(), 32));
       buffer1.append(" - ");

       if (true)
         {
           final int depth = Thread.currentThread().getStackTrace().length;

           for (int i = 0; i < depth; i++)
             {
               buffer1.append(" ");
             }
         }

       final String prefix = buffer1.toString();

       final StringBuilder buffer2 = new StringBuilder();
       String message = formatMessage(record);
       buffer2.append(message);
       buffer2.append(lineSeparator);

       if (record.getThrown() != null)
         {
           try
             {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               record.getThrown().printStackTrace(pw);
               pw.close();
               buffer2.append(sw.toString());
             }
           catch (Exception e)
             {
             }
         }

       final StringBuilder buffer3 = new StringBuilder();

       for (final String part : buffer2.toString().split("\n"))
         {
           buffer3.append(prefix);
           buffer3.append(part);
           buffer3.append("\n");
         }

       return buffer3.toString();
     }
   }
