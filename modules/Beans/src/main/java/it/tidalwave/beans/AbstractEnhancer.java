/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.beans;

import it.tidalwave.util.logging.Logger;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import org.jdesktop.beansbinding.Property;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

/***********************************************************************************************************************
 *
 * Thanks to pupmonster@dev.java.net, jarppe2@dev.java.net for contributing the
 * base code.
 * 
 * @author  pupmonster@dev.java.net
 * @author  jarppe2@dev.java.net
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 **********************************************************************************************************************/
public abstract class AbstractEnhancer<T> 
  {
    private final static String CLASS = AbstractEnhancer.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);
    
    private final Class<T> interfaces;
    
    public static interface PropertyFilter 
      {
        public boolean accept (PropertyDescriptor propertyDescriptor, Property property);
    
        public static final PropertyFilter DEFAULT_FILTER = new PropertyFilter() 
          {
            public boolean accept (final PropertyDescriptor propertyDescriptor, final Property property) 
              {
                final Class<?> propertyType = propertyDescriptor.getPropertyType();
                return (!propertyType.isPrimitive() && !propertyType.isArray() && !propertyType.getName().startsWith("java"));
              }
          }; 
      }

    public AbstractEnhancer (final Class<T> interfaces) 
      {
        if (interfaces == null)
          {
            throw new IllegalArgumentException("Interface is mandatory");
          }
        
        if (!interfaces.isInterface())
          {
            throw new IllegalArgumentException("Only interfaces allowed here");
          }
        
        this.interfaces = interfaces;
      }
  
    public T createEnhancedBean (final Object bean, final Object ... arguments)
      {
        logger.fine("createEnhancedItem(%s, %s) - interfaces: %s", bean, Arrays.asList(arguments), interfaces);
        final long time = System.currentTimeMillis();
        final Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(Thread.currentThread().getContextClassLoader());
        enhancer.setSuperclass(bean.getClass());
        enhancer.setInterfaces(new Class[] {interfaces});
        enhancer.setCallback(createInterceptor(bean, arguments));
        final T result = (T)enhancer.create();
        logger.finer(">>>> created decorator %s in %d msec", result, (int)(System.currentTimeMillis() - time));
        return result;
      }
    
    protected abstract Callback createInterceptor (final Object bean, final Object ... arguments);
  }
         