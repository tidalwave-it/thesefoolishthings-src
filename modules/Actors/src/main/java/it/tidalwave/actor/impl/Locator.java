/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.actor.impl;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import org.openide.util.Lookup;

/***********************************************************************************************************************
 *
 * A trimmed down replacement for OpenBlueSky Locator, in order to avoid depending on OpenBlueSky.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class Locator 
  {
    public static class NotFoundException extends RuntimeException
      {
        public NotFoundException (final @Nonnull Class<?> serviceClass)
          {
            super("Not found: " + serviceClass);
          }

        public NotFoundException (final @Nonnull String name)
          {
            super("Not found: " + name);
          }
      }

    @Nonnull
    public static <T> Provider<T> createProviderFor (final @Nonnull Class<T> serviceClass)
      {
        return new Provider<T>()
          {
            @Override @Nonnull
            public T get()
              {
                return find(serviceClass);
              }
          };
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private static <T> T find (final @Nonnull Class<T> serviceClass)
      {
        final T service = Lookup.getDefault().lookup(serviceClass);

        if (service == null)
          {
            throw new NotFoundException(serviceClass);
          }

        return service;
      }
  }
