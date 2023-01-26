/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.role.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.util.ShortNames.shortName;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor @Getter @EqualsAndHashCode
public class OwnerAndRole
  {
    @Nonnull
    private final Class<?> ownerClass;

    @Nonnull
    private final Class<?> roleClass;

    /***************************************************************************************************************
     *
     * Returns pairs (class, role) for each class or interface up in the hierarchy.
     *
     **************************************************************************************************************/
    @Nonnull
    public List<OwnerAndRole> getSuper()
      {
        final List<OwnerAndRole> result = new ArrayList<>();
        result.add(this);

        if (ownerClass.getSuperclass() != null)
          {
            result.addAll(new OwnerAndRole(ownerClass.getSuperclass(), roleClass).getSuper());
          }

        for (final var interfaceClass : ownerClass.getInterfaces())
          {
            result.addAll(new OwnerAndRole(interfaceClass, roleClass).getSuper());
          }

        return result;
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("(%s, %s)", shortName(ownerClass), shortName(roleClass));
      }
  }