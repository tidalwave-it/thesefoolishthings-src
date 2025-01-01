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
package it.tidalwave.role.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.AsException;
import it.tidalwave.role.spring.mock.MockConcreteRole1;
import it.tidalwave.role.spring.mock.MockConcreteRole2;
import it.tidalwave.role.spring.mock.MockDatum1;
import it.tidalwave.role.spring.mock.MockDatum2;
import it.tidalwave.role.spring.mock.MockRole1;
import it.tidalwave.role.spring.mock.MockRole2;
import it.tidalwave.role.spring.mock.MockRole3;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class SpringAdapterTest // FIXME: tests are genera, move to a AsSupportTestSupport
  {
    private ApplicationContext context;

    private MockDatum1 datum1;

    private MockDatum2 datum2;

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        context = new ClassPathXmlApplicationContext(RoleSpringConfiguration.BEANS);
        datum1 = new MockDatum1();
        datum2 = new MockDatum2();
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void must_inject_a_role_properly_assigning_its_owner_1()
      {
        // when
        final var role = datum1.as(MockRole1.class);
        // then
        assertThat(role, is(notNullValue()));
        assertThat(role, is(instanceOf(MockConcreteRole1.class)));
        assertThat(((MockConcreteRole1)role).getOwner(), is(sameInstance(datum1)));
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void must_inject_a_role_supporting_multiple_datum_types_properly_assigning_its_owner()
      {
        // when
        final var role1 = datum1.as(MockRole2.class);
        final var role2 = datum2.as(MockRole2.class);
        // then
        assertThat(role1, is(notNullValue()));
        assertThat(role1, is(instanceOf(MockConcreteRole2.class)));
        assertThat(((MockConcreteRole2)role1).getOwner(), is(sameInstance(datum1)));

        assertThat(role2, is(notNullValue()));
        assertThat(role2, is(instanceOf(MockConcreteRole2.class)));
        assertThat(((MockConcreteRole2)role2).getOwner(), is(sameInstance(datum2)));
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_throw_AsException_when_asking_for_an_unavailable_role_1()
      {
        datum1.as(MockRole3.class);
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test(expectedExceptions = AsException.class)
    public void must_throw_AsException_when_asking_for_an_unavailable_role_2()
      {
        datum2.as(MockRole1.class);
      }

    /***********************************************************************************************************************************************************
     * 
     **********************************************************************************************************************************************************/
    @Test
    public void must_return_the_datum_object_when_it_directly_implements_a_role()
      {
        // when
        final var role = datum2.as(MockRole3.class);
        // then
        assertThat(role, is(notNullValue()));
        assertThat(role, is(sameInstance(datum2)));
      }
  }