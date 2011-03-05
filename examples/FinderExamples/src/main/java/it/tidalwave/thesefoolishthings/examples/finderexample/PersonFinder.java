/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import it.tidalwave.util.spi.SpecializedFinderSupport;
import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public interface PersonFinder extends SpecializedFinderSupport<Person, PersonFinder> 
  {
    @Nonnull
    public PersonFinder withFirstName (@Nonnull String firstName);
    
    @Nonnull
    public PersonFinder withLastName (@Nonnull String firstName);
  }
