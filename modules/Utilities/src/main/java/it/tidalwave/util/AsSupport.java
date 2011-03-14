/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @it.tidalwave.javadoc.draft
 *
 **********************************************************************************************************************/
public class AsSupport implements As, Serializable, Lookup.Provider
  {
    private static final long serialVersionUID = 34534568437593487L;

    protected final List<Object> capabilities;

    @CheckForNull
    protected transient Lookup lookup;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    protected AsSupport (final @Nonnull Object ... capabilities)
      {
        this.capabilities = new ArrayList<Object>(Arrays.asList(capabilities));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public final <T> T as (final @Nonnull Class<T> type)
      {
        return as(type, As.Defaults.throwAsException(type));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public <T> T as (final @Nonnull Class<T> type, final @Nonnull NotFoundBehaviour<T> nfb)
      {
        for (final Object capability : capabilities)
          {
            if (isInstanceOf(capability.getClass(), type))
              {
                return (T)capability;
              }
          }

        return nfb.run(new NotFoundException(type.getName()));
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public synchronized Lookup getLookup()
      {
        if (lookup == null)
          {
            lookup = Lookups.fixed(capabilities.toArray()); // toArray() otherwise puts the list instance
          }

        return lookup;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String toString()
      {
        return String.format("%s@%x[%s]", getClass().getSimpleName(), System.identityHashCode(this), capabilities);
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private boolean isInstanceOf (final @Nonnull Class<?> subClass, final @Nonnull Class<?> superClass)
      {
        if (superClass.isAssignableFrom(subClass))
          {
            return true;
          }

        for (final Class<?> interfaceClass : subClass.getInterfaces())
          {
            if (isInstanceOf(interfaceClass, superClass))
              {
                return true;
              }
          }

        return false;
      }
  }
