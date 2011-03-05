/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import it.tidalwave.util.Finder.SortCriterion;

/**
 *
 * @author fritz
 */
public enum PersonSortCriterion implements SortCriterion 
  {
    BY_FIRST_NAME(new Comparator<Person>() 
      {
        public int compare (final @Nonnull Person p1, final @Nonnull Person p2) 
          {
            return p1.getFirstName().compareTo(p2.getFirstName());
          }
      }),
    
    BY_LAST_NAME(new Comparator<Person>() 
      {
        public int compare (final @Nonnull Person p1, final @Nonnull Person p2) 
          {
            return p1.getLastName().compareTo(p2.getLastName());
          }
      });  
    
    private final Comparator<Person> comparator;
    
    protected void sort (final @Nonnull List<? extends Person> persons)
      {
        Collections.sort(persons, comparator);
      }
    
    private PersonSortCriterion (final @Nonnull Comparator<Person> comparator)
      {
        this.comparator = comparator;  
      }
  }
