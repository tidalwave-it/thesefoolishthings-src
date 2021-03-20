/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import it.tidalwave.util.Id;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = PRIVATE)
public final class Utils
  {
    public static void populatePresidents (@Nonnull final PersonRegistry registry)
      {
        registry.add(new Person(new Id("1"), "Richard", "Nixon"));
        registry.add(new Person(new Id("2"), "Jimmy", "Carter"));
        registry.add(new Person(new Id("3"), "Ronald", "Reagan"));
        registry.add(new Person(new Id("4"), "George", "Bush"));
        registry.add(new Person(new Id("5"), "Bill", "Clinton"));
        registry.add(new Person(new Id("6"), "George Walker", "Bush"));
        registry.add(new Person(new Id("7"), "Barack", "Obama"));
      }
  }
