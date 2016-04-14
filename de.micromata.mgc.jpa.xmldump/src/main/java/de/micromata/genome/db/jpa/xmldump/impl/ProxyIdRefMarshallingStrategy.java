//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.db.jpa.xmldump.impl;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.ReferenceByIdUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * The Class ProxyIdRefMarshallingStrategy.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ProxyIdRefMarshallingStrategy extends ReferenceByIdMarshallingStrategy
{

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Object unmarshal(Object root, HierarchicalStreamReader reader, DataHolder dataHolder,
      ConverterLookup converterLookup, Mapper classMapper)
  {
    return new ReferenceByIdUnmarshaller(root, reader, converterLookup, classMapper).start(dataHolder);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void marshal(HierarchicalStreamWriter writer, Object obj, ConverterLookup converterLookup,
      Mapper classMapper, DataHolder dataHolder)
  {
    new ProxyIdRefMarshaller(writer, converterLookup, classMapper).start(obj, dataHolder);
  }

}
