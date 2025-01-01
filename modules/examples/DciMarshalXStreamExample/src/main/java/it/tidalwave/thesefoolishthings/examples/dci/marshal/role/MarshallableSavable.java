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
package it.tidalwave.thesefoolishthings.examples.dci.marshal.role;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import it.tidalwave.util.As;
import it.tidalwave.dci.annotation.DciRole;
import static it.tidalwave.role.io.Marshallable._Marshallable_;

/***************************************************************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **************************************************************************************************************************************************************/
// START SNIPPET: marshallablesavable
@DciRole(datumType = Object.class)
public class MarshallableSavable implements Savable
  {
    @Nonnull
    private final As datumAsDelegate;

    public MarshallableSavable (@Nonnull final Object datum)
      {
        this.datumAsDelegate = As.forObject(datum);
      }

    @Override
    public void saveTo (@Nonnull final Path path, @Nonnull final Charset charset, @Nonnull final OpenOption ... openOptions)
            throws IOException
      {
        assert charset.equals(StandardCharsets.UTF_8);

        try (final var os = Files.newOutputStream(path, openOptions))
          {
            datumAsDelegate.as(_Marshallable_).marshal(os);
          }
      }
  }
// END SNIPPET: marshallablesavable
