/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.spi;

import jakarta.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import it.tidalwave.util.ContextManager;
import it.tidalwave.util.impl.OwnerAndRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Arrays.asList;
import static it.tidalwave.util.ShortNames.*;
import static it.tidalwave.util.spi.Mocks.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.mockito.Mockito.*;

class XCA1 extends CA1 {}
class YCA1 extends XCA1 {}

class UnderTest extends SystemRoleFactorySupport
  {
    private final Map<Class<?>, Class<?>[]> ownerClassesMapByRoleClass = new HashMap<>();

    private final Map<Class<?>, Class<?>> contextClassMapByRoleClass = new HashMap<>();

    private final Map<Class<?>, Object> beanMapByClass = new HashMap<>();

    public void register (@Nonnull final Class<?> roleClass, @Nonnull final Class<?> ... ownerClasses)
      {
        ownerClassesMapByRoleClass.put(roleClass, ownerClasses);
      }

    public void registerContext (@Nonnull final Class<?> roleImplementationClass,
                                 @Nonnull final Class<?> contextClass)
      {
        contextClassMapByRoleClass.put(roleImplementationClass, contextClass);
      }

    public void registerBean (@Nonnull final Object bean)
      {
        beanMapByClass.put(bean.getClass(), bean);
      }

    @Override @Nonnull @SuppressWarnings("BoundedWildcard")
    protected <T> Optional<T> getBean (@Nonnull final Class<T> beanType)
      {
        return Optional.ofNullable(beanMapByClass.get(beanType)).map(beanType::cast);
      }

    @Override @Nonnull
    protected Optional<Class<?>> findContextTypeForRole (@Nonnull final Class<?> roleImplementationClass)
      {
        return Optional.ofNullable(contextClassMapByRoleClass.get(roleImplementationClass));
      }

    @Override @Nonnull
    protected Class<?>[] findDatumTypesForRole (@Nonnull final Class<?> roleImplementationClass)
      {
        final var result = ownerClassesMapByRoleClass.get(roleImplementationClass);
        return (result == null) ? new Class<?>[0] : result;
      }
  }

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class SystemRoleFactorySupportTest
  {
    private static final Context1 context1 = new Context1();

    private static final Context2 context2 = new Context2();

    private static final Bean1 bean1 = new Bean1();

    private static final Bean2 bean2 = new Bean2();

    private ContextManager contextManager;

    /**********************************************************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        contextManager = mock(ContextManager.class);
        ContextManager.set(() -> contextManager);
      }

    /**********************************************************************************************************************************************************/
    @Test(dataProvider = "classesAndExpectedInterfaces")
    public void must_correctly_find_implemented_interfaces (@Nonnull final Class<?> clazz, @Nonnull final Set<Class<?>> expectedInterfaces)
      {
        // given the mocks
        // when
        final var actualInterfaces = SystemRoleFactorySupport.findAllImplementedInterfacesOf(clazz);
        // then
        assertThat(actualInterfaces).containsExactlyInAnyOrder(expectedInterfaces.toArray(Class[]::new));
      }

    /**********************************************************************************************************************************************************/
    @DataProvider
    private static Object[][] classesAndExpectedInterfaces()
      {
        return new Object[][]
          {
            { CA1.class, Set.of() },
            { CA2.class, Set.of(IA2.class) },
            { CA3.class, Set.of(IA3.class) },
            { CB1.class, Set.of(IB2.class, IA2.class, IA3.class) },
            { CB2.class, Set.of(IA2.class, IB1.class, IA1.class) },
            { CB3.class, Set.of() },
          };
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void must_properly_scan_classes()
      {
        // given
        final var underTest = new UnderTest();
        registerMockRoles(underTest);
        // when
        underTest.scan(asList(RI1A.class, RI1B.class, RI1C.class,
                              RI2A.class, RI2B.class, RI2C.class,
                              RI3A.class, RI3B.class, RI3C.class));
        // then
        final var m = underTest.roleMapByOwnerAndRole;

        assertThat(m).hasSize(8);
        assertThat(m.getValues(new OwnerAndRole(IA1.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(IA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R2.class))).containsOnly(RI2A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA3.class, R3.class))).containsOnly(RI3A.class);
        assertThat(m.getValues(new OwnerAndRole(CB1.class, R2.class))).containsOnly(RI2B.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R2.class))).containsOnly(RI2A.class, RI2B.class);

        // The following were not mapped now, but will be discovered later:
        // CA2 -> R12C
        // CB1 -> R12C
        // CB2 -> RI1A, R12C, R13C
        // CB3 -> R11A

        assertThat(underTest.findRoleImplementationsFor(CA1.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(CA1.class, R2.class)).containsOnly(RI2A.class);
        assertThat(underTest.findRoleImplementationsFor(CA1.class, R3.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CA2.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(CA2.class, R2.class)).containsOnly(RI2C.class);
        assertThat(underTest.findRoleImplementationsFor(CA2.class, R3.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CA3.class, R1.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CA3.class, R2.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CA3.class, R3.class)).containsOnly(RI3A.class);
        assertThat(underTest.findRoleImplementationsFor(CB1.class, R1.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CB1.class, R2.class)).containsOnly(RI2B.class, RI2C.class);
        assertThat(underTest.findRoleImplementationsFor(CB1.class, R3.class)).isEmpty();
        assertThat(underTest.findRoleImplementationsFor(CB2.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(CB2.class, R2.class)).containsOnly(RI2C.class);
        assertThat(underTest.findRoleImplementationsFor(CB2.class, R3.class)).containsOnly(RI3C.class);
        assertThat(underTest.findRoleImplementationsFor(CB3.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(CB3.class, R2.class)).containsOnly(RI2A.class, RI2B.class);
        assertThat(underTest.findRoleImplementationsFor(CB3.class, R3.class)).isEmpty();

        assertThat(m).hasSize(13);
        assertThat(m.getValues(new OwnerAndRole(IA1.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(IA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R2.class))).containsOnly(RI2A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA3.class, R3.class))).containsOnly(RI3A.class);
        assertThat(m.getValues(new OwnerAndRole(CB1.class, R2.class))).containsOnly(RI2B.class, RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R2.class))).containsOnly(RI2A.class, RI2B.class);

        // These were not explicitly registered - late discovery
        assertThat(underTest.findRoleImplementationsFor(XCA1.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(XCA1.class, R2.class)).containsOnly(RI2A.class);
        assertThat(underTest.findRoleImplementationsFor(XCA1.class, R3.class)).isEmpty();

        assertThat(m).hasSize(15);
        assertThat(m.getValues(new OwnerAndRole(IA1.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(IA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R2.class))).containsOnly(RI2A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA3.class, R3.class))).containsOnly(RI3A.class);
        assertThat(m.getValues(new OwnerAndRole(CB1.class, R2.class))).containsOnly(RI2B.class, RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R2.class))).containsOnly(RI2A.class, RI2B.class);

        assertThat(m.getValues(new OwnerAndRole(XCA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(XCA1.class, R2.class))).containsOnly(RI2A.class);

        // These were not explicitly registered - late discovery
        assertThat(underTest.findRoleImplementationsFor(YCA1.class, R1.class)).containsOnly(RI1A.class);
        assertThat(underTest.findRoleImplementationsFor(YCA1.class, R2.class)).containsOnly(RI2A.class);
        assertThat(underTest.findRoleImplementationsFor(YCA1.class, R3.class)).isEmpty();

        assertThat(m).hasSize(17);
        assertThat(m.getValues(new OwnerAndRole(IA1.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(IA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA1.class, R2.class))).containsOnly(RI2A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CA2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CA3.class, R3.class))).containsOnly(RI3A.class);
        assertThat(m.getValues(new OwnerAndRole(CB1.class, R2.class))).containsOnly(RI2B.class, RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R2.class))).containsOnly(RI2C.class);
        assertThat(m.getValues(new OwnerAndRole(CB2.class, R3.class))).containsOnly(RI3C.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(CB3.class, R2.class))).containsOnly(RI2A.class, RI2B.class);
        assertThat(m.getValues(new OwnerAndRole(XCA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(XCA1.class, R2.class))).containsOnly(RI2A.class);

        assertThat(m.getValues(new OwnerAndRole(YCA1.class, R1.class))).containsOnly(RI1A.class);
        assertThat(m.getValues(new OwnerAndRole(YCA1.class, R2.class))).containsOnly(RI2A.class);
      }

    /**********************************************************************************************************************************************************/
    @Test(dataProvider = "ownersAndRoleImplementations")
    public void must_correctly_find_roles (@Nonnull final Object owner,  @Nonnull final Class<?> roleClass, @Nonnull final List<Object> expectedRoles)
      {
        // given
        when(contextManager.findContextOfType(eq(Context1.class))).thenReturn(Optional.empty());
        when(contextManager.findContextOfType(eq(Context2.class))).thenReturn(Optional.of(context2));

        final var underTest = new UnderTest();
        registerMockRoles(underTest);
        underTest.registerBean(bean1);
        underTest.registerBean(bean2);
        underTest.scan(asList(RI1A.class, RI1B.class, RI1C.class,
                              RI2A.class, RI2B.class, RI2C.class,
                              RI3A.class, RI3B.class, RI3C.class));
        // when
        final var actualRoles = (List<Object>)underTest.findRoles(owner, roleClass);
        // then
        final var message = String.format("owner: %s role: %s", shortId(owner), shortName(roleClass));
        assertThat(actualRoles).withFailMessage(message).containsExactlyInAnyOrderElementsOf(expectedRoles);
      }

    /**********************************************************************************************************************************************************/
    @DataProvider
    private static Object[][] ownersAndRoleImplementations()
      {
        final var ca3 = new CA3();

        return new Object[][]
          {
          //  owner      role      expected role implementations
            {new CA1(), R1.class, List.of(new RI1A())},
            // no RI2A because Context1 is not present
            {new CA1(), R2.class, List.of()},
            {new CA1(), R3.class, List.of()},

            {new CA2(), R1.class, List.of(new RI1A())},
            {new CA2(), R2.class, List.of(new RI2C(context2, bean1))},
            {new CA2(), R3.class, List.of()},

            {new CA3(), R1.class, List.of()},
            {new CA3(), R2.class, List.of()},
            {ca3, R3.class, List.of(new RI3A(ca3))},

            {new CB1(), R1.class, List.of()},
            // RI2C because Context2 is present
            {new CB1(), R2.class, asList(new RI2B(bean1, bean2), new RI2C(context2, bean1)) },
            {new CB1(), R3.class, List.of()},

            {new CB2(), R1.class, List.of(new RI1A())},
            {new CB2(), R2.class, List.of(new RI2C(context2, bean1))},
            {new CB2(), R3.class, List.of(new RI3C())},

            {new CB3(), R1.class, List.of(new RI1A())},
            // no RI2A because Context1 is not present
            {new CB3(), R2.class, List.of(new RI2B(bean1, bean2))},
            {new CB3(), R3.class, List.of()},
          };
      }

    /**********************************************************************************************************************************************************/
    @Test
    public void findTypeOf_must_return_original_class_for_Mockito_mocks()
      {
        // given
        final R1 nonMock = new RI1A();
        final var mock = mock(R2.class);
        // when
        final var nonMockType = SystemRoleFactorySupport.findTypeOf(nonMock);
        final var mockType = SystemRoleFactorySupport.findTypeOf(mock);
        // then
        assertEquals(RI1A.class, nonMockType);
        assertEquals(R2.class, mockType);
      }

    /**********************************************************************************************************************************************************/
    private void registerMockRoles (@Nonnull final UnderTest underTest)
      {
        underTest.register(RI1A.class, CA1.class, CA2.class);
        underTest.register(RI3A.class, CA3.class);
        underTest.register(RI2B.class, CB1.class, CB3.class);
        underTest.register(RI2A.class, CA1.class, CB3.class);
        underTest.register(RI3C.class, IA1.class);
        underTest.register(RI2C.class, IA2.class);

        underTest.registerContext(RI2A.class, Context1.class);
        underTest.registerContext(RI2C.class, Context2.class);
      }
  }