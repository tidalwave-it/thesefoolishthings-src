/*
 * *************************************************************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings
 *
 * Copyright (C) 2009 - 2025 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.nio.file.Path;
import static it.tidalwave.role.impl.ServiceLoaderLocator.lazySupplierOf;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @since   3.2-ALPHA-17
 *
 **************************************************************************************************************************************************************/
public interface PreferencesHandler
  {
    static class Inner
      {
        private static final LazySupplier<PreferencesHandler> REF = lazySupplierOf(PreferencesHandler.class);
      }

    public static final String PROPS_BASE_NAME = PreferencesHandler.class.getPackage().getName();

    public static final String PROP_APP_NAME = PROPS_BASE_NAME + ".appName";

    /** Suppress any console output. @since 3.2-ALPHA-21 */
    public static final String PROP_SUPPRESS_CONSOLE = PROPS_BASE_NAME + ".suppressConsoleOutput";

    /***********************************************************************************************************************************************************
     * Returns a singleton instance.
     *
     * @return  the singleton instance
     **********************************************************************************************************************************************************/
    @Nonnull
    public static PreferencesHandler getInstance()
      {
        return Inner.REF.get();
      }

    /***********************************************************************************************************************************************************
     * Returns the path of the folder where files related to the app should go.
     *
     * @return    the path
     **********************************************************************************************************************************************************/
    @Nonnull
    public Path getAppFolder();

    /***********************************************************************************************************************************************************
     * Returns the path of the folder where logs should go.
     *
     * @return    the path
     **********************************************************************************************************************************************************/
    @Nonnull
    public Path getLogFolder();

    /***********************************************************************************************************************************************************
     * Sets the application name. This method must be called at boot from the {@code main} method before doing
     * anything else.
     *
     * @param name    the property name
     **********************************************************************************************************************************************************/
    public static void setAppName (@Nonnull final String name)
      {
        System.setProperty(PROP_APP_NAME, name);
      }

    /***********************************************************************************************************************************************************
     * Gets a property.
     *
     * @param <T>     the property type
     * @param name    the property name
     * @return        the property value
     **********************************************************************************************************************************************************/
    @Nonnull
    public <T> Optional<T> getProperty (@Nonnull Key<T> name);

    /***********************************************************************************************************************************************************
     * Sets a property, overriding the current value.
     *
     * @param <T>     the property type
     * @param name    the property name
     * @param value   the property value
     **********************************************************************************************************************************************************/
    public <T> void setProperty (@Nonnull final Key<T> name, @Nonnull final T value);

    /**
     *
     * Sets a property, unless it has been already set.
     *
     * @param <T>     the property type
     * @param name    the property name
     * @param value   the property value
     **********************************************************************************************************************************************************/
    public <T> void setDefaultProperty (@Nonnull final Key<T> name, @Nonnull final T value);
  }
