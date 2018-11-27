/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2018 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.function.Function;
import it.tidalwave.util.As;
import it.tidalwave.util.spi.ArrayListCollectorSupport;
import it.tidalwave.role.spi.ArrayListSimpleComposite;
import it.tidalwave.role.Composite;
import it.tidalwave.role.ui.PresentationModel;
import static it.tidalwave.role.ui.Presentable.Presentable;

/***********************************************************************************************************************
 *
 * A {@link Collector} which collects a {@link Stream} of {@link PresentationModel}s into a single
 * {@code PresentationModel} with a {@link Composite&lt;PresentationModel&gt;} role containing them.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PresentationModelCollectors extends ArrayListCollectorSupport<PresentationModel, PresentationModel>
  {
    @Nonnull
    private final List<Object> roles = new ArrayList<>();
    
    /*******************************************************************************************************************
     *
     * A {@link Collector} which collects a {@link Stream} of {@link PresentationModel}s into a single
     * {@code PresentationModel} with a {@link Composite} role containing them. In other words:
     * 
     * <pre>
     * List&lt;PresentationModel&gt; pms = ...
     * PresentationModel compositePm = pms.stream().collect(toCompositePresentationModel());
     * // same contents as childrenPms
     * List&lt;PresentationModel&gt; childrenPms = compositePm.as(Composite).findChildren().results();
     * </pre>
     * 
     * @param   roles   some extra roles included in the resulting {@code PresentationModel}
     * @return          a {@code PresentationModel}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static PresentationModelCollectors toCompositePresentationModel (final @Nonnull Object ... roles)
      {
        return new PresentationModelCollectors(Arrays.asList(roles));
      }

    /*******************************************************************************************************************
     *
     * A facility method that creates a composite {@link PresentationModel} out of a stream of objects. For each object
     * in the stream, its {@code PresentationModel} is created by means of invoking its {@link Presentable} role.
     * Then all the {@code PresentationModel}s are aggregated into the composite.
     * 
     * This method accepts an {@link Iterable}. A function which creates specific roles for each 
     * {@code PresentationModel} can be supplied. The function can return a single role or multiple roles in form of
     * an {@code Object[]}.
     * 
     * @param   i               the {@code Iterable}
     * @param   roleCreator     the function to create roles
     * @return                  the composite {@code PresentationModel}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> PresentationModel toCompositePresentationModel (
            final @Nonnull Iterable<T> i,
            final @Nonnull Function<T, Object> roleCreator)
      {
        return StreamSupport.stream(i.spliterator(), false)
                            .map(o -> o.as(Presentable).createPresentationModel(array(roleCreator.apply(o))))
                            .collect(PresentationModelCollectors.toCompositePresentationModel());
      }

    /*******************************************************************************************************************
     *
     * A facility simplified version of 
     * {@link #toCompositePresentationModel(java.lang.Iterable, java.util.function.Function)} without a function to
     * supply additional roles.
     * 
     * @param   i               the {@code Iterable}
     * @return                  the composite {@code PresentationModel}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> PresentationModel toCompositePresentationModel (final @Nonnull Iterable<T> i)
      {
        return toCompositePresentationModel(i, o -> new Object[0]);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Override @Nonnull 
    public Function<List<PresentationModel>, PresentationModel> finisher() 
      {
        return childrenPms ->
          {
            final List<Object> temp = new ArrayList<>(roles);
            temp.add(new ArrayListSimpleComposite<>(childrenPms));
            // FIXME: "" triggers a NPE in RoleManagerSupport.java:341
            return new DefaultPresentationModel("", temp.toArray());
          };
      }
    
    /*******************************************************************************************************************
     *
     * 
     ******************************************************************************************************************/
    private PresentationModelCollectors (final @Nonnull List<Object> roles) 
      {
        this.roles.addAll(roles);
      }
    
    /*******************************************************************************************************************
     *
     * 
     ******************************************************************************************************************/
    @Nonnull
    private static Object[] array (final @Nonnull Object object) 
      {
        return (object instanceof Object[]) ? (Object[])object : new Object[] { object };
      }
  }
