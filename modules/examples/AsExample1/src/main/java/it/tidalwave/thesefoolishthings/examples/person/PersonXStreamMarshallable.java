/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import com.thoughtworks.xstream.XStream;
import it.tidalwave.role.Marshallable;
import it.tidalwave.role.annotation.RoleFor;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RoleFor(datum = Person.class, context = XStreamContext.class) @RequiredArgsConstructor
public final class PersonXStreamMarshallable implements Marshallable
  {
    @Nonnull
    private final Person datum;
    
    @Nonnull
    private final XStreamContext xStreamContext;
                        
    @Override
    public void marshal (final @Nonnull OutputStream os) 
      throws IOException 
      {
        xStreamContext.getXStream().toXML(datum, os);
      }
  }
