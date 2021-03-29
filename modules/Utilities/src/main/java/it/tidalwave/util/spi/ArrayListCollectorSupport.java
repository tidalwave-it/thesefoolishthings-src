/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/it-tidalwave-util
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
package it.tidalwave.util.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

/***********************************************************************************************************************
 *
 * A support {@link Collector} which uses an {@link ArrayList} as the accumulator.
 * 
 * @param  <COLLECTED_TYPE>     the type of collected items
 * @param  <COLLECTING_TYPE>    the type of collecting item
 * 
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public abstract class ArrayListCollectorSupport<COLLECTED_TYPE, COLLECTING_TYPE> 
        implements Collector<COLLECTED_TYPE, List<COLLECTED_TYPE>, COLLECTING_TYPE>
  {
    @Override @Nonnull 
    public Supplier<List<COLLECTED_TYPE>> supplier() 
      {
        return ArrayList::new;
      } 

    @Override @Nonnull 
    public BiConsumer<List<COLLECTED_TYPE>, COLLECTED_TYPE> accumulator() 
      {
        return List::add;
      }

    @Override @Nonnull 
    public BinaryOperator<List<COLLECTED_TYPE>> combiner() 
      {
        return (left, right) -> { left.addAll(right); return left; };
      }

    @Override @Nonnull 
    public Set<Characteristics> characteristics() 
      {
        // Not CONCURRENT since ArrayList is not thread-safe
        return Collections.emptySet();
      }
  }
