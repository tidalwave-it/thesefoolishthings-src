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
package it.tidalwave.dci.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * Designates a DCI role implementation for a given owner object.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DciRole
  {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class NoContext
      {
      }

    /*******************************************************************************************************************
     *
     * The datum type to which this role can be associated. Multiple data types can be specified.
     *
     * @return    the datum type
     *
     ******************************************************************************************************************/
    @Nonnull
    public Class<?>[] datumType();

    /*******************************************************************************************************************
     *
     * The context type to which this role is restricted. If no context is specified, this role will be always
     * associated.
     *
     * @return    the context type
     *
     ******************************************************************************************************************/
    public Class<?> context() default NoContext.class;
  }
