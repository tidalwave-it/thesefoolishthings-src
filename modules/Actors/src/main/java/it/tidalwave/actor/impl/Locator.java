/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * *********************************************************************************************************************
 * 
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
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
        NotFoundException (final @Nonnull Class<?> serviceClass)
          {
            super("Not found: " + serviceClass);
          }

        NotFoundException (final @Nonnull String name)
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
