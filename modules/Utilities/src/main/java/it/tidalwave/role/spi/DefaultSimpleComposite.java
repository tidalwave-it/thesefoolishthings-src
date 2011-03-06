/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.util.Finder;

/**
 *
 * @author fritz
 */
public class DefaultSimpleComposite<Type> implements SimpleComposite<Type> 
  {
    @Nonnull
    private final Finder<Type> finder;

    public DefaultSimpleComposite (final @Nonnull Finder<Type> finder) 
      {
        this.finder = finder;
      }
    
    @Nonnull
    public final Finder<Type> findChildren() 
      {
        return finder;
      }
  }
