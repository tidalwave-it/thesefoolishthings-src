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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.AsException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/***********************************************************************************************************************
 *
 * A {@link Matcher} for {@link PresentationModel}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NotThreadSafe
public class PresentationModelMatcher extends BaseMatcher<PresentationModel>
  {
    private final StringBuilder pmDescription = new StringBuilder("PresentationModel");

    private String separator = "";

    private final List<Class<?>> expectedRoleTypes = new ArrayList<>();

    @Nonnull
    public static PresentationModelMatcher presentationModel()
      {
        return new PresentationModelMatcher();
      }

    @Nonnull
    public PresentationModelMatcher withRole (final @Nonnull Class<?> roleType)
      {
        expectedRoleTypes.add(roleType);
        pmDescription.append(separator).append(" with role ").append(roleType.getName());
        separator = ", ";
        return this;
      }

    @Override
    public boolean matches (final Object item)
      {
        if (!(item instanceof PresentationModel))
          {
            return false;
          }

        final PresentationModel pm = (PresentationModel)item;

        for (final Class<?> roleType : expectedRoleTypes)
          {
            try
              {
                pm.as(roleType);
              }
            catch (AsException e)
              {
                return false;
              }
          }

        return true;
      }

    @Override
    public void describeTo (final @Nonnull Description description)
      {
        description.appendText(pmDescription.toString());
      }
  }
