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
package it.tidalwave.role.spi.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class Mocks
  {
    public static interface IA1 { }
    public static interface IA2 { }
    public static interface IA3 { }

    public static interface IB1 extends IA1 { }
    public static interface IB2 extends IA2, IA3 { }
    public static interface IB3 { }

    public static class CA1 { }
    public static class CA2 implements IA2 { }
    public static class CA3 implements IA3 { }

    public static class CB1 implements IB2 { }
    public static class CB2 extends CA2 implements IB1 { }
    public static class CB3 extends CA1 { }

    public static interface R1 { }
    public static interface R2 { }
    public static interface R3 { }

    @EqualsAndHashCode
    public static class Context1 { }

    @EqualsAndHashCode
    public static class Context2 { }

    @EqualsAndHashCode
    public static class RI1A implements R1 {}

    @EqualsAndHashCode @ToString
    public static class RI1B implements R1
      {
        private Bean1 bean1;

        private Bean2 bean2;

        public RI1B (final Bean1 bean1)
          {
            this.bean1 = bean1;
          }

        public RI1B (final Bean2 bean2)
          {
            this.bean2 = bean2;
          }
      }

    @EqualsAndHashCode
    public static class RI1C implements R1 {}

    @EqualsAndHashCode
    public static class RI2A implements R2 {}

    @AllArgsConstructor @EqualsAndHashCode @ToString
    public static class RI2B implements R2
      {
        private final Bean1 bean1;

        private final Bean2 bean2;
      }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class RI2C implements R2
      {
        private final Context2 context;
        private final Bean1 bean1;
      }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class RI3A implements R3
      {
        private final Object owner;
      }

    @EqualsAndHashCode
    public static class RI3B implements R3 {}

    @EqualsAndHashCode
    public static class RI3C implements R3 {}

    public static class Bean1 {}
    public static class Bean2 {}
  }
