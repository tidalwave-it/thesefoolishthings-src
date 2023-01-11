/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2023 by Tidalwave s.a.s. (http://tidalwave.it)
 *
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
 * git clone https://bitbucket.org/tidalwave/thesefoolishthings-src
 * git clone https://github.com/tidalwave-it/thesefoolishthings-src
 *
 * *********************************************************************************************************************
 */
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * Facility class to manage resource bundles.
 *
 * @since   3.1-ALPHA-2
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BundleUtilities
  {
    /*******************************************************************************************************************
     *
     * Returns a localised message.
     *
     * @param   ownerClass      the owner of the bundle
     * @param   resourceName    the name of the resource inside the bundle
     * @param   params          the parameters (used if the string in the bundle is a {@code String} format)
     * @return                  the message
     *
     ******************************************************************************************************************/
    @Nonnull
    public static String getMessage (@Nonnull final Class<?> ownerClass,
                                     @Nonnull final String resourceName,
                                     @Nonnull final Object ... params)
      {
        return getMessage(ownerClass, Locale.getDefault(), resourceName, params);
      }

    /*******************************************************************************************************************
     *
     * Returns a localised message.
     *
     * @since   3.1-ALPHA-4
     * @param   ownerClass      the owner of the bundle
     * @param   locale          the {@link Locale}
     * @param   resourceName    the name of the resource inside the bundle
     * @param   params          the parameters (used if the string in the bundle is a {@code String} format)
     * @return                  the message
     *
     ******************************************************************************************************************/
      @Nonnull
      public static String getMessage (@Nonnull final Class<?> ownerClass,
                                       @Nonnull final Locale locale,
                                       @Nonnull final String resourceName,
                                       @Nonnull final Object ... params)
      {
        final String packageName = ownerClass.getPackage().getName();
        final ResourceBundle bundle = ResourceBundle.getBundle(packageName + ".Bundle", locale);
        final String string = bundle.getString(resourceName);

        return (params.length == 0) ? string : String.format(string, params);
      }
  }
