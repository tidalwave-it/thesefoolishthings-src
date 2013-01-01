/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/

package it.tidalwave.thesefoolishthings.examples.person;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import it.tidalwave.util.Id;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class XStreamContext
  {
    @Getter
    private final XStream xStream = new XStream(new StaxDriver());
    
    public XStreamContext()
      {
        xStream.aliasField("first-name", Person.class, "firstName");
        xStream.aliasField("last-name", Person.class, "lastName");
        xStream.alias("person", Person.class);
        xStream.alias("persons", ListOfPersons.class);
        xStream.addImplicitCollection(ListOfPersons.class, "persons");
        
        xStream.useAttributeFor(Person.class, "id");
        xStream.registerConverter(new SingleValueConverter() 
          {
            @Override
            public String toString (final Object object) 
              {
                return ((Id)object).stringValue();
              }

            @Override
            public Object fromString (final String string) 
              {
                return new Id(string);
              }

            @Override
            public boolean canConvert (Class type) 
              {
                return type.equals(Id.class);
              }
          });
      }
  }
