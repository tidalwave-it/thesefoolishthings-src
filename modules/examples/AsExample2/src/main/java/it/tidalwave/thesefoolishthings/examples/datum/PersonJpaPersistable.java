/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.datum;

import it.tidalwave.thesefoolishthings.examples.person.Person;
import javax.annotation.Nonnull;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import it.tidalwave.role.Persistable;
import it.tidalwave.role.Removable;
import it.tidalwave.role.annotation.RoleFor;
import it.tidalwave.role.AsExtensions;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.ExtensionMethod;
import lombok.Getter;
import lombok.Setter;
import static it.tidalwave.role.Identifiable.Identifiable;
import javax.persistence.Transient;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RoleFor(datum=Person.class, context=JpaPersistenceContext.class) 
@Entity @NoArgsConstructor @Getter @Setter @ToString
@ExtensionMethod(AsExtensions.class)
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
        this.id = datum.as(Identifiable).getId().stringValue();
        this.firstName = datum.firstName;
        this.lastName = datum.lastName;
      }
    
    @Nonnull
    public Person toPerson()
      {
        return new Person(firstName, lastName);
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
