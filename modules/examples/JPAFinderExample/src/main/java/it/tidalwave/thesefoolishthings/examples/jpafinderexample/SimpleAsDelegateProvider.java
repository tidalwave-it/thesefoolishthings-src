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
package it.tidalwave.thesefoolishthings.examples.jpafinderexample;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.role.Removable;
import it.tidalwave.role.io.Persistable;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl.PersonJpaPersistable;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.role.Findable;
import it.tidalwave.thesefoolishthings.examples.person.Person;

/***********************************************************************************************************************
 *
 * This example doesn't use Spring integration, so we provide a simple quick-n-dirty {@code AsDelegateProvider} that
 * finds the unique role available. This class is instantiated by {@code META-INF/services}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class SimpleAsDelegateProvider implements AsDelegateProvider
  {
    private static final List<Class<?>> ROLES = Arrays.asList(Persistable.class, Removable.class, Findable.class);

    @Override @Nonnull
    public AsDelegate createAsDelegate (@Nonnull final Object datum)
      {
        return new AsDelegate()
          {
            @Nonnull @Override
            public <T> Collection<? extends T> as (@Nonnull final Class<T> roleType)
              {
                return ((datum instanceof Person) && ROLES.contains(roleType))
                      ? Arrays.asList(roleType.cast(new PersonJpaPersistable((Person)datum)))
                      : Collections.emptyList();
              }
        };
      }
  }
