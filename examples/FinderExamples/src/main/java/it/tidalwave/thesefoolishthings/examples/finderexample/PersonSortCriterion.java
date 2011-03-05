/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import it.tidalwave.util.DefaultFilterSortCriterion;
import it.tidalwave.util.Finder.SortCriterion;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public enum PersonSortCriterion implements SortCriterion 
  {
    BY_FIRST_NAME
      {
        protected Comparator<Person> getComparator()
          {
            return new Comparator<Person>() 
              {
                public int compare (final @Nonnull Person p1, final @Nonnull Person p2) 
                  {
                    return p1.getFirstName().compareTo(p2.getFirstName());
                  }
              };  
          }
      },
    
    BY_LAST_NAME
      {
        protected Comparator<Person> getComparator()
          {
            return new Comparator<Person>() 
              {
                public int compare (final @Nonnull Person p1, final @Nonnull Person p2) 
                  {
                    return p1.getLastName().compareTo(p2.getLastName());
                  }
              };  
          }
      };
    
    protected void sort (List<? extends Person> persons)
      {
        Collections.sort(persons, getComparator());
      }
    
    protected abstract Comparator<Person> getComparator();
  }
