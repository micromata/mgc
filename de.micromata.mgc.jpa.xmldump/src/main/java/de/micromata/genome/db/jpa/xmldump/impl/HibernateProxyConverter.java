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

import org.hibernate.proxy.HibernateProxy;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * For marshaling, unboxing proxies.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateProxyConverter extends ReflectionConverter
{
  private ConverterLookup converterLookup;

  public HibernateProxyConverter(Mapper arg0, ReflectionProvider arg1, ConverterLookup converterLookup)
  {
    super(arg0, arg1);
    this.converterLookup = converterLookup;
  }

  /**
   * be responsible for hibernate proxy
   */
  @Override
  public boolean canConvert(Class clazz)
  {
    return HibernateProxy.class.isAssignableFrom(clazz);
  }

  @Override
  public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context)
  {
    Object item = arg0;
    if (item instanceof HibernateProxy) {
      item = ((HibernateProxy) arg0).getHibernateLazyInitializer().getImplementation();
    }
    converterLookup.lookupConverterForType(item.getClass()).marshal(item, writer, context);
  }
}
