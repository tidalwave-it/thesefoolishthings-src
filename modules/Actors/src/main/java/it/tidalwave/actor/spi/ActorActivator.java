/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.actor.spi;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.impl.CollaborationAwareMessageBusAdapter;
import it.tidalwave.actor.impl.ExecutorWithPriority;
import it.tidalwave.actor.impl.MBeansManager;
import it.tidalwave.actor.impl.PostConstructInvoker;
import it.tidalwave.actor.impl.PreDestroyInvoker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.messagebus.spi.ReflectionUtils.*;

/***********************************************************************************************************************
 *
 * This class is used to activate and deactivate an actor.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class ActorActivator
  {
    @Nonnull
    private final Class<?> actorClass;

    @Nonnegative @Getter
    private final int poolSize;

    @Getter
    private Object actorObject;

    private ExecutorWithPriority executor;

    private CollaborationAwareMessageBusAdapter messageBusAdapter;

    private MBeansManager mBeansManager;

    /*******************************************************************************************************************
     *
     * Creates an instance for the given actor class.
     *
     * @param  actorClass   the actor class
     * @return              the instance
     *
     ******************************************************************************************************************/
    public static ActorActivator activatorFor (@Nonnull final Class<?> actorClass)
      {
        return new ActorActivator(actorClass, 1);
      }

    /*******************************************************************************************************************
     *
     * Specifies the pool size for this activator.
     *
     * @param  poolSize    the pool size
     * @return             the activator
     *
     ******************************************************************************************************************/
    @Nonnull
    public ActorActivator withPoolSize (@Nonnegative final int poolSize)
      {
        return new ActorActivator(actorClass, poolSize);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private ActorActivator (@Nonnull final Class<?> actorClass, @Nonnegative final int poolSize)
      {
        this.actorClass = actorClass;
        this.poolSize = poolSize;
      }

    /*******************************************************************************************************************
     *
     * Activates the managed actor.
     *
     ******************************************************************************************************************/
    public void initialize()
      {
        try
          {
            final var actor = actorClass.getAnnotation(Actor.class);
            validate(actor);
            actorObject = actorClass.getDeclaredConstructor().newInstance();
            executor = new ExecutorWithPriority(poolSize, actorClass.getSimpleName(), actor.initialPriority());
            mBeansManager = new MBeansManager(actorObject, poolSize);
            messageBusAdapter = new CollaborationAwareMessageBusAdapter(actorObject, executor, mBeansManager.getStats());
          }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
          {
            throw new RuntimeException(e);
          }

        forEachMethodInTopDownHierarchy(actorObject, messageBusAdapter);
        forEachMethodInTopDownHierarchy(actorObject, new PostConstructInvoker(actorObject));
        mBeansManager.register();
      }

    /*******************************************************************************************************************
     *
     * Deactivates the managed actor and releases resources.
     *
     ******************************************************************************************************************/
    public void dispose()
      {
        mBeansManager.unregister();
        forEachMethodInBottomUpHierarchy(actorObject, new PreDestroyInvoker(actorObject));
        messageBusAdapter.unsubscribe();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void validate (@Nonnull final Actor actor)
      {
        //noinspection ConstantConditions
        if (actor == null)
          {
            throw new IllegalArgumentException("Actor class must be annotated with @Actor: " + actorClass);
          }

        if (!actor.threadSafe() && (poolSize != 1))
          {
            throw new IllegalArgumentException("Actors that aren't thread safe can't have pool size > 1");
          }
      }
  }
