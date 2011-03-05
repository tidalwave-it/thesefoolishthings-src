/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public class PersonRegistry 
  {
    private final List<Person> persons = new ArrayList<Person>();

    public PersonRegistry() 
      {
        persons.add(new Person("Richard", "Nixon"));
        persons.add(new Person("Jimmy", "Carter"));
        persons.add(new Person("Ronald", "Reagan"));
        persons.add(new Person("George", "Bush"));
        persons.add(new Person("Bill", "Clinton"));
        persons.add(new Person("George Walker", "Bush"));
        persons.add(new Person("Barack", "Obama"));
      }
    
    @Nonnull
    public PersonFinder findPersons()
      {
        return new DefaultPersonFinder()
          {    
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
          };
      }
  }
