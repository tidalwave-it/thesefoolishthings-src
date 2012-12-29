/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.util;

import it.tidalwave.util.asexamples.AsExtensions;
import it.tidalwave.util.asexamples.Datum1;
import it.tidalwave.util.asexamples.Datum2;
import it.tidalwave.util.asexamples.RenderingContext;
import it.tidalwave.util.asexamples.Renderable;
import lombok.experimental.ExtensionMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.util.asexamples.Renderable.*;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class AsTest 
  {
    private RenderingContext terminal;
    
    @BeforeMethod
    public void setupTerminal()
      {
        terminal = mock(RenderingContext.class);  
      }
    
    @Test
    public void test1()
      {
        final Datum1 datum1 = new Datum1("foo");
        
        datum1.as(Renderable.class).renderTo(terminal);
        
        verify(terminal).render(eq("foo"));
      }
    
    @Test
    public void test2()
      {
        final Datum2 datum2 = new Datum2("bar");
        
        datum2.as(Renderable).renderTo(terminal);
        
        verify(terminal).render(eq("bar"));
      }
    
    @Test
    public void test3()
      {
        final String string = "foobar";
        
        string.as(Renderable).renderTo(terminal);
        
        verify(terminal).render(eq("foobar"));
      }
  }
