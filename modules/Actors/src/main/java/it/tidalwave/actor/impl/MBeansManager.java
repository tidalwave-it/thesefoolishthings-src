/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - Actors
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
package it.tidalwave.actor.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nonnegative;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import it.tidalwave.actor.spi.ActorActivatorStats;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import static it.tidalwave.messagebus.spi.ReflectionUtils.*;
import it.tidalwave.messagebus.spi.ReflectionUtils.MethodProcessor.FilterResult;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class MBeansManager
  {
    @Nonnull
    private final Object actorObject;

    @Nonnull @Getter
    private final ActorActivatorStats stats;

    private ObjectName statsName;

    private final Map<ObjectName, Object> mbeansMapByName = new HashMap<ObjectName, Object>();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public MBeansManager (final @Nonnull Object actorObject, final @Nonnegative int poolSize)
      {
        this.actorObject = actorObject;
        stats = new ActorActivatorStats(poolSize);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void register()
      {
        forEachMethodInTopDownHierarchy(actorObject, new MethodProcessorSupport()
          {
            @Override @Nonnull
            public FilterResult filter (final @Nonnull Class<?> clazz)
              {
                mbeansMapByName.putAll(getMBeans(actorObject, clazz));
                return FilterResult.IGNORE;
              }
          });

        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try
          {
            final String name = String.format("%s:type=%s", actorObject.getClass().getPackage().getName(),
                                                            actorObject.getClass().getSimpleName());
            statsName = new ObjectName(name);
            mBeanServer.registerMBean(stats, statsName);
          }
        catch (InstanceAlreadyExistsException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }
        catch (MalformedObjectNameException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }
        catch (MBeanRegistrationException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }
        catch (NotCompliantMBeanException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }

        for (final Map.Entry<ObjectName, Object> entry : mbeansMapByName.entrySet())
          {
            try
              {
                log.info(">>>> registering MBean {}", entry);
                mBeanServer.registerMBean(entry.getValue(), entry.getKey());
              }
            catch (InstanceAlreadyExistsException e)
              {
                log.error("Cannot register MBean: " + entry, e);
              }
            catch (MBeanRegistrationException e)
              {
                log.error("Cannot register MBean: " + entry, e);
              }
            catch (NotCompliantMBeanException e)
              {
                log.error("Cannot register MBean: " + entry, e);
              }
          }
      }

    /*******************************************************************************************************************
     *
     * Unregisters the MBeans.
     *
     ******************************************************************************************************************/
    public void unregister()
      {
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try
          {
            mBeanServer.unregisterMBean(statsName);
          }
        catch (InstanceNotFoundException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }
        catch (MBeanRegistrationException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }

        for (final Map.Entry<ObjectName, Object> entry : mbeansMapByName.entrySet())
          {
            try
              {
                log.info(">>>> unregistering MBean {}", entry);
                mBeanServer.unregisterMBean(entry.getKey());
              }
            catch (InstanceNotFoundException e)
              {
                log.error("Cannot unregister MBean: " + entry, e);
              }
            catch (MBeanRegistrationException e)
              {
                log.error("Cannot unregister MBean: " + entry, e);
              }
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Map<ObjectName, Object> getMBeans (final @Nonnull Object actorObject, final @Nonnull Class<?> clazz)
      {
        final Map<ObjectName, Object> result = new HashMap<ObjectName, Object>();

        for (final Field field : clazz.getDeclaredFields())
          {
            if (!field.isSynthetic() && ((field.getModifiers() & Modifier.STATIC) == 0))
              {
                try
                  {
                    field.setAccessible(true);
                    final Object value = field.get(actorObject);

                    if (value != null)
                      {
                        final Class<?>[] interfaces = value.getClass().getInterfaces();
                        //
                        // TODO: it checks only first interface - what about an annotation?
                        //
                        if ((interfaces.length > 0) && interfaces[0].getName().endsWith("MBean"))
                          {
                            final String name = String.format("%s:type=%s", clazz.getPackage().getName(),
                                                                            value.getClass().getSimpleName());
                            result.put(new ObjectName(name), value);
                          }
                      }
                  }
                catch (IllegalArgumentException e)
                  {
                    log.error("Cannot handle object: {}", field);
                  }
                catch (IllegalAccessException e)
                  {
                    log.error("Cannot handle object: {}", field);
                  }
                catch (MalformedObjectNameException e)
                  {
                    log.error("Cannot handle object: {}", field);
                  }
              }
          }

        return result;
      }
  }
