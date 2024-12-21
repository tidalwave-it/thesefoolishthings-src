/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2024 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 * *************************************************************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * *************************************************************************************************************************************************************
 *
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *************************************************************************************************************************************************************
 */
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import it.tidalwave.util.As;
import it.tidalwave.util.spi.ArrayListCollectorSupport;
import it.tidalwave.role.Composite;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import static it.tidalwave.util.Parameters.r;
import static it.tidalwave.role.ui.Presentable._Presentable_;

/***************************************************************************************************************************************************************
 *
 * A {@link java.util.stream.Collector} which collects a {@link Stream} of {@link PresentationModel}s into a single
 * {@code PresentationModel} with a {@code Composite&lt;PresentationModel&gt;} role containing them.
 * 
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
public class PresentationModelCollectors extends ArrayListCollectorSupport<PresentationModel, PresentationModel>
  {
    @Nonnull
    private final Collection<Object> roles = new ArrayList<>();
    
    /***********************************************************************************************************************************************************
     * A {@link java.util.stream.Collector} which collects a {@link Stream} of {@link PresentationModel}s into a single
     * {@code PresentationModel} with a {@link Composite} role containing them. In other words:
     * 
     * <pre>
     * List&lt;PresentationModel&gt; pms = ...
     * PresentationModel compositePm = pms.stream().collect(toCompositePresentationModel());
     * // same contents as childrenPms
     * List&lt;PresentationModel&gt; childrenPms = compositePm.as(_Composite_).findChildren().results();
     * </pre>
     * 
     * @param   roles   some extra roles included in the resulting {@code PresentationModel}
     * @return          a {@code PresentationModel}
     * @since   3.2-ALPHA-3 (refactored)
     **********************************************************************************************************************************************************/
    @Nonnull
    public static PresentationModelCollectors toCompositePresentationModel (@Nonnull final Collection<Object> roles)
      {
        return new PresentationModelCollectors(roles);
      }

    @Nonnull
    public static PresentationModelCollectors toCompositePresentationModel()
      {
        return toCompositePresentationModel((Collection<Object>)Collections.emptyList());
      }

    /***********************************************************************************************************************************************************
     * A facility method that creates a composite {@link PresentationModel} out of an iterable (which means an array,
     * a collection or a stream) of objects implementing {@link As}. For each  object in the iterable, its {@code
     * PresentationModel} is created by means of invoking its {@link it.tidalwave.role.ui.Presentable} role. Then all
     * the {@code PresentationModel}s are aggregated into the composite.
     * 
     * A function which creates specific roles for each {@code PresentationModel} can be specified. The function can
     * return a single role or multiple roles in form of an array {@code Object[]}.
     *
     * @param   <T>             the type of the objects
     * @param   i               the {@code Iterable}
     * @param   roleCreator     the function to create roles
     * @return                  the composite {@code PresentationModel}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> PresentationModel toCompositePresentationModel (
            @Nonnull final Iterable<? extends T> i,
            @Nonnull final Function<? super T, Object> roleCreator)
      {
        return StreamSupport.stream(i.spliterator(), false)
                            .map(o -> o.as(_Presentable_).createPresentationModel(r(roleCreator.apply(o))))
                            .collect(toCompositePresentationModel());
      }

    /***********************************************************************************************************************************************************
     * A facility simplified version of 
     * {@link #toCompositePresentationModel(java.lang.Iterable, java.util.function.Function)}.
     *
     * @param   <T>             the type of the objects
     * @param   i               the {@code Iterable}
     * @return                  the composite {@code PresentationModel}
     * 
     ******************************************************************************************************************/
    @Nonnull
    public static <T extends As> PresentationModel toCompositePresentationModel (@Nonnull final Iterable<T> i)
      {
        return toCompositePresentationModel(i, o -> new Object[0]);
      }

    /***********************************************************************************************************************************************************
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Override @Nonnull 
    public Function<List<PresentationModel>, PresentationModel> finisher() 
      {
        return childrenPms -> PresentationModel.of("", r(roles, SimpleComposite.ofCloned(childrenPms)));
      }
    
    /***********************************************************************************************************************************************************
     * 
     ******************************************************************************************************************/
    private PresentationModelCollectors (@Nonnull final Collection<Object> roles)
      {
        this.roles.addAll(roles);
      }
  }
