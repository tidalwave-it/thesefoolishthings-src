/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.ui.spi;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Id;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.Identifiable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static it.tidalwave.util.Parameters.r;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class SimpleCompositePresentableTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    public static class MockDatum implements As, Identifiable
      {
        @Getter @Nonnull
        private final Id id;

        @CheckForNull
        private SimpleComposite<MockDatum> composite;

        @Getter
        private List<MockDatum> children = new ArrayList<>();

        public MockDatum (@Nonnull final String id)
          {
            this.id = new Id(id);
          }

        @Nonnull
        public MockDatum withChildren (@Nonnull final MockDatum ... children)
          {
            return withChildren(Arrays.asList(children));
          }

        @Nonnull
        public MockDatum withChildren (@Nonnull final List<MockDatum> children)
          {
            composite = SimpleComposite.ofCloned(this.children = children);
            return this;
          }

        @Override @Nonnull
        public <T> T as (@Nonnull final Class<T> roleType)
          {
            return as(roleType, As.Defaults.throwAsException(roleType));
          }

        @Override @Nonnull
        public <T> T as (@Nonnull final Class<T> roleType, @Nonnull final NotFoundBehaviour<T> notFoundBehaviour)
          {
            if (roleType.equals(SimpleComposite.class) && (composite != null))
              {
                return roleType.cast(composite);
              }

            return notFoundBehaviour.run(new AsException(roleType));
          }

        @Override @Nonnull
        public <T> Collection<T> asMany (@Nonnull final Class<T> roleType)
          {
            if (roleType.equals(SimpleComposite.class) && (composite != null))
              {
                return new ArrayList<>(Arrays.asList(roleType.cast(composite)));
              }

            return new ArrayList<>();
          }

        @Override @Nonnull
        public String toString()
          {
            return String.format("MockDatum(%s)", id);
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static class MockRole1
      {
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class MockRole2
      {
        @Getter @Nonnull
        private final MockDatum datum;
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static class MockRoleFactory implements RoleFactory<MockDatum>
      {
        @Override @Nonnull
        public Object createRoleFor (@Nonnull final MockDatum datum)
          {
            return new MockRole2(datum);
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        AsDelegateProvider.Locator.set(AsDelegateProvider.empty());
        ContextManager.Locator.set(new DefaultContextManagerProvider());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_PresentationModel_containing_the_proper_children()
      {
        // given
        final MockDatum c1 = new MockDatum("c1");
        final MockDatum c2 = new MockDatum("c2");
        final MockDatum c3 = new MockDatum("c3");

        final MockDatum b1 = new MockDatum("b1").withChildren(c1, c2, c3);
        final MockDatum b2 = new MockDatum("b2");
        final MockDatum b3 = new MockDatum("b3");

        final MockDatum a = new MockDatum("a").withChildren(b1, b2, b3);

        final SimpleCompositePresentable<MockDatum> underTest
                = new SimpleCompositePresentable<>(a, new DefaultPresentationModelFactory());

        final MockRole1 role1 = new MockRole1();
        final MockRoleFactory roleFactory = new MockRoleFactory();
        // when
        final PresentationModel pm = underTest.createPresentationModel(r(role1, roleFactory));
        // then
        assertProperPresentationModel("", pm, a);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private void assertProperPresentationModel (@Nonnull final String indent,
                                                @Nonnull final PresentationModel pm,
                                                @Nonnull final MockDatum datum)
      {
        log.debug("assertProperPresentationModel() - {} {}, {}", indent, pm, datum);
        pm.as(MockRole1.class);                        // must not throw AsException
        final MockRole2 role = pm.as(MockRole2.class); // must not throw AsException

        assertThat(role.getDatum(), is(sameInstance(datum)));

        final SimpleComposite<PresentationModel> composite = pm.as(SimpleComposite.class);
        final List<? extends PresentationModel> childrenPm = composite.findChildren().results();
        final List<MockDatum> childrenData = datum.getChildren();

        assertThat(childrenPm.size(), is(childrenData.size()));

        for (int i = 0; i < childrenPm.size(); i++)
          {
            assertProperPresentationModel(indent + "    ", childrenPm.get(i), childrenData.get(i));
          }
      }
  }