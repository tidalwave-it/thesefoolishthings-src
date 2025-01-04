package it.tidalwave.thesefoolishthings.examples.jpafinderexample;

import jakarta.annotation.Nonnull;
import java.util.List;
import it.tidalwave.role.spi.SystemRoleFactory;
import it.tidalwave.role.spi.SystemRoleFactoryProvider;
import it.tidalwave.role.spi.SystemRoleFactorySupport;
import it.tidalwave.thesefoolishthings.examples.jpafinderexample.impl.PersonJpaPersistable;

// START SNIPPET: HardwiredSystemRoleFactoryProvider
public class HardwiredSystemRoleFactoryProvider implements SystemRoleFactoryProvider
  {
    private static final List<Class<?>> ROLES = List.of(PersonJpaPersistable.class);

    static class HardwiredRoleFactory  extends SystemRoleFactorySupport
      {
        public void initialize()
          {
            scan(ROLES);
          }
      }

    @Override @Nonnull
    public SystemRoleFactory getSystemRoleFactory ()
      {
        final var h = new HardwiredRoleFactory();
        h.initialize();
        return h;
      }
  }
// END SNIPPET: HardwiredSystemRoleFactoryProvider
