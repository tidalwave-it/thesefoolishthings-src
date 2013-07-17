/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.mock.VoidAsDelegateProvider;
import java.util.Arrays;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContextManagerTest
  {
    private DefaultContextManager fixture;

    private Object globalContext1;

    private Object globalContext2;

    private Object globalContext3;

    private Object localContext1;

    private Object localContext2;

    private Object localContext3;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultContextManager();

        globalContext1 = new Object();
        globalContext2 = new Object();
        globalContext3 = new Object();
        localContext1 = new Object();
        localContext2 = new Object();
        localContext3 = new Object();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_start_with_no_contexts()
      {
        assertThat(fixture.getContexts(), is(Collections.<Object>emptyList()));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts()
      {
        fixture.addGlobalContext(globalContext1);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_add_and_retrieve_global_contexts_in_order()
      {
        fixture.addGlobalContext(globalContext1);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext2, globalContext3)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_remove_and_retrieve_global_contexts()
      {
        fixture.addGlobalContext(globalContext1);
        fixture.addGlobalContext(globalContext2);
        fixture.addGlobalContext(globalContext3);
        fixture.removeGlobalContext(globalContext2);

        assertThat(fixture.getContexts(), is(Arrays.asList(globalContext1, globalContext3)));
      }

  }