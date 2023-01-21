/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
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

import java.util.function.Consumer;
import it.tidalwave.util.asexamples.AsExtensions;
import it.tidalwave.util.asexamples.Datum1;
import it.tidalwave.util.asexamples.Datum2;
import lombok.experimental.ExtensionMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.util.asexamples.Renderable._Renderable_;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@ExtensionMethod(AsExtensions.class)
public class AsExtensionsTest
  {
    private Consumer<String> terminal;

    @BeforeMethod
    public void setupTerminal()
      {
        terminal = mock(Consumer.class);
      }

    @Test
    public void test1()
      {
        // given
        final var datum = new Datum1("foo");
        // when
        datum.as(_Renderable_).renderTo(terminal);
        // then
        verify(terminal).accept(eq("foo"));
      }

    @Test
    public void test2()
      {
        // given
        final var datum = new Datum2("bar");
        // when
        datum.as(_Renderable_).renderTo(terminal);
        // then
        verify(terminal).accept(eq("bar"));
      }

    @Test
    public void test3()
      {
        // given
        final var string = "foobar";
        // when
        string.as(_Renderable_).renderTo(terminal);
        // then
        verify(terminal).accept(eq("foobar"));
      }
  }
