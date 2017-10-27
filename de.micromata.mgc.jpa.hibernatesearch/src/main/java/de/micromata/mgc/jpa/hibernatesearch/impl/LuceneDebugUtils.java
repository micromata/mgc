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

package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.metadata.FieldDescriptor;
import org.hibernate.search.metadata.IndexDescriptor;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;

import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;

/**
 * Just some utils to debugging.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LuceneDebugUtils
{
  public String getIndexDescription(SearchEmgrFactory<?> emfac, Class<?> entityClass)
  {
    StringBuilder sb = new StringBuilder();
    emfac.runWoTrans((emgr) -> {
      sb.append("class: ").append(entityClass.getName()).append("\n");

      FullTextEntityManager femg = emgr.getFullTextEntityManager();
      SearchFactory sf = femg.getSearchFactory();
      IndexedTypeDescriptor itd = sf.getIndexedTypeDescriptor(entityClass);
      List<String> fields = itd.getIndexedProperties().stream().map((desc) -> desc.getName())
          .collect(Collectors.toList());
      sb.append("\nFields: ").append(StringUtils.join(fields, ", ")).append("\n");

      IndexedTypeDescriptor descr = sf.getIndexedTypeDescriptor(entityClass);
      sb.append("\nIndexedTypeDescriptor: indexed: ").append(descr.isIndexed()).append("\nFields:\n");
      for (FieldDescriptor field : descr.getIndexedFields()) {
        sb.append("  ").append(field).append("<br?\n");
      }
      sb.append("\nProperties: \n");
      for (PropertyDescriptor ip : descr.getIndexedProperties()) {
        sb.append("  ").append(ip).append("\n");
      }

      sb.append("\nIndexe: \n");
      for (IndexDescriptor ides : descr.getIndexDescriptors()) {
        sb.append("  ").append(ides).append("\n");
      }

      String[] sfields = getSearchFieldsForEntity(emfac, entityClass);
      sb.append("\nSearchFields: ").append(StringUtils.join(sfields, ",")).append("\n");
      return null;
    });
    return sb.toString();
  }

  public String[] getSearchFieldsForEntity(SearchEmgrFactory<?> emfac, Class<?> entityClass)
  {
    String[] ret = emfac.getSearchFieldsForEntity(entityClass).keySet().toArray(new String[] {});

    List<String> list = emfac.runWoTrans((emgr) -> {
      FullTextEntityManager femg = emgr.getFullTextEntityManager();
      SearchFactory sf = femg.getSearchFactory();
      IndexedTypeDescriptor itd = sf.getIndexedTypeDescriptor(entityClass);
      return itd.getIndexedProperties().stream().map((desc) -> desc.getName()).collect(Collectors.toList());
    });
    if (ret.length <= list.size()) {
      return list.toArray(new String[] {});
    }
    return ret;
  }
}
