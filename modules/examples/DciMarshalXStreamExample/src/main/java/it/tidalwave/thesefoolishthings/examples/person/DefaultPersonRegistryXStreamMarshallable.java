/*
 * #%L
 * *********************************************************************************************************************
 * 
 * TheseFoolishThings Examples - DCI Marshal XStream
 * %%
 * Copyright (C) 2009 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.thesefoolishthings.examples.person;

import javax.annotation.Nonnull;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.thesefoolishthings.examples.dci.marshal.xstream.XStreamContext;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datum = DefaultPersonRegistry.class, context = XStreamContext.class)
public class DefaultPersonRegistryXStreamMarshallable extends XStreamMarshallableSupport<DefaultPersonRegistry>
  {
    public DefaultPersonRegistryXStreamMarshallable (final @Nonnull DefaultPersonRegistry datum,
                                                     final @Nonnull XStreamContext context)
      {
        super(datum, context);
      }

    @Override @Nonnull
    protected Object getMarshallingObject (final @Nonnull DefaultPersonRegistry datum)
      {
        return datum.persons;
      }
  }
