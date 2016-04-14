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

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.bridge.spi.FieldMetadataBuilder;
import org.hibernate.search.bridge.spi.FieldType;

import de.micromata.genome.db.jpa.history.entities.HistoryMasterBaseDO;

/**
 * Stores the History table in hibernate search.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HistoryMasterClassBridge implements MetadataProvidingFieldBridge
{
  /**
   * Limit of Lucene.
   */
  private static final int MAX_FULLTEXT_FIELDLENGTH = 30000;

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
  {
    HistoryMasterBaseDO<?, ?> hm = (HistoryMasterBaseDO<?, ?>) value;

    Field field = new LongField("entityId", hm.getEntityId(), Field.Store.YES);
    document.add(field);
    field = new StringField("entityName", hm.getEntityName(), TabAttrFieldBridge.DEFAULT_STORE);
    document.add(field);
    field = new LongField("modifiedAt", hm.getModifiedAt().getTime(), TabAttrFieldBridge.DEFAULT_STORE);
    document.add(field);

    for (String key : hm.getAttributeKeys()) {
      String svalue = hm.getStringAttribute(key);
      if (StringUtils.isBlank(svalue) == true) {
        continue;
      }
      //      field = new StringField(key, svalue, Field.Store.NO);
      //      document.add(field);
      //      if (StringUtils.endsWith(key, ":nv") == true) {
      //        field = new StringField("NEW_VALUE", svalue, Field.Store.NO);
      //        document.add(field);
      //      }
      if (StringUtils.endsWith(key, ":ov") == true) {
        String indexv = svalue;
        if (indexv.length() > MAX_FULLTEXT_FIELDLENGTH) {
          indexv = indexv.substring(0, MAX_FULLTEXT_FIELDLENGTH);
        }
        field = new StringField("oldValue", indexv, TabAttrFieldBridge.DEFAULT_STORE);
        document.add(field);
      }
    }
  }

  @Override
  public void configureFieldMetadata(String name, FieldMetadataBuilder builder)
  {
    // does not have a effect on meta info?!?
    builder.field(name + "entityId", FieldType.LONG)
        .field(name + "entityName", FieldType.STRING).sortable(true)
        .field(name + "modifiedAt", FieldType.LONG).sortable(true)
        //        .field("NEW_VALUE", FieldType.STRING)
        .field(name + "oldValue", FieldType.STRING).sortable(true);

  }

}
