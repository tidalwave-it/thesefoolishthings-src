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
package it.tidalwave.role.ui.spi;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.util.mock.VoidAsDelegateProvider;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.util.mock.DefaultContextManagerProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SimpleCompositePresentableTest
  {
//    // Not called by tests, we only need it's there
//    @ServiceProvider(service = AsDelegateProvider.class)
//    public static class MockAsDelegateProvider extends VoidAsDelegateProvider
//      {
//      }
//
//    @ServiceProvider(service = ContextManagerProvider.class)
//    public static class MockContextManagerProvider extends VoidContextManagerProvider
//      {
//      }
//
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor @ToString(of = "id")
    public static class MockDatum implements As
      {
        @Nonnull
        private final String id;

        @CheckForNull
        private SimpleComposite<MockDatum> composite;

        @Getter
        private List<MockDatum> children = new ArrayList<MockDatum>();

        @Nonnull
        public MockDatum withChildren (final @Nonnull MockDatum ... children)
          {
            return withChildren(Arrays.asList(children));
          }

        @Nonnull
        public MockDatum withChildren (final @Nonnull List<MockDatum> children)
          {
            this.children = children;
            composite = new DefaultSimpleComposite<MockDatum>(new SimpleFinderSupport<MockDatum>()
              {
                @Override @Nonnull
                protected List<? extends MockDatum> computeResults()
                  {
                    return children;
                  }
              });

            return this;
          }

        @Override @Nonnull
        public <T> T as (final @Nonnull Class<T> roleType)
          {
            return as(roleType, As.Defaults.throwAsException(roleType));
          }

        @Override @Nonnull
        public <T> T as (final @Nonnull Class<T> roleType, final @Nonnull NotFoundBehaviour<T> notFoundBehaviour)
          {
            if (roleType.equals(SimpleComposite.class) && (composite != null))
              {
                return roleType.cast(composite);
              }

            return notFoundBehaviour.run(new AsException(roleType));
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    class MockRole1
      {
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    class MockRole2
      {
        @Getter @Nonnull
        private final MockDatum datum;
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    class MockRoleFactory implements RoleFactory<MockDatum>
      {
        @Override @Nonnull
        public Object createRoleFor (final @Nonnull MockDatum datum)
          {
            return new MockRole2(datum);
          }
      }

    @BeforeMethod
    public void setup()
      {
        AsDelegateProvider.Locator.set(new VoidAsDelegateProvider());
        ContextManager.Locator.set(new DefaultContextManagerProvider());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_PresentationModel_containing_the_proper_children()
      {
        final MockDatum c1 = new MockDatum("c1");
        final MockDatum c2 = new MockDatum("c2");
        final MockDatum c3 = new MockDatum("c3");

        final MockDatum b1 = new MockDatum("b1").withChildren(c1, c2, c3);
        final MockDatum b2 = new MockDatum("b2");
        final MockDatum b3 = new MockDatum("b3");

        final MockDatum a = new MockDatum("a").withChildren(b1, b2, b3);

        final SimpleCompositePresentable<MockDatum> fixture
                = new SimpleCompositePresentable<MockDatum>(a, new DefaultPresentationModelFactory());

        final MockRole1 role1 = new MockRole1();
        final MockRoleFactory roleFactory = new MockRoleFactory();

        final PresentationModel pm = fixture.createPresentationModel(role1, roleFactory);

        assertProperPresentationModel(pm, a);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private void assertProperPresentationModel (final @Nonnull PresentationModel pm, final @Nonnull MockDatum datum)
      {
        pm.as(MockRole1.class);                        // must not throw AsException
        final MockRole2 role = pm.as(MockRole2.class); // must not throw AsException

        assertThat(role.getDatum(), is(sameInstance(datum)));

        final SimpleComposite<PresentationModel> composite = pm.as(SimpleComposite.class);
        final List<? extends PresentationModel> childrenPm = composite.findChildren().results();
        final List<MockDatum> childrenData = datum.getChildren();

        assertThat(childrenPm.size(), is(childrenData.size()));

        for (int i = 0; i < childrenPm.size(); i++)
          {
            assertProperPresentationModel(childrenPm.get(i), childrenData.get(i));
          }
      }
  }