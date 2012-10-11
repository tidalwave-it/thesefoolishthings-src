/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.ServiceLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface AsDelegateProvider 
  {
    public static final Class<AsDelegateProvider> AsDelegateProvider = AsDelegateProvider.class;
    
    @NoArgsConstructor(access=AccessLevel.PRIVATE)
    public static final class Locator
      {
        private static AsDelegateProvider asSpiProvider;
        
        @Nonnull
        public static synchronized AsDelegateProvider find()
          {
            if (asSpiProvider == null)
              {
                final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                final Iterator<AsDelegateProvider> i = ServiceLoader.load(AsDelegateProvider.class, classLoader).iterator();

                if (!i.hasNext())
                  {
                    throw new RuntimeException("No ServiceProvider for AsDelegateProvider");  
                  }

                asSpiProvider = i.next();
              }
            
            return asSpiProvider;
          }
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public AsDelegate createAsDelegate (@Nonnull Object datum);
  }
