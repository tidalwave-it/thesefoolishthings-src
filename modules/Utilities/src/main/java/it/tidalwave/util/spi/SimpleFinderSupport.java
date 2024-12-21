/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import lombok.NoArgsConstructor;

/***************************************************************************************************************************************************************
 *
 * A starting point for implementing a custom {@link Finder} that is not an extended finder. This class provides a
 * dummy copy constructor.
 *
 * @author Fabrizio Giudici
 * @it.tidalwave.javadoc.draft
 *
 **************************************************************************************************************************************************************/
@NoArgsConstructor
public abstract class SimpleFinderSupport<T> extends HierarchicFinderSupport<T, Finder<T>>
  {
    private static final long serialVersionUID = 743059684933055L;

    protected SimpleFinderSupport (@Nonnull final String name)
      {
        super(name);
      }

    protected SimpleFinderSupport (@Nonnull final SimpleFinderSupport<T> other, @Nonnull final Object override)
      {
        super(other, override);
      }
  }
