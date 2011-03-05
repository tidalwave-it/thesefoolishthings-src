/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.FinderSupport;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public abstract class DefaultPersonFinder extends FinderSupport<Person, PersonFinder> implements PersonFinder
  {
    protected String firstName = ".*";
    protected String lastName = ".*";
    protected List<PersonSortCriterion> sortCriteria = new ArrayList<PersonSortCriterion>();
    
    public DefaultPersonFinder() 
      {
        super("DefaultPersonFinder");
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
  }
