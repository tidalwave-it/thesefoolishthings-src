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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.VetoableChangeSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.SwingUtilities;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Property;
import it.tidalwave.beans.AbstractEnhancer.PropertyFilter;
import it.tidalwave.util.logging.Logger;

/*******************************************************************************
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
 ******************************************************************************/
public class JavaBeanAspect 
  extends JavaBeanSupport
  implements MethodInterceptor, JavaBean 
  {
    private final static String CLASS = JavaBeanAspect.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);
    
    private static final String SET_PREFIX = "set";
    
    private static final List<Method> javaBeanMethods = Arrays.asList(JavaBean.class.getMethods());

    protected final Object bean;
    
    /** A map used for replacing inner enhanced beans. Note that an IdentityHashMap 
     *  is mandatory here, as we don't want to compare them through equals(). */
    private final Map<Object, JavaBean> enhancedObjectMap = new IdentityHashMap<Object, JavaBean>();
    
    /** True of setter methods must be invoked in the EventDispatcherThread. */
    private final boolean edtCompliant;
    
    /***************************************************************************
     *
     * 
     **************************************************************************/
    public JavaBeanAspect (final Object bean, 
                           final AbstractEnhancer<?> enhancer,
                           final Object ... arguments)
      {
        this.bean = bean;
        fixContainedBeans(bean, enhancer, arguments);
        this.edtCompliant = Arrays.asList(arguments).contains(JavaBeanEnhancer.EDT_COMPLIANT);
      }
    
    /***************************************************************************
     *
     * 
     **************************************************************************/
    public Object intercept (final Object object, 
                             final Method method, 
                             final Object[] methodParameters, 
                             final MethodProxy proxy) 
      throws Throwable 
      {
        // This ensures that the event source is the decorator, not the original bean
        // Also, it restores stuff after a serialization (change support is volatile)
        synchronized (this)
          {
            if (propertyChangeSupport == null)
              {
                propertyChangeSupport = new PropertyChangeSupport(object);
                vetoableChangeSupport = new VetoableChangeSupport(object);
              }
          }
        
//        System.err.println("$$$$$$$$$$$ " + method);
//        if (method.getName().equals("toString") && (methodParameters.length == 0))
//          {
//            return String.format("%s[%s]", object, bean.toString());
//          }
        
        if (javaBeanMethods.contains(method)) 
          {
            return method.invoke(this, methodParameters);
          }  

        if (method.getName().equals("equals") && (methodParameters.length == 1))
          {
            return doEquals(methodParameters[0]);
          }
        
        if (!method.getName().startsWith(SET_PREFIX) || (methodParameters.length != 1)) 
          {
            return doGenericMethod(method, methodParameters);
          }

        return doSetter(object, methodParameters, method);
      }

    /***************************************************************************
     *
     * 
     **************************************************************************/
    public Object __getDelegate()
      {
        return bean;
      }
    
    /***************************************************************************
     *
     * 
     **************************************************************************/
    protected static String getPropertyName (final Method method)
      {
        return Introspector.decapitalize(method.getName().substring(SET_PREFIX.length()));
      }
    
    /***************************************************************************
     *
     * 
     **************************************************************************/
    protected Collection<JavaBean> getEnhancedObjects()
      {
        return new CopyOnWriteArraySet<JavaBean>(enhancedObjectMap.values());
      }

    /***************************************************************************
     *
     * 
     **************************************************************************/
    private Object doEquals (Object other) 
      {
        if (other instanceof JavaBean) 
          {
            other = ((JavaBean)other).__getDelegate();
          }

        return bean.equals(other);
      }  

    /***************************************************************************
     *
     * 
     **************************************************************************/
    private Object doGenericMethod (final Method method, final Object[] methodParameters)
      throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
      {
        //logger.finest(String.format("doGenericMethod(%s, %s)", method, Arrays.asList(methodParameters)));
        
        // FIXME: or proxy.invokeSuper()?
        // "The original method may either be invoked by normal reflection using the Method object, or by using the MethodProxy (faster)."
        Object result = method.invoke(bean, methodParameters);

        if (result != null) 
          {
            Object enhancedObject = enhancedObjectMap.get(result);

            if (enhancedObject != null) 
              {
                logger.finest(String.format(">>>> replacing result: %s -> %s", result, enhancedObject));
                result = enhancedObject;
              }
          }

        return result;
      }

    /***************************************************************************
     *
     * 
     **************************************************************************/
    private Object doSetter (final Object object, 
                             final Object[] methodParameters, 
                             final Method method) 
      throws InvocationTargetException, IllegalArgumentException, IllegalAccessException 
      {
        if (!edtCompliant || SwingUtilities.isEventDispatchThread())
          {
            return doSetterInEDT(object, methodParameters, method);
          }
        else
          {
            final Object[] result = new Object[1];
            
            try   
              {
                SwingUtilities.invokeAndWait(new Runnable() 
                  {
                    public void run() 
                      {
                        try
                          {
                            result[0] = doSetterInEDT(object, methodParameters, method);
                          }
                        catch (Exception e)
                          {
                            logger.throwing(CLASS, "doSetterInEDT()", e);  
                          }
                      }
                  });
                  
                return result[0];
              } 
            catch (InterruptedException e)  
              {
                throw new RuntimeException(e);
              } 
            catch (InvocationTargetException e) 
              {
                throw new RuntimeException(e);
              }
          }
      }
    
    /***************************************************************************
     *
     * 
     **************************************************************************/
    private Object doSetterInEDT (final Object object, 
                                  final Object[] methodParameters, 
                                  final Method method) 
      throws InvocationTargetException, IllegalArgumentException, IllegalAccessException 
      {
        final String propertyName = getPropertyName(method);
        final Property property = BeanProperty.create(Introspector.decapitalize(propertyName));
        final Object oldValue = property.getValue(object);

        Object returnValue = method.invoke(bean, methodParameters); // FIXME: or proxy.invokeSuper()?
        
        if (returnValue == bean) 
          {
            returnValue = object;
          }

        logger.finest(String.format(">>>> %s - %s changed : %s -> %s", object, propertyName, oldValue, methodParameters[0]));
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, methodParameters[0]);

        return returnValue;
      }
            
    /***************************************************************************
     *
     * 
     **************************************************************************/
    private void fixContainedBeans (final Object bean, 
                                    final AbstractEnhancer<?> enhancer,
                                    final Object ... arguments) 
      throws RuntimeException 
      {
        final PropertyDescriptor[] propertyDescriptors;

        try 
          {
            propertyDescriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
          }
        catch (IntrospectionException e)
          {
            throw new RuntimeException(e);
          }

        PropertyFilter filter = PropertyFilter.DEFAULT_FILTER;
        
        for (final Object argument : arguments)
          {
            if (argument instanceof AbstractEnhancer.PropertyFilter)
              {
                filter = (PropertyFilter)argument;
                break;
              }
          }
        
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) 
          {
            final String propertyName = propertyDescriptor.getName();
            final Class<?> propertyType = propertyDescriptor.getPropertyType();
            final Property property = BeanProperty.create(propertyName);

            if (property.isReadable(bean) && !property.isWriteable(bean) && filter.accept(propertyDescriptor, property))
              {
                logger.fine(String.format(">>>> enhancing property %s type %s", propertyName, propertyType));
                final Object object = property.getValue(bean);

                if (object != null)
                  {
                    enhancedObjectMap.put(object, (JavaBean)enhancer.createEnhancedBean(object, arguments));
                  }
              }
          }
      }
  }
