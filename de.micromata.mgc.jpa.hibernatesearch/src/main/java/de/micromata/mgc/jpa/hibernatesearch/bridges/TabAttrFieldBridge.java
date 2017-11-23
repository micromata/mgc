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

package de.micromata.mgc.jpa.hibernatesearch.bridges;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.bridge.spi.FieldMetadataBuilder;
import org.hibernate.search.bridge.spi.FieldType;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;

/**
 * Add to Add Search to Attrs
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TabAttrFieldBridge implements FieldBridge, MetadataProvidingFieldBridge
{
  private static final Logger LOG = Logger.getLogger(TabAttrFieldBridge.class);
  public static final Store DEFAULT_STORE = Store.YES;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
  {
    addToIndex("", value, document, luceneOptions);
  }

  public static void addToIndex(String prefix, Object value, Document document, LuceneOptions luceneOptions)
  {
    if ((value instanceof Map) == false) {
      return;
    }
    Map rawmap = (Map) value;
    if (rawmap.isEmpty() == true) {
      return;
    }

    Set<Map.Entry> mes = rawmap.entrySet();
    for (Map.Entry me : mes) {
      if ((me.getKey() instanceof String) == false || (me.getValue() instanceof JpaTabAttrBaseDO) == false) {
        LOG.error("Bridge to incompatible type: " + me.getKey() + "=" + me.getValue());
        continue;
      }
      String key = (String) me.getKey();
      JpaTabAttrBaseDO<?, ?> attr = (JpaTabAttrBaseDO<?, ?>) me.getValue();
      String svalue = attr.getStringData();
      svalue = StringUtils.defaultString(svalue);
      Field field = new StringField(key, svalue, DEFAULT_STORE);
      document.add(field);
      field = new StringField("ALL", svalue, DEFAULT_STORE);
      document.add(field);
      field = new StringField("propertyName", key, DEFAULT_STORE);
      document.add(field);

      field = new StringField("value", svalue, DEFAULT_STORE);
      document.add(field);

    }
  }

  @Override
  public void configureFieldMetadata(String name, FieldMetadataBuilder builder)
  {
    builder
        .field(name + "_ALL", FieldType.STRING)
        .sortable(false)
        .field(name + "_propertyName", FieldType.STRING)
        .sortable(true)
        .field(name + "_value", FieldType.STRING)
        .sortable(true);

  }

}
