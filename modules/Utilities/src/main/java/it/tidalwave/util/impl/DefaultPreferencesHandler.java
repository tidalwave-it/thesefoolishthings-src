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
package it.tidalwave.util.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import it.tidalwave.util.Key;
import it.tidalwave.util.PreferencesHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class DefaultPreferencesHandler implements PreferencesHandler
  {
    @Getter
    private final Path appFolder;

    @Getter
    private final Path logFolder;

    private final Map<Key<?>, Object> properties = new HashMap<>();

    public DefaultPreferencesHandler()
      {
        try
          {
            final var appName = System.getProperty(PROP_APP_NAME);
            Objects.requireNonNull(appName, "You must call PreferencesHandler.setAppName(\"...\") before getting here");

            final var osName = System.getProperty("os.name").toLowerCase();
            var pattern = "";

            switch (osName)
              {
                case "linux":
                  pattern = "%s/.%s/";
                  break;

                case "mac os x":
                  pattern = "%s/Library/Application Support/%s/";
                  break;

                case "windows":
                  pattern = "%s/AppData/Local/%s/";
                  break;

                default:
                  throw new ExceptionInInitializerError("Unknown o.s.: " + osName);
              }

            final var home = System.getProperty("user.home", "/tmp");
            appFolder = Paths.get(String.format(pattern, home, appName)).toAbsolutePath();
            logFolder = appFolder.resolve("logs").toAbsolutePath();
            Files.createDirectories(logFolder);
            log.info("App folder: {}", appFolder);
            log.info("Logging folder: {}", logFolder);
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }

    @Override @Nonnull
    public <T> Optional<T> getProperty (@Nonnull final Key<T> name)
      {
        return Optional.ofNullable((T)properties.get(name));
      }

    @Override
    public <T> void setProperty (@Nonnull final Key<T> name, @Nonnull final T value)
      {
        properties.put(name, value);
        // FIXME: should be made persistent (JSON?)
      }

    @Override
    public <T> void setDefaultProperty (@Nonnull final Key<T> name, @Nonnull final T value)
      {
        if (!properties.containsKey(name))
          {
            setProperty(name, value);
          }
      }
  }
