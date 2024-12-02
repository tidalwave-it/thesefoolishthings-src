/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import it.tidalwave.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static org.testng.FileAssert.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 *
 **********************************************************************************************************************/
public class UserActionProviderTest
  {
    @RequiredArgsConstructor(staticName = "sameInstances")
    static class InstanceMatcher extends BaseMatcher<Collection<? extends UserAction>>
      {
        private final List<UserAction> expected;

        @Override
        public boolean matches (@Nonnull final Object object)
          {
            final var actual = (List<UserAction>)object;

             if (expected.size() != actual.size())
               {
                 return false;
               }

             for (var i = 0; i < actual.size(); i++)
               {
                 if (expected.get(i) != actual.get(i))
                   {
                     return false;
                   }
               }

             return true;
          }

        @Override
        public void describeTo(final Description description)
          {
            description.appendValue(expected);
          }
      }

    private final UserAction[] ACTIONS = IntStream.range(0, 10)
                                                  .mapToObj(i -> mock(UserAction.class))
                                                  .collect(toList())
                                                  .toArray(new UserAction[0]);

    private final UserAction[] NO_ACTIONS = new UserAction[0];

    @Test
    public void works_with_actions()
      throws NotFoundException
      {
        // when
        final var underTest = UserActionProvider.of(ACTIONS);
        // then
        assertThat(underTest.getActions(), is(InstanceMatcher.sameInstances(asList(ACTIONS))));
        assertThat(underTest.getDefaultAction(), sameInstance(ACTIONS[0]));
        assertThat(underTest.getOptionalDefaultAction().get(), sameInstance(ACTIONS[0]));
      }

    @Test
    public void works_with_no_actions()
      {
        // when
        final var underTest = UserActionProvider.of(NO_ACTIONS);
        // then
        assertThat(underTest.getActions(), is(emptyList()));
        assertThat(underTest.getOptionalDefaultAction().isPresent(), is(false));

        try
          {
            underTest.getDefaultAction();
            fail("Expected NotFoundException");
          }
        catch (NotFoundException e)
          {
            // ok
          }
      }
  }
