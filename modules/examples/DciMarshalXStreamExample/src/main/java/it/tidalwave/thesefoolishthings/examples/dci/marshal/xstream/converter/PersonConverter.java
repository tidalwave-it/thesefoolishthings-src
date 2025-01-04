package it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream.converter;

import jakarta.annotation.Nonnull;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import it.tidalwave.util.Id;
import it.tidalwave.thesefoolishthings.examples.person.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/***************************************************************************************************************************************************************
 *
 * The custom converter for {@link Person}, which is immutable.
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class PersonConverter implements Converter
  {
    @AllArgsConstructor @Getter @Setter
    public static class MutablePerson
      {
        @Nonnull
        private Id id;

        @Nonnull
        private String firstName;

        @Nonnull
        private String lastName;

        @Nonnull
        public static MutablePerson fromPerson (@Nonnull final Person person)
          {
            return new MutablePerson(person.getId(), person.getFirstName(), person.getLastName());
          }

        @Nonnull
        public Person toPerson()
          {
            return new Person(id, firstName, lastName);
          }
      }

    @Override
    public void marshal (@Nonnull final Object source,
                         @Nonnull final HierarchicalStreamWriter writer,
                         @Nonnull final MarshallingContext context)
      {
        context.convertAnother(MutablePerson.fromPerson((Person)source));
      }

    @Override
    public Object unmarshal (@Nonnull final HierarchicalStreamReader reader,
                             @Nonnull final UnmarshallingContext context)
      {
        return ((MutablePerson)context.convertAnother(null, MutablePerson.class)).toPerson();
      }

    @Override
    public boolean canConvert (@Nonnull final Class type)
      {
        return type.equals(Person.class);
      }
  }
