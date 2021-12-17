/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.impl;

import javax.annotation.Nonnull;
import java.text.Collator;
import java.util.Comparator;
import it.tidalwave.util.As;
import it.tidalwave.role.ui.Displayable;
import static it.tidalwave.role.ui.Displayable._Displayable_;

/***********************************************************************************************************************
 *
 * A {@link Comparator} for classes implementing the {@link As} interface containing a {@link Displayable} role.
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.draft Will be moved to a different package
 *
 **********************************************************************************************************************/
public final class AsDisplayableComparator implements Comparator<As>
  {
    private static final AsDisplayableComparator INSTANCE = new AsDisplayableComparator();

    private final Collator collator = Collator.getInstance();

    @Nonnull
    public static AsDisplayableComparator getInstance()
      {
        return INSTANCE;
      }

    @Override
    public int compare (@Nonnull final As object1, @Nonnull final As object2)
      {
        return collator.compare(object1.as(_Displayable_).getDisplayName(), object2.as(_Displayable_).getDisplayName());
      }
  }
