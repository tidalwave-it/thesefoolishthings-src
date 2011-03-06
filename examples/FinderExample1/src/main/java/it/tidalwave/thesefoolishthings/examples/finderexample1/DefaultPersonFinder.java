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
    private List<Person> persons;
    
    private String firstName = ".*";
    
    private String lastName = ".*";
    
    private List<PersonSortCriterion> sortCriteria = new ArrayList<PersonSortCriterion>();
    
    public DefaultPersonFinder (final @Nonnull List<Person> persons) 
      {
        super("DefaultPersonFinder");
        this.persons = persons;
      }
    
    public DefaultPersonFinder() // for clone()
      {
      }

    @Override @Nonnull
    protected DefaultPersonFinder clone() 
      {
        final DefaultPersonFinder clone = (DefaultPersonFinder)super.clone();
        clone.persons      = this.persons;
        clone.firstName    = this.firstName;
        clone.lastName     = this.lastName;
        clone.sortCriteria = new ArrayList<PersonSortCriterion>(this.sortCriteria);
        return clone;
      }
    
    @Override @Nonnull
    public PersonFinder withFirstName (final @Nonnull String firstName) 
      {
        final DefaultPersonFinder clone = clone();
        clone.firstName = firstName;
        return clone;
      }

    @Override @Nonnull
    public PersonFinder withLastName (final @Nonnull String lastName) 
      {
        final DefaultPersonFinder clone = clone();
        clone.lastName = lastName;
        return clone;
      }

    @Override @Nonnull
    public PersonFinder sort (final @Nonnull SortCriterion criterion, final @Nonnull SortDirection direction) 
      {
        final DefaultPersonFinder clone = clone();
        clone.sortCriteria.add((PersonSortCriterion)criterion);
        return clone;
      }
    
    @Override @Nonnull
    protected List<? extends Person> computeResults() 
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
