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
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import it.tidalwave.role.Persistable;
import it.tidalwave.role.Removable;
import it.tidalwave.role.annotation.RoleFor;
import it.tidalwave.thesefoolishthings.examples.datum.JpaPersistenceContext;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RoleFor(datum=Person.class, context=JpaPersistenceContext.class)
@Entity @NoArgsConstructor @Getter @Setter @ToString(exclude="context")
public class PersonJpaPersistable implements Serializable, Persistable, Removable
  {
    @Transient
    private JpaPersistenceContext context;

    @Id
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    public PersonJpaPersistable (final @Nonnull Person datum, final @Nonnull JpaPersistenceContext context)
      {
        this.context = context;
        this.id = datum.id.stringValue();
        this.firstName = datum.firstName;
        this.lastName = datum.lastName;
      }

    @Nonnull
    public Person toPerson()
      {
        return new Person(new it.tidalwave.util.Id(id), firstName, lastName);
      }

    public void persist()
      {
        context.persist(this);
      }

    public void remove()
      {
        context.remove(this);
      }
  }
