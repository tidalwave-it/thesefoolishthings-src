/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://thesefoolishthings.kenai.com
 * SCM: https://kenai.com/hg/thesefoolishthings~src
 *
 **********************************************************************************************************************/
package it.tidalwave.java.awt.event;

import it.tidalwave.java.awt.AWTEvent;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ActionEvent extends AWTEvent 
  {
    private static final long serialVersionUID = -7671078796273832149L;
    
    public static final int SHIFT_MASK = 1;
    public static final int CTRL_MASK = 2;
    public static final int META_MASK = 4;
    public static final int ALT_MASK = 8;
    public static final int ACTION_FIRST = 1001;
    public static final int ACTION_LAST = 1001;
    public static final int ACTION_PERFORMED = 1001;
    
    private String command;

    private long when;
    
    private int modifiers;
    
    public ActionEvent (Object source, int id, String command) 
      {
        this(source, id, command, 0);
      }

    public ActionEvent (Object source, int id, String command, int modifiers) 
      {
        this(source, id, command, 0l, modifiers);
      }

    public ActionEvent (Object source, int id, String command, long when, int modifiers) 
      {
        super(source, id);

        this.command = command;
        this.when = when;
        this.modifiers = modifiers;
      }

    public int getModifiers() 
      {
        return modifiers;
      }

    public String getActionCommand() 
      {
        return command;
      }

    public long getWhen() 
      {
        return when;
      }

    @Override
    public String paramString()   
      {
        return "";
      }
  }
