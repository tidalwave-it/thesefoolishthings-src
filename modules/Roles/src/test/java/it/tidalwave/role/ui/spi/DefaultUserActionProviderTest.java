/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-role
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
package it.tidalwave.role.ui.spi;

import it.tidalwave.role.ui.UserAction;
import it.tidalwave.util.NotFoundException;
import java.util.Collection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultUserActionProviderTest
  {
    private DefaultUserActionProvider underTest;

    @BeforeMethod
    public void setup()
      {
        underTest = new DefaultUserActionProvider();
      }

    @Test
    public void must_return_no_actions()
      {
        // when
        final Collection<? extends UserAction> actions = underTest.getActions();
        // then
        assertThat(actions.isEmpty(), is(true));
      }

    @Test(expectedExceptions = NotFoundException.class)
    public void must_return_no_default_action()
      throws NotFoundException
      {
        // when
        underTest.getDefaultAction();
      }
  }