/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s.
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.swing.beansbinding.converter;

import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class CommaSeparatedListConverterTest 
  {
    private CommaSeparatedListConverter converter;

    @Before
    public void setUp() 
      {
        converter = new CommaSeparatedListConverter();
      }  

    @After
    public void tearDown() 
      {
        converter = null;
      }

    @Test
    public void convertForward1() 
      {
        assertEquals("one", converter.convertForward(Arrays.asList("one")));
      }

    @Test
    public void convertForward2() 
      {
        assertEquals("one, two", converter.convertForward(Arrays.asList("one", "two")));
      }

    @Test
    public void convertForwardNull() 
      {
        assertEquals(null, converter.convertForward(null));
      }

    @Test
    public void convertForwardEmpty() 
      {
        assertEquals("", converter.convertForward(Collections.<String>emptyList()));
      }
    
    @Test
    public void convertReverse1() 
      {
        assertEquals(Arrays.asList("one"), converter.convertReverse("one"));
      }
    
    @Test
    public void convertReverse2() 
      {
        assertEquals(Arrays.asList("one", "two"), converter.convertReverse("one, two"));
      }
    
    @Test
    public void convertReverseNull() 
      {
        assertEquals(null, converter.convertReverse(null));
      }
    
    @Test
    public void convertReverseEmpty() 
      {
        assertEquals(Collections.<String>emptyList(), converter.convertReverse(""));
      }
  }