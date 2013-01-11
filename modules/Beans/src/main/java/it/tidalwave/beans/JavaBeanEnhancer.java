/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.beans;

import net.sf.cglib.proxy.Callback;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 ******************************************************************************/
public class JavaBeanEnhancer extends AbstractEnhancer<JavaBean>
  {
    public final static Object EDT_COMPLIANT = "EDT_Compliant";

    public JavaBeanEnhancer()
      {
        super(JavaBean.class);
      }

    protected Callback createInterceptor (final Object bean, final Object ... arguments)
      {
        return new JavaBeanAspect(bean, this, arguments);
      }
  }
