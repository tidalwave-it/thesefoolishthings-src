/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.finderexample1;

import javax.annotation.Nonnull;
import java.util.Comparator;
import it.tidalwave.util.Finder.SortCriterion;
import it.tidalwave.util.DefaultFilterSortCriterion;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public final class PersonSortCriterion
  {
    public final static SortCriterion BY_FIRST_NAME = new DefaultFilterSortCriterion<Person>(
        new Comparator<Person>()
          {
            public int compare (final @Nonnull Person p1, final @Nonnull Person p2)
              {
                return p1.getFirstName().compareTo(p2.getFirstName());
              }
          }, "BY_FIRST_NAME");

    public final static SortCriterion BY_LAST_NAME = new DefaultFilterSortCriterion<Person>(
        new Comparator<Person>()
          {
            public int compare (final @Nonnull Person p1, final @Nonnull Person p2)
              {
                return p1.getLastName().compareTo(p2.getLastName());
              }
          }, "BY_LAST_NAME");
  }
