/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id$
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.actor.impl;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import it.tidalwave.messagebus.spi.ReflectionUtils;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class PostConstructInvoker extends ReflectionUtils.MethodProcessorSupport
  {
    @Nonnull
    private final Object object;

    @Override
    public void process (final @Nonnull Method method)
      {
        if (method.getAnnotation(PostConstruct.class) != null)
          {
            try
              {
                method.invoke(object);
              }
            catch (IllegalAccessException e)
              {
                throw new RuntimeException(e);
              }
            catch (IllegalArgumentException e)
              {
                throw new RuntimeException(e);
              }
            catch (InvocationTargetException e)
              {
                throw new RuntimeException(e);
              }
          }
      }
  }
