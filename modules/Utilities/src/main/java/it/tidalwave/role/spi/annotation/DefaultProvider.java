package it.tidalwave.role.spi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************************************************************************
 *
 * Demarcates a provider as a default implementation, so it can be eventually overridden.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultProvider
  {
  }
