/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/

package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import it.tidalwave.role.Marshallable;
import it.tidalwave.role.annotation.RoleFor;
import com.thoughtworks.xstream.XStream;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RoleFor(datum = DefaultPersonRegistry.class) @RequiredArgsConstructor
public class DefaultPersonRegistryXStreamMarshallable implements Marshallable
  {
    @Nonnull
    private final DefaultPersonRegistry datum;
    
    private final XStream xstream = new PersonXStream();
            
    @Override
    public void marshal (final @Nonnull OutputStream os) 
      throws IOException 
      {
        xstream.toXML(datum.persons, os);
      }
  }
