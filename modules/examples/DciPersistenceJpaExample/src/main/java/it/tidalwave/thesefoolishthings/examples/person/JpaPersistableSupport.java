/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.person;

import java.io.Serializable;
import javax.persistence.Transient;
import it.tidalwave.role.Persistable;
import it.tidalwave.role.Removable;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.thesefoolishthings.examples.dci.persistable.jpa.JpaPersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = Person.class, context = JpaPersistenceContext.class)
@AllArgsConstructor @NoArgsConstructor 
public abstract class JpaPersistableSupport implements Serializable, Persistable, Removable
  {
    @Transient
    private JpaPersistenceContext context;

    @Override
    public void persist()
      {
        context.persist(this);
      }

    @Override
    public void remove()
      {
        context.remove(this);
      }
  }
