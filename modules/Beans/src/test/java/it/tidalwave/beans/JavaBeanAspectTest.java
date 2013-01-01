/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
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
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.beans;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.beans.mock.CompositeMockItem;
import java.io.Serializable;
import java.util.logging.Logger;

class PropertyChangeListenerMock implements PropertyChangeListener
  {
    public List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
    
    public void propertyChange (final PropertyChangeEvent event)  
      {
        events.add(event);
      }
  }

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class JavaBeanAspectTest extends TestSupport
  {
    private final static String CLASS = JavaBeanAspectTest.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);
    
    private CompositeMockItem bean;
    private CompositeMockItem enhancedBean;
    
    @BeforeMethod
    public void setup() 
      {
        logger.info("******** setup()");
        final JavaBeanEnhancer enhancer = new JavaBeanEnhancer();
        bean = new CompositeMockItem();
        enhancedBean = (CompositeMockItem)enhancer.createEnhancedBean(bean);
        logger.info("******** setup() completed");
      }

    @AfterMethod
    public void tearDown() 
      {
        bean = null;
        enhancedBean = null;
      }

    @Test
    public void testEnhancementWorks()
      {
        logger.info("******** testEnhancementWorks()");
        AssertJUnit.assertTrue(enhancedBean instanceof JavaBean);
        AssertJUnit.assertTrue(enhancedBean.getItem1() instanceof JavaBean);
        AssertJUnit.assertTrue(enhancedBean.getItem2() instanceof JavaBean);
        AssertJUnit.assertTrue(enhancedBean instanceof Serializable); // TODO: you should also try to serialize it to check other problems
        // TODO: this test should fail if JavaBeanAspect doesn't use IdentityHashMap
        // I tried with forcing MockItem1.equals() to return true, but it doesn't fail...
      }
    
    @Test
    public void testCorrectEventSource() 
      throws Exception 
      {
        logger.info("******** testCorrectEventSource()");
        final PropertyChangeListenerMock listener = new PropertyChangeListenerMock();
        ((JavaBean)enhancedBean).addPropertyChangeListener(listener);
        enhancedBean.setName("new name");
        AssertJUnit.assertFalse(listener.events.isEmpty());
        AssertJUnit.assertTrue(enhancedBean == listener.events.get(0).getSource());
      }
    
    // TODO test events - at the moment this is covered by tests of MetadataItemEnhancer,
    // but you must put tests here when this class is spinned off to OpenBlueSky.
  }