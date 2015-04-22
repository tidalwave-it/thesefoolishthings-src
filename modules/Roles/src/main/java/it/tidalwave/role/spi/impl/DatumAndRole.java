/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spi.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Getter @ToString @EqualsAndHashCode @Slf4j
public class DatumAndRole
  {
    @Nonnull
    private final Class<?> datumClass;

    @Nonnull
    private final Class<?> roleClass;

    /***************************************************************************************************************
     *
     * Returns pairs (class, role) for each class or interface up in the hierarchy.
     *
     **************************************************************************************************************/
    @Nonnull
    public List<DatumAndRole> getSuper()
      {
        final List<DatumAndRole> result = new ArrayList<>();
        result.add(this);

        if (datumClass.getSuperclass() != null)
          {
            result.addAll(new DatumAndRole(datumClass.getSuperclass(), roleClass).getSuper());
          }

        for (final Class<?> interfaceClass : datumClass.getInterfaces())
          {
            result.addAll(new DatumAndRole(interfaceClass, roleClass).getSuper());
          }

        return result;
      }
  }