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

import java.util.Collection;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * The Class CollectionConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SkippUnkownElementsCollectionConverter
    extends com.thoughtworks.xstream.converters.collections.CollectionConverter
{

  private static final Logger LOG = Logger.getLogger(SkippUnkownElementsCollectionConverter.class);

  public SkippUnkownElementsCollectionConverter(Mapper mapper, Class type)
  {
    super(mapper, type);
  }

  public SkippUnkownElementsCollectionConverter(Mapper mapper)
  {
    super(mapper);

  }

  @Override
  protected void addCurrentElementToCollection(HierarchicalStreamReader reader, UnmarshallingContext context,
      Collection collection, Collection target)
  {
    try {
      Object item = readItem(reader, context, collection);
      target.add(item);
    } catch (CannotResolveClassException ex) {
      LOG.warn("Cannot reconstruct nested type: " + ex.getMessage());
    }
  }
}
