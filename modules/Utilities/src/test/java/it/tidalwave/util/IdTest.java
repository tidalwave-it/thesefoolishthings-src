/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
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
package it.tidalwave.util;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.testng.AssertJUnit.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class IdTest
  {
    private Id id1;
    private Id id2;
    private Id id1a;

    @BeforeMethod
    public void setUp()
      {
        id1 = new Id("id1");
        id2 = new Id("id2");
        id1a = new Id("id1");
      }

    @Test
    public void testStringValue()
      {
        assertEquals("id1", id1.stringValue());
        assertEquals("id2", id2.stringValue());
        assertEquals("id1", id1a.stringValue());
      }

    @Test
    public void testEquals()
      {
        assertTrue(id1.equals(id1a));
        assertTrue(id1a.equals(id1));
        assertFalse(id1.equals(id2));
        assertFalse(id2.equals(id1));
      }

    @Test
    public void testHashCode()
      {
        assertEquals(104113L, id1.hashCode());
        assertEquals(104114L, id2.hashCode());
        assertEquals(104113L, id1a.hashCode());
      }

    @Test
    public void testCompareTo()
      {
        assertEquals(0, id1.compareTo(id1a));
        assertEquals(0, id1a.compareTo(id1));
        assertEquals(-1, id1.compareTo(id2));
        assertEquals(+1, id2.compareTo(id1));
      }

    @Test
    public void testToString()
      {
        assertEquals("id1", id1.toString());
        assertEquals("id1", id1a.toString());
        assertEquals("id2", id2.toString());
      }
  }
