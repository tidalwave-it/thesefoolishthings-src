/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import lombok.Delegate;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor
public class ListOfPersons implements List<Person>
  {
    @Delegate
    private final List<Person> persons = new ArrayList<Person>();
    
    public ListOfPersons (final @Nonnull List<Person> persons) 
      {
        this.persons.addAll(persons);
      }
  }
