/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tidalwave.util;

import javax.annotation.Nonnull;

/**
 *
 * @author fritz
 */
public abstract class SimpleFinderSupport<Type> extends FinderSupport<Type, SimpleFinder<Type>> implements SimpleFinder<Type> 
  {
    public SimpleFinderSupport (final @Nonnull String name) 
      {
        super(name);
      }
  }
