/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings - MessageBus
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
package it.tidalwave.messagebus.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************************************************************************
 *
 * This annotation is used to write simpler code which uses a message bus. With the proper support, code that receives
 * messages can be written as simple methods such as:
 *
 * <pre>
 * private void onMessage (final @ListenTo Message message)
 *   {
 *     ...
 *   }
 * </pre>
 *
 * Note that this annotation doesn't support any further semantics; for instance, <code>message</code> could be null or
 * not, in function of the message bus implementation; nor you can make any assumption on the threading. The exact
 * semantics are defined by further annotations (on the class containing the listener method), or by the build context
 * (e.g. an annotation processor, AspectJ, etc...).
 *
 * Specific support must be used in order to have this annotation working. For instance, an annotation processor, or
 * some facility which scans classes at runtime, or {@link it.tidalwave.messagebus.MessageBusHelper}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface ListensTo
  {
  }
