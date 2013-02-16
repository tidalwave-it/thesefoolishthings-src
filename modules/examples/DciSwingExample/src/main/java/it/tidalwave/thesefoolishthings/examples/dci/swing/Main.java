/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings Examples - DCI Swing
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
package it.tidalwave.thesefoolishthings.examples.dci.swing;

import javax.annotation.Nonnull;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.BeanFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class Main
  {
    public static void main (final @Nonnull String ... args)
      throws Exception
      {
        final String beans = "it/tidalwave/thesefoolishthings/examples/dci/swing/Beans.xml";
        final BeanFactory context = new ClassPathXmlApplicationContext(beans);
        context.getBean(SwingDciExample.class).run();
      }
  }
