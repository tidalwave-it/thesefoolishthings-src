/***********************************************************************************************************************
 *
 * TheseFoolishThings - Miscellaneous utilities
 * ============================================
 *
 * Copyright (C) 2009-2010 by Tidalwave s.a.s.
 * Project home page: http://thesefoolishthings.kenai.com
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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.swing.beansbinding;

import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 ******************************************************************************/
public class AbstractBindablePane<Bean> extends JPanel
  {
    private BindingGroup bindingGroup;
    private Field beanField;
    
    /***************************************************************************
     *
     *
     **************************************************************************/
    public void initialize (final String beanFieldName, final BindingGroup bindingGroup)
      {
        this.bindingGroup = bindingGroup;
        
        try
          {
            beanField = getClass().getDeclaredField(beanFieldName);
          }
        catch (Exception e)
          {
            throw new ExceptionInInitializerError(e);
          }
      }
    
    /***************************************************************************
     *
     *
     **************************************************************************/
    final public void bind (final Bean bean) 
      {
        final Bean oldValue;
        
        try
          {
            oldValue = (Bean)beanField.get(this);
            beanField.set(this, bean);
          }
        catch (IllegalAccessException e)
          {
            throw new RuntimeException(e);
          }
        
        bindingGroup.unbind();
        //
        // This is needed NOW so the list of state/province is populated
        // from the current country and the binding from iptcContact won't fail.
        //
        specialBind(bean);
        bindingGroup.unbind();
        
        for (Binding binding : bindingGroup.getBindings())
          {
            if (binding.getSourceObject() == oldValue)
              {
                binding.setSourceObject(bean);
              }
          }
        
        bindingGroup.bind();
      }
    
    /***************************************************************************
     *
     *
     **************************************************************************/
    @Override
    public void setForeground (final Color color)
      {
        super.setForeground(color);
        
        for (final Component component : getComponents())
          {
            component.setForeground(color);
          }
      }

    /***************************************************************************
     *
     *
     **************************************************************************/
    protected void specialBind (final Bean bean)
      {
      }
  }
