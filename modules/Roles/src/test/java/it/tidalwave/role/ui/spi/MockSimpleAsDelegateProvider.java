package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.spi.AsDelegate;
import it.tidalwave.util.spi.AsDelegateProvider;
import org.mockito.Mockito;

/***********************************************************************************************************************
 *
 * A simple implementation of {@link AsDelegateProvider} that might be enough for running tests. It just instantiates
 * a Mockito mock for each requested role.
 *
 * Install it with {@code AsDelegateProvider.Locator.set(new MockSimpleAsDelegateProvider());} in a {@code BeforeClass}
 * method.
 *
 * @author  Fabrizio Giudici
 *
 * @it.tidalwave.javadoc.experimental
 * @since 3.2-ALPHA-8
 *
 **********************************************************************************************************************/
public class MockSimpleAsDelegateProvider implements AsDelegateProvider
  {
    static class MockSimpleAsDelegate implements AsDelegate
      {
        @Override @Nonnull
        public <T> Collection<? extends T> as (@Nonnull final Class<T> roleType)
          {
            return Collections.singleton(Mockito.mock(roleType));
          }
      }

    @Override @Nonnull
    public AsDelegate createAsDelegate (@Nonnull final Object datum)
      {
        return new MockSimpleAsDelegate();
      }
  }