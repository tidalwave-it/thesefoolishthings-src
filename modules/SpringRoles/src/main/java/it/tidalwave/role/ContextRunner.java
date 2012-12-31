/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.role;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ContextRunner 
  {
    public static interface Callable<V, T extends Throwable>
      {
        public <V> V run()
          throws T;
      }
    
    public static interface SimpleCallable extends Callable<Void, RuntimeException>
      {       
      }
    
    private final static List<Object> globalContexts = new ArrayList<Object>();
    
    private final static Stack<Object> localContexts = new Stack<Object>();
    
    private final static ThreadLocal<Object> currentContext = new ThreadLocal<Object>();
    
    @Nonnull
    public static List<Object> getContexts()
      {
        final List<Object> contexts = new ArrayList<Object>();
        contexts.addAll(localContexts);
        Collections.reverse(contexts);
        contexts.addAll(0, globalContexts);
        return contexts;
      }
    
    public static void addGlobalContext (final @Nonnull Object context)
      {
        globalContexts.add(context);
      }
    
    public static <V, T extends Throwable> V runInContext (final @Nonnull Object context, final @Nonnull Callable<V, T> callable)
      throws T
      {
        try
          {
            localContexts.push(context);
            currentContext.set(context);
            
            return callable.run();        
          }
        finally
          {
            localContexts.pop();
            currentContext.set(localContexts.peek());
          }
      }
  }
