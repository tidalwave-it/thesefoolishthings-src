/***********************************************************************************************************************
 *
 * OpenBlueSky - NetBeans Platform Enhancements
 * ============================================
 *
 * Copyright (C) 2007-2010 by Tidalwave s.a.s.
 * Project home page: http://openbluesky.kenai.com
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
package it.tidalwave.util;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * This interface describes the behavior of classes that can be converted into a {@link String}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface StringValue 
  {
    /***************************************************************************
     *
     * Returns a {@link String} representing this object.
     *
     * @return  the string
     *
     **************************************************************************/
    @Nonnull
    public String stringValue();
  }
