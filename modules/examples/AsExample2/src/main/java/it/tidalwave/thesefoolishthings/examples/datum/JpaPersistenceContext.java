/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.datum;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JpaPersistenceContext 
  {
//    @PersistenceContext
//    private EntityManager em;
    
    public void persist (final @Nonnull Object object)
      {
          System.err.println("PERSIST " + object);
//        em.persist(object);
      }
    
    public void remove (final @Nonnull Object object)
      {
          System.err.println("REMOVE " + object);
//        em.remove(object);
      }
  }
