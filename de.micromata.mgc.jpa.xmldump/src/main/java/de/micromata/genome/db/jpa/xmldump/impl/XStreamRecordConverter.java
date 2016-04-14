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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Class XStreamRecordConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class XStreamRecordConverter implements Converter
{

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(XStreamRecordConverter.class);

  /**
   * The converter lookup.
   */
  private final ConverterLookup converterLookup;

  /**
   * The emgrfac.
   */
  private EmgrFactory<?> emgrfac;

  private List<Object> allEnties = new ArrayList<>();
  /**
   * The entities.
   */
  private Map<EntityMetadata, List<Object>> entities = new HashMap<>();

  /**
   * The xml id to object map.
   */
  private Map<Object, Object> xmlIdToObjectMap = new HashMap<>();

  /**
   * The table classes.
   */
  private Map<Class<?>, EntityMetadata> tableClasses = new HashMap<>();

  /**
   * Instantiates a new x stream record converter.
   *
   * @param xstream the xstream
   * @param emgrfac the emgrfac
   */
  public XStreamRecordConverter(XStream xstream, EmgrFactory<?> emgrfac)
  {
    converterLookup = new XStream().getConverterLookup();
    this.emgrfac = emgrfac;
    emgrfac.getMetadataRepository().getTableEntities().forEach((e) -> tableClasses.put(e.getJavaType(), e));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2)
  {
    converterLookup.lookupConverterForType(arg0.getClass()).marshal(arg0, arg1, arg2);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Object unmarshal(final HierarchicalStreamReader streamReader, UnmarshallingContext context)
  {
    Object result;
    Class<?> targetType = null;
    try {
      targetType = context.getRequiredType();
      Converter conv = converterLookup.lookupConverterForType(targetType);
      result = conv.unmarshal(streamReader, context);
      //      LOG.info("Restored: " + targetType);
      if (targetType.getName().endsWith("KontoDO") == true) {
        LOG.info("Restored: " + targetType);
      }
    } catch (final Exception ex) {
      LOG.warn("Ignore unknown class or property " + targetType + " " + ex.getMessage());
      return null;
    } finally {
    }
    XStreamReferenceByIdUnmarshaller byund = (XStreamReferenceByIdUnmarshaller) context;
    registerObject(byund, result);

    return result;
  }

  /**
   * Find table meta data.
   *
   * @param clazz the clazz
   * @return the entity metadata
   */
  private EntityMetadata findTableMetaData(Class<?> clazz)
  {
    for (EntityMetadata md : emgrfac.getMetadataRepository().getTableEntities()) {
      if (md.getJavaType().equals(clazz) == true) {
        return md;
      }
    }
    return null;
  }

  /**
   * Register object.
   *
   * @param byidunm the byidunm
   * @param result the result
   */
  private void registerObject(XStreamReferenceByIdUnmarshaller byidunm, Object result)
  {
    if (result == null) {
      return;
    }

    EntityMetadata entityMetadata = findTableMetaData(result.getClass());
    if (entityMetadata == null) {
      return;
    }
    allEnties.add(result);
    //    Object id = byidunm.getCurrentId();
    xmlIdToObjectMap.put(System.identityHashCode(result), result);
    List<Object> list = entities.get(entityMetadata);
    if (list == null) {
      list = new ArrayList<>();
      entities.put(entityMetadata, list);
    }
    list.add(result);
  }

  /**
   * {@inheritDoc}
   *
   */

  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert(final Class clazz)
  {
    return tableClasses.containsKey(clazz);
  }

  public List<Object> getAllEnties()
  {
    return allEnties;
  }

}
