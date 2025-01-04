/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.actor.impl;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnegative;
import jakarta.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import it.tidalwave.actor.spi.ActorActivatorStats;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.messagebus.spi.ReflectionUtils.*;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
@Slf4j
public class MBeansManager
  {
    @Nonnull
    private final Object actorObject;

    @Nonnull @Getter
    private final ActorActivatorStats stats;

    private ObjectName statsName;

    private final Map<ObjectName, Object> mbeansMapByName = new HashMap<>();

    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    public MBeansManager (@Nonnull final Object actorObject, @Nonnegative final int poolSize)
      {
        this.actorObject = actorObject;
        stats = new ActorActivatorStats(poolSize);
      }

    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    public void register()
      {
        forEachMethodInTopDownHierarchy(actorObject, new MethodProcessorSupport()
          {
            @Override @Nonnull
            public FilterResult filter (@Nonnull final Class<?> clazz)
              {
                mbeansMapByName.putAll(getMBeans(actorObject, clazz));
                return FilterResult.IGNORE;
              }
          });

        final var mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try
          {
            final var name = String.format("%s:type=%s", actorObject.getClass().getPackage().getName(),
                                           actorObject.getClass().getSimpleName());
            statsName = new ObjectName(name);
            mBeanServer.registerMBean(stats, statsName);
          }
        catch (InstanceAlreadyExistsException | MalformedObjectNameException | NotCompliantMBeanException | MBeanRegistrationException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }

        for (final var entry : mbeansMapByName.entrySet())
          {
            try
              {
                log.info(">>>> registering MBean {}", entry);
                mBeanServer.registerMBean(entry.getValue(), entry.getKey());
              }
            catch (InstanceAlreadyExistsException | NotCompliantMBeanException | MBeanRegistrationException e)
              {
                log.error("Cannot register MBean: " + entry, e);
              }
          }
      }

    /***********************************************************************************************************************************************************
     * Unregisters the MBeans.
     **********************************************************************************************************************************************************/
    public void unregister()
      {
        final var mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try
          {
            mBeanServer.unregisterMBean(statsName);
          }
        catch (InstanceNotFoundException | MBeanRegistrationException e)
          {
            log.error("Cannot register master MBean for actor " + actorObject, e);
          }

        for (final var entry : mbeansMapByName.entrySet())
          {
            try
              {
                log.info(">>>> unregistering MBean {}", entry);
                mBeanServer.unregisterMBean(entry.getKey());
              }
            catch (InstanceNotFoundException | MBeanRegistrationException e)
              {
                log.error("Cannot unregister MBean: " + entry, e);
              }
          }
      }

    /***********************************************************************************************************************************************************
     *
     **********************************************************************************************************************************************************/
    @Nonnull
    private static Map<ObjectName, Object> getMBeans (@Nonnull final Object actorObject, @Nonnull final Class<?> clazz)
      {
        final Map<ObjectName, Object> result = new HashMap<>();

        for (final var field : clazz.getDeclaredFields())
          {
            if (!field.isSynthetic() && ((field.getModifiers() & Modifier.STATIC) == 0))
              {
                try
                  {
                    field.setAccessible(true);
                    final var value = field.get(actorObject);

                    if (value != null)
                      {
                        final var interfaces = value.getClass().getInterfaces();
                        //
                        // TODO: it checks only first interface - what about an annotation?
                        //
                        if ((interfaces.length > 0) && interfaces[0].getName().endsWith("MBean"))
                          {
                            final var name = String.format("%s:type=%s", clazz.getPackage().getName(),
                                                           value.getClass().getSimpleName());
                            result.put(new ObjectName(name), value);
                          }
                      }
                  }
                catch (IllegalArgumentException | MalformedObjectNameException | IllegalAccessException e)
                  {
                    log.error("Cannot handle object: {}", field);
                  }
              }
          }

        return result;
      }
  }
