/*
 * *********************************************************************************************************************
 *
 * TheseFoolishThings: Miscellaneous utilities
 * http://tidalwave.it/projects/thesefoolishthings/modules/thesefoolishthings-examples/it-tidalwave-thesefoolishthings-examples-dci-marshal-xstream
 *
 * Copyright (C) 2009 - 2021 by Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import it.tidalwave.role.io.Marshallable;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream.XStreamContext;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * A facility class for implementing a {@link Marshallable} using XStream. Subclass properly and eventually override
 * {@link #getMarshallingObject(java.lang.Object)} when only a part of the datum must be considered by XStream.
 * 
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public abstract class XStreamMarshallableSupport<D> implements Marshallable
  {
    @Nonnull
    private final D datum;

    @Nonnull
    private final XStreamContext xStreamContext;

    @Override
    public final void marshal (@Nonnull final OutputStream os)
      throws IOException
      {
        xStreamContext.getXStream().toXML(getMarshallingObject(datum), os);
      }

    @Nonnull
    protected Object getMarshallingObject (@Nonnull final D datum)
      {
        return datum;
      }
  }
