/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public class Person 
  {
    private final String firstName;

    private final String lastName;

    public Person(String firstName, String lastName) 
      {
        this.firstName = firstName;
        this.lastName = lastName;
      }
        
    public String getFirstName() 
      {
        return firstName;
      }

    public String getLastName() 
      {
        return lastName;
      }
    
    @Override @Nonnull
    public String toString()
      {
        return String.format("%s %s", firstName, lastName);
      }
  }
