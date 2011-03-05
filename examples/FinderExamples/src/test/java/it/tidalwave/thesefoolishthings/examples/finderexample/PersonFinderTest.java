/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.thesefoolishthings.examples.finderexample;

import it.tidalwave.util.Finder.SortCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
/**
 *
 * @author fritz
 */
public class PersonFinderTest 
  {
    private PersonFinder fixture;
    
    @Before
    public void setupFixture() 
      {
        fixture = new PersonRegistry().findPersons();
      }

    @Test
    public void test1()
      {
        assertThat(fixture.results().toString(),
                   is("[Richard Nixon, Jimmy Carter, Ronald Reagan, George Bush, "
                    + "Bill Clinton, George Walker Bush, Barack Obama]"));
      }
    
    @Test
    public void test2()
      {
        assertThat(fixture.withFirstName("B.*").results().toString(),
                   is("[Bill Clinton, Barack Obama]"));
      }
    
    @Test
    public void test2a()
      {
        assertThat(fixture.withFirstName("B.*").sort(PersonSortCriterion.BY_FIRST_NAME).results().toString(),
                   is("[Barack Obama, Bill Clinton]"));
      }
    
    @Test
    public void test3()
      {
        assertThat(fixture.withLastName("Bush").results().toString(),
                   is("[George Bush, George Walker Bush]"));
      }
  }