/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/

package it.tidalwave.thesefoolishthings.examples.dci.swing;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface PersonPresentationControl 
  {
    public static final Class<PersonPresentationControl> PersonPresentationController = PersonPresentationControl.class;

    public void okButtonPressed();
  }
