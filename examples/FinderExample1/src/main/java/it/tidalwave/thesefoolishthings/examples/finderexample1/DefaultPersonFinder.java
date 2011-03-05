/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.finderexample1;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import it.tidalwave.util.spi.FinderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultPersonFinder extends FinderSupport<Person, PersonFinder> implements PersonFinder
  {
    private final List<Person> persons;
    
    private String firstName = ".*";
    
    private String lastName = ".*";
    
    private List<PersonSortCriterion> sortCriteria = new ArrayList<PersonSortCriterion>();
    
    public DefaultPersonFinder (final List<Person> persons) 
      {
        super("DefaultPersonFinder");
        this.persons = persons;
      }

    @Nonnull
    public PersonFinder withFirstName (final @Nonnull String firstName) 
      {
        this.firstName = firstName;
        return this;
      }

    @Nonnull
    public PersonFinder withLastName (final @Nonnull String lastName) 
      {
        this.lastName = lastName;
        return this;
      }

    @Override
    public PersonFinder sort (SortCriterion criterion, SortDirection direction) 
      {
        sortCriteria.add((PersonSortCriterion)criterion);
        return this;
      }
    
    @Override @Nonnull
    protected List<? extends Person> doCompute() 
      {
        final List<Person> result = new ArrayList<Person>();
        final Pattern firstNameRegEx = Pattern.compile(firstName);
        final Pattern lastNameRegEx = Pattern.compile(lastName);

        for (final Person person : persons)
          {
            if (firstNameRegEx.matcher(person.getFirstName()).matches() 
                && lastNameRegEx.matcher(person.getLastName()).matches())
              {
                result.add(person);  
              }
          }

        for (final PersonSortCriterion sortCriterion : sortCriteria)
          {
            sortCriterion.sort(result);                        
          }

        return result;
      }
  }
