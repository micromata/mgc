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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.jpa.metainf.ColumnMetadataBean;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchFieldInfoProvider;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;

/**
 * Provides a list of field names.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ListHibernateFieldInfoProvider implements HibernateSearchFieldInfoProvider
{

  @Override
  public Map<String, SearchColumnMetadata> getAdditionallySearchFields(EntityMetadata entm, String params)
  {
    Map<String, SearchColumnMetadata> ret = new HashMap<>();
    String[] fields = StringUtils.split(params, ',');
    for (String field : fields) {
      String fname = StringUtils.trim(field);
      ColumnMetadataBean cm = new ColumnMetadataBean(entm);
      cm.setName(fname);
      cm.setJavaType(String.class);
      SearchColumnMetadataBean mb = new SearchColumnMetadataBean(fname, cm);
      mb.setIndexed(true);
      mb.setIndexType(String.class);
      ret.put(fname, mb);
    }
    return ret;
  }

}
