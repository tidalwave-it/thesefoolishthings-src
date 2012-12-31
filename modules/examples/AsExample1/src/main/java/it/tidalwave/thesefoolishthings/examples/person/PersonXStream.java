/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/

package it.tidalwave.thesefoolishthings.examples.person;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import it.tidalwave.util.Id;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PersonXStream extends XStream
  {
    public PersonXStream() 
      {
        super(new StaxDriver());
        aliasField("first-name", Person.class, "firstName");
        aliasField("last-name", Person.class, "lastName");
//        alias("id", Id.class);
        alias("person", Person.class);
        
        useAttributeFor(Person.class, "id");
        registerConverter(new SingleValueConverter() 
          {
            public String toString (final Object object) 
              {
                return ((Id)object).stringValue();
              }

            public Object fromString (final String string) 
              {
                return new Id(string);
              }

            public boolean canConvert (Class type) 
              {
                return type.equals(Id.class);
              }
          });
      }
    
  }
