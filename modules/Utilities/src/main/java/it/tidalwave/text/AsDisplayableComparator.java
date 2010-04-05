/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.text;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.text.Collator;
import java.io.Serializable;
import it.tidalwave.util.As;
import it.tidalwave.util.Displayable;
import static it.tidalwave.util.Displayable.Displayable;

/***********************************************************************************************************************
 *
 * A {@link Comparator} for classes implementing the {@link As} interface containing a {@link Displayable} role.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @draft Will be moved to a different package
 *
 **********************************************************************************************************************/
public final class AsDisplayableComparator implements Comparator<As>, Serializable
  {
    private static final AsDisplayableComparator INSTANCE = new AsDisplayableComparator();

    private final Collator collator = Collator.getInstance();

    @Nonnull
    public static AsDisplayableComparator getInstance()
      {
        return INSTANCE;
      }

    @Override
    public int compare (final @Nonnull As object1, final @Nonnull As object2)
      {
        return collator.compare(object1.as(Displayable).getDisplayName(), object2.as(Displayable).getDisplayName());
      }
  }
