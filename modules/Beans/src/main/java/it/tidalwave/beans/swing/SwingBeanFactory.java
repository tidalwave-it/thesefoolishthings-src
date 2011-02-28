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
 *
 **********************************************************************************************************************/
package it.tidalwave.beans.swing;

import it.tidalwave.beans.JavaBeanEnhancer;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class SwingBeanFactory<Bean>
  {
    private static final JavaBeanEnhancer JAVA_BEAN_ENHANCER = new JavaBeanEnhancer();
    
    private final Bean bean;
    
    /***************************************************************************
     *
     *
     **************************************************************************/
    public SwingBeanFactory (final Class<Bean> beanClass)
      {
        try 
          {
            bean = (Bean)JAVA_BEAN_ENHANCER.createEnhancedBean(beanClass.newInstance(), JavaBeanEnhancer.EDT_COMPLIANT);
          } 
        catch (InstantiationException e) 
          {
            throw new IllegalArgumentException(e);
          }
        catch (IllegalAccessException e) 
          {
            throw new IllegalArgumentException(e);
          }
      }
    
    /***************************************************************************
     *
     *
     **************************************************************************/
    public final Bean getBean()
      {
        return bean;    
      }
  }
