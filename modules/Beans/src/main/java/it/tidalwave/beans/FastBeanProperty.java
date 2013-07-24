/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateListener;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 * @experimental
 *
 ******************************************************************************/
public class FastBeanProperty<S,V> extends Property<S,V>
  {
    private static final Map<String, Method> setterCache = new HashMap<String, Method>();
    private static final Map<String, Method> getterCache = new HashMap<String, Method>();

    private final String name;

    public static <S, V> FastBeanProperty create (final String name)
      {
        return new FastBeanProperty<S, V>(name);
      }

    private FastBeanProperty (final String name)
      {
        this.name = name;
      }

    @Override
    public Class<? extends V> getWriteType (final S bean)
      {
        return (Class<? extends V>)getGetter(bean).getReturnType();
      }

    @Override
    public V getValue (final S bean)
      {
        try
          {
            return (V)getGetter(bean).invoke(bean);
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

    @Override
    public void setValue (final S bean, final V value)
      {
        try
          {
            getSetter(bean).invoke(bean, value);
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

    @Override
    public boolean isReadable (final S bean)
      {
        try
          {
            getGetter(bean);
            return true;
          }
        catch (Exception e)
          {
            return false;
          }
      }

    @Override
    public boolean isWriteable (final S bean)
      {
        try
          {
            getSetter(bean);
            return true;
          }
        catch (Exception e)
          {
            return false;
          }
      }

    @Override
    public void addPropertyStateListener (final S arg0, final PropertyStateListener arg1)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void removePropertyStateListener (final S arg0, final PropertyStateListener arg1)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public PropertyStateListener[] getPropertyStateListeners (final S arg0)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    private Method getGetter (final S bean)
      {
        final String key = bean.getClass().getName() + "." + name;
        Method getter = getterCache.get(key);

        if (getter == null)
          {
            try
              {
                getter = bean.getClass().getMethod("get" + capitalized(name));
              }
            catch (Exception e)
              {
                try
                  {
                    getter = bean.getClass().getMethod("is" + capitalized(name));
                  }
                catch (Exception e2)
                  {
                    throw new RuntimeException(e2);
                  }
              }

            getterCache.put(key, getter);
          }

        return getter;
      }

    private Method getSetter (final S bean)
      {
        final String key = bean.getClass().getName() + "." + name;
        Method setter = setterCache.get(key);

        if (setter == null)
          {
            try
              {
                setter = bean.getClass().getMethod("set" + capitalized(name), getWriteType(bean));
                setterCache.put(key, setter);
              }
            catch (Exception e)
              {
                throw new RuntimeException(e);
              }
          }

        return setter;
      }

    private static String capitalized (final String string)
      {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
      }
 }
