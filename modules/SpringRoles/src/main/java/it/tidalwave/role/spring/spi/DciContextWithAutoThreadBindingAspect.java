/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone https://bitbucket.org/tidalwave/northernwind-src.git
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
package it.tidalwave.role.spring.spi;

import javax.annotation.Nonnull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import it.tidalwave.util.Task;
import it.tidalwave.role.ContextManager;
import it.tidalwave.dci.annotation.DciContext;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.spi.impl.LogUtil.shortId;

/***********************************************************************************************************************
 *
 * An aspect which implements {@link DciContext} with {@code autoThreadBinding=true}. This class must not be used
 * directly. Instead, add the library which contains it as a depenency of your project and make it visible to the
 * AspectJ compiler.
 *
 * @author  Fabrizio Giudici
 * @since   3.0
 *
 **********************************************************************************************************************/
@Aspect @Slf4j
public class DciContextWithAutoThreadBindingAspect
  {
    @Around("within(@it.tidalwave.dci.annotation.DciContext *) && execution(* *(..))")
    public Object advice (final @Nonnull ProceedingJoinPoint pjp)
      throws Throwable
      {
        final Object context = pjp.getTarget();

        if (!context.getClass().getAnnotation(DciContext.class).autoThreadBinding())
          {
            return pjp.proceed();
          }
        else
          {
            if (log.isTraceEnabled())
              {
                log.trace("executing {}.{}() with context thread binding", shortId(context), pjp.getSignature().getName());
              }

            // It looks like the @Inject approach creates bogus multiple instance of ContextManager
            final ContextManager contextManager = ContextManager.Locator.find();
            return contextManager.runWithContext(context, new Task<Object, Throwable>()
              {
                @Override
                public Object run()
                  throws Throwable
                  {
                    return pjp.proceed();
                  }
              });
          }
      }
  }