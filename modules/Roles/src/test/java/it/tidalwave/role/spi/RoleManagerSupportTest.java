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
package it.tidalwave.role.spi;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.impl.DatumAndRole;
import it.tidalwave.role.spi.impl.MultiMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static java.util.Arrays.asList;
import static org.testng.AssertJUnit.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static it.tidalwave.role.spi.impl.LogUtil.*;
import static it.tidalwave.role.spi.impl.Mocks.*;

class XCA1 extends CA1 {}
class YCA1 extends XCA1 {}

class UnderTest extends RoleManagerSupport
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

    @Override @Nonnull
    protected <T> T getBean (@Nonnull final Class<T> beanType)
      {
        return (T)beanMapByClass.get(beanType);
      }

    @Override @Nonnull
    protected Class<?> findContextTypeForRole (final Class<?> roleImplementationClass)
      throws NotFoundException
      {
        return NotFoundException.throwWhenNull(contextClassMapByRoleClass.get(roleImplementationClass), "No context");
      }

    @Override @Nonnull
    protected Class<?>[] findDatumTypesForRole (final Class<?> roleImplementationClass)
      {
        final Class<?>[] result = ownerClassesMapByRoleClass.get(roleImplementationClass);
        return (result == null) ? new Class<?>[0] : result;
      }
  }

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class RoleManagerSupportTest
  {
    private static final Context1 context1 = new Context1();

    private static final Context2 context2 = new Context2();

    private static final Bean1 bean1 = new Bean1();

    private static final Bean2 bean2 = new Bean2();

    private ContextManager contextManager;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        contextManager = mock(ContextManager.class);
        ContextManager.Locator.set(() -> contextManager);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "classesAndExpectedInterfaces")
    public void must_correctly_find_implemented_interfaces (@Nonnull final Class<?> clazz,
                                                            @Nonnull final Set<Class<?>> expectedInterfaces)
      {
        // given the mocks
        // when
        final SortedSet<Class<?>> actualInterfaces = RoleManagerSupport.findAllImplementedInterfacesOf(clazz);
        // then
        assertThat(actualInterfaces, is(expectedInterfaces));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider
    private static Object[][] classesAndExpectedInterfaces()
      {
        return new Object[][]
          {
            { CA1.class, asSet() },
            { CA2.class, asSet(IA2.class) },
            { CA3.class, asSet(IA3.class) },
            { CB1.class, asSet(IB2.class, IA2.class, IA3.class) },
            { CB2.class, asSet(IA2.class, IB1.class, IA1.class) },
            { CB3.class, asSet() },
          };
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_scan_classes()
      {
        // given
        final UnderTest underTest = new UnderTest();
        registerMockRoles(underTest);
        // when
        underTest.scan(asList(RI1A.class, RI1B.class, RI1C.class,
                              RI2A.class, RI2B.class, RI2C.class,
                              RI3A.class, RI3B.class, RI3C.class));
        // then
        final MultiMap<DatumAndRole, Class<?>> m = underTest.roleMapByDatumAndRole;

        assertThat(m.size(), is(8));
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));

        // The following were not mapped now, but will be discovered later:
        // CA2 -> R12C
        // CB1 -> R12C
        // CB2 -> RI1A, R12C, R13C
        // CB3 -> R11A

        assertSetEquals(underTest.findRoleImplementationsFor(CA1.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CA1.class, R2.class), asSet(RI2A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CA1.class, R3.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CA2.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CA2.class, R2.class), asSet(RI2C.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CA2.class, R3.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CA3.class, R1.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CA3.class, R2.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CA3.class, R3.class), asSet(RI3A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB1.class, R1.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CB1.class, R2.class), asSet(RI2B.class, RI2C.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB1.class, R3.class), asSet());
        assertSetEquals(underTest.findRoleImplementationsFor(CB2.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB2.class, R2.class), asSet(RI2C.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB2.class, R3.class), asSet(RI3C.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB3.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB3.class, R2.class), asSet(RI2A.class, RI2B.class));
        assertSetEquals(underTest.findRoleImplementationsFor(CB3.class, R3.class), asSet());

        assertThat(m.size(), is(13));
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));

        // These were not explicitly registered - late discovery
        assertSetEquals(underTest.findRoleImplementationsFor(XCA1.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(XCA1.class, R2.class), asSet(RI2A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(XCA1.class, R3.class), asSet());

        assertThat(m.size(), is(15));
        assertThat(m.getValues(new DatumAndRole(IA1.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class, R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class, R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class, R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class, R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class, R2.class)), is(asSet(RI2A.class, RI2B.class)));

        assertThat(m.getValues(new DatumAndRole(XCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R2.class)), is(asSet(RI2A.class)));

        // These were not explicitly registered - late discovery
        assertSetEquals(underTest.findRoleImplementationsFor(YCA1.class, R1.class), asSet(RI1A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(YCA1.class, R2.class), asSet(RI2A.class));
        assertSetEquals(underTest.findRoleImplementationsFor(YCA1.class, R3.class), asSet());

        assertThat(m.size(), is(17));
        assertThat(m.getValues(new DatumAndRole(IA1.class,  R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(IA2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA1.class,  R2.class)), is(asSet(RI2A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CA2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CA3.class,  R3.class)), is(asSet(RI3A.class)));
        assertThat(m.getValues(new DatumAndRole(CB1.class,  R2.class)), is(asSet(RI2B.class, RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R2.class)), is(asSet(RI2C.class)));
        assertThat(m.getValues(new DatumAndRole(CB2.class,  R3.class)), is(asSet(RI3C.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class,  R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(CB3.class,  R2.class)), is(asSet(RI2A.class, RI2B.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(XCA1.class, R2.class)), is(asSet(RI2A.class)));

        assertThat(m.getValues(new DatumAndRole(YCA1.class, R1.class)), is(asSet(RI1A.class)));
        assertThat(m.getValues(new DatumAndRole(YCA1.class, R2.class)), is(asSet(RI2A.class)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "ownersAndRoleImplementations")
    public void must_correctly_find_roles (@Nonnull final Object owner,
                                           @Nonnull final Class<?> roleClass,
                                           @Nonnull final List<?> expectedRoles)
      throws NotFoundException
      {
        // given
        when(contextManager.findContextOfType(eq(Context1.class))).thenThrow(new NotFoundException());
        when(contextManager.findContextOfType(eq(Context2.class))).thenReturn(context2);

        final UnderTest underTest = new UnderTest();
        registerMockRoles(underTest);
        underTest.registerBean(bean1);
        underTest.registerBean(bean2);
        underTest.scan(asList(RI1A.class, RI1B.class, RI1C.class,
                              RI2A.class, RI2B.class, RI2C.class,
                              RI3A.class, RI3B.class, RI3C.class));
        // when
        final List<?> actualRoles = underTest.findRoles(owner, roleClass);
        // then
        final String s = String.format("owner: %s role: %s", shortId(owner), shortName(roleClass));
        assertListEquals(s, actualRoles, expectedRoles);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider
    private static Object[][] ownersAndRoleImplementations()
      {
        final CA3 ca3 = new CA3();

        return new Object[][]
          {
          //  owner      role      expected role implementations
            { new CA1(), R1.class, asList(new RI1A()) },
            // no RI2A because Context1 is not present
            { new CA1(), R2.class, asList()           },
            { new CA1(), R3.class, asList()           },

            { new CA2(), R1.class, asList(new RI1A()) },
            { new CA2(), R2.class, asList(new RI2C(context2, bean1)) },
            { new CA2(), R3.class, asList()           },

            { new CA3(), R1.class, asList()           },
            { new CA3(), R2.class, asList()           },
            { ca3,       R3.class, asList(new RI3A(ca3)) },

            { new CB1(), R1.class, asList()           },
            // RI2C because Context2 is present
            { new CB1(), R2.class, asList(new RI2B(bean1, bean2, "a"), new RI2C(context2, bean1)) },
            { new CB1(), R3.class, asList()           },

            { new CB2(), R1.class, asList(new RI1A()) },
            { new CB2(), R2.class, asList(new RI2C(context2, bean1)) },
            { new CB2(), R3.class, asList(new RI3C()) },

            { new CB3(), R1.class, asList(new RI1A()) },
            // no RI2A because Context1 is not present
            { new CB3(), R2.class, asList(new RI2B(bean1, bean2, "a")) },
            { new CB3(), R3.class, asList()           },
          };
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void findTypeOf_must_return_original_class_for_Mockito_mocks()
      {
        // given
        final R1 nonMock = new RI1A();
        final R2 mock = mock(R2.class);
        // when
        final Class<R1> nonMockType = RoleManagerSupport.findTypeOf(nonMock);
        final Class<R2> mockType = RoleManagerSupport.findTypeOf(mock);
        // then
        assertEquals(RI1A.class, nonMockType);
        assertEquals(R2.class, mockType);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     *
     * To get rid of generics problems.
     *
     ******************************************************************************************************************/
    private static <T> void assertSetEquals (final Set actual, final Set expected)
      {
        assertThat(actual, is(expected));
      }

    /*******************************************************************************************************************
     *
     * To get rid of generics problems.
     *
     ******************************************************************************************************************/
    private static <T> void assertListEquals (@Nonnull final String message,
                                              @Nonnull final List actual,
                                              @Nonnull final List expected)
      {
        sort(actual);
        sort(expected);
        assertThat(message, actual, is(expected));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Set<Class<?>> asSet (final Class<?> ... objects)
      {
        return new HashSet<>(asList(objects));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private static void sort (@Nonnull final List<?> list)
      {
        list.sort(Comparator.comparing(Object::toString));
      }
  }