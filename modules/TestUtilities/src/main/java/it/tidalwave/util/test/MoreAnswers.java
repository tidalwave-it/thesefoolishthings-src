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
package it.tidalwave.util.test;

import lombok.experimental.UtilityClass;
import org.mockito.stubbing.Answer;
import static org.mockito.Answers.RETURNS_DEFAULTS;

/***********************************************************************************************************************
 *
 * A repository of reusable {@link Answer}s for Mockito.
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-8
 *
 **********************************************************************************************************************/
@UtilityClass
public class MoreAnswers
  {
    /*******************************************************************************************************************
     *
     * An {@link Answer} that calls default methods of an interface. To be used as: {@code Foo foo = mock(Foo.class,
     * MoreAnswers.CALLS_DEFAULT_METNODS);}.
     *
     * Mockito's {@link org.mockito.Answers#CALLS_REAL_METHODS} actually behaves the same when used with interfaces,
     * but it has been designed for mixed mocking, which is a practice that is supposed to be used only with corner
     * cases an legacy stuff.
     *
     * @it.tidalwave.javadoc.experimental
     *
     ******************************************************************************************************************/
    public static final Answer<?> CALLS_DEFAULT_METHODS = invocation ->
            invocation.getMethod().isDefault() ? invocation.callRealMethod()
                                               : RETURNS_DEFAULTS.answer(invocation);
  }
