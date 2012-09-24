/***********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://solidblue.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/solidblue-src
 *
 **********************************************************************************************************************/
package it.tidalwave.actor.spi;

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
 * @stereotype Actor
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
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
     * 
     * 
     ******************************************************************************************************************/
    public static ActorActivator activatorFor (final @Nonnull Class<?> actorClass)
      {
        return new ActorActivator(actorClass, 1);
      }
            
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    public ActorActivator withPoolSize (@Nonnegative int poolSize)
      {
        return new ActorActivator(actorClass, poolSize);
      }
            
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    private ActorActivator (final @Nonnull Class<?> actorClass, final @Nonnegative int poolSize)
      {
        this.actorClass = actorClass;
        this.poolSize = poolSize;
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    public void initialize() 
      {        
        try
          {
            final Actor actor = actorClass.getAnnotation(Actor.class);
            validate(actor);
            actorObject = actorClass.newInstance();
            executor = new ExecutorWithPriority(poolSize, actorClass.getSimpleName(), actor.initialPriority());
            mBeansManager = new MBeansManager(actorObject, poolSize);
            messageBusAdapter = new CollaborationAwareMessageBusAdapter(actorObject, executor, mBeansManager.getStats());
          }
        catch (InstantiationException e)
          { 
            throw new RuntimeException(e);  
          }
        catch (IllegalAccessException e)
          { 
            throw new RuntimeException(e);  
          }
        
        forEachMethodInTopDownHierarchy(actorObject, messageBusAdapter);
        forEachMethodInTopDownHierarchy(actorObject, new PostConstructInvoker(actorObject));        
        mBeansManager.register();
      }
    
    /*******************************************************************************************************************
     * 
     * 
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
    private void validate (final @Nonnull Actor actor) 
      {
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
