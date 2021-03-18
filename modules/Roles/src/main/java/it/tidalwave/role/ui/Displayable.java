/*
 * #%L
 * *********************************************************************************************************************
 * 
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.tidalwave.it - git clone git@bitbucket.org:tidalwave/thesefoolishthings-src.git
 * %%
 * Copyright (C) 2009 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 * 
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.role.ui;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.impl.DefaultDisplayable;
import java.util.function.Function;
import java.util.function.Supplier;
import static it.tidalwave.util.BundleUtilities.getMessage;

/***********************************************************************************************************************
 *
 * The role of an object which can provide its own display name.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @it.tidalwave.javadoc.stable
 *
 **********************************************************************************************************************/
@FunctionalInterface
public interface Displayable
  {
    //@bluebook-begin other
    public final static Class<Displayable> _Displayable_ = Displayable.class;

    /*******************************************************************************************************************
     *
     * A default {@code Displayable} with an empty display name.
     *
     ******************************************************************************************************************/
    public final static Displayable DEFAULT = new DefaultDisplayable("", "DEFAULT");

    //@bluebook-end other
    /*******************************************************************************************************************
     *
     * Returns the display name in the current {@link java.util.Locale}.
     *
     * @return  the display name
     *
     ******************************************************************************************************************/
    @Nonnull
    public String getDisplayName();

    /*******************************************************************************************************************
     *
     * Creates an instance with a given display name.
     *
     * @param  displayName   the display name
     * @since   3.2-ALPHA-1 (was {@code DefaultDisplayable}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable of (final @Nonnull String displayName)
      {
        return of(displayName, "???");
      }

    /*******************************************************************************************************************
     *
     * Creates an instance with a given display name iand an explicit label for  {@code toString()}.
     *
     * @param  displayName   the display name
     * @param  toStringName  the name to be rendered when {@code toString()} is called
     * @since   3.2-ALPHA-1 (was {@code DefaultDisplayable}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable of (final @Nonnull String displayName, final @Nonnull String toStringName)
      {
        return new DefaultDisplayable(displayName, toStringName);
      }

    /*******************************************************************************************************************
     *
     * Creates an instance from a {@link Supplier<String>}. The supplier is invoked each time {@link #getDisplayName()}
     * is called.
     *
     * @param   supplier    the {@code Supplier}
     * @return              the instance
     * @since   3.2-ALPHA-3
     * @it.tidalwave.javadoc.experimental
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable of (final @Nonnull Supplier<String> supplier)
      {
        return () -> supplier.get();
      }

    /*******************************************************************************************************************
     *
     * Creates an instance from a {@link Function<..., String>} and a generic object that the function is applied to.
     * The function is invoked each time {@link #getDisplayName()} is called.
     *
     * @param   function    the {@code Function}
     * @param   object      the object
     * @return              the instance
     * @since   3.2-ALPHA-3
     * @it.tidalwave.javadoc.experimental
     *
     ******************************************************************************************************************/
    @Nonnull
    public static <T> Displayable of (final @Nonnull Function<T, String> function, final @Nonnull T object)
      {
        return () -> function.apply(object);
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link LocalizedDisplayable} from a resource bundle. The bundle resource file is named
     * {@code Bundle.properties} and it should be placed in the same package as the owner class.
     *
     * @param   ownerClass  the class that owns the bundle
     * @param   key         the resource key
     * @return              the {@code Displayable}
     * @since   3.2-ALPHA-1 (was previously in {@code Displayable8}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static LocalizedDisplayable fromBundle (final @Nonnull Class<?> ownerClass, final @Nonnull String key)
      {
        return new DefaultDisplayable(getMessage(ownerClass, key));
      }
  }
