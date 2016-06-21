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

package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.mgc.jpa.hibernatesearch.impl.SearchEmgrFactoryRegistryUtils;

/**
 * Entity Manager supporting Hibernate Search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <EMGR>
 */
public abstract class SearchEmgrFactory<EMGR extends ISearchEmgr<?>> extends EmgrFactory<EMGR>
{
  /**
   * Key to getMetadataRepository().getServiceCustomAttributes().
   */
  public static Class<?> REPO_ENTITY_SEARCHFIELDS = org.hibernate.search.annotations.Field.class;

  protected SearchEmgrFactory()
  {
    super();
  }

  protected SearchEmgrFactory(String unitName)
  {
    super(unitName);
  }

  @Override
  protected void initMetadata()
  {
    super.initMetadata();
    SearchEmgrFactoryRegistryUtils.initJpaMetadataRepostory(this);
  }

  /**
   * Get all the lucene columns for the enetity.
   * 
   * @param entityClass
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, SearchColumnMetadata> getSearchFieldsForEntity(Class<?> entityClass)
  {
    Map<Class<?>, SearchEntityMetadata> sfm = (Map<Class<?>, SearchEntityMetadata>) getMetadataRepository()
        .getServiceCustomAttributes()
        .get(REPO_ENTITY_SEARCHFIELDS);
    if (sfm == null) {
      return Collections.emptyMap();
    }
    SearchEntityMetadata esf = sfm.get(entityClass);
    if (esf == null) {
      return Collections.emptyMap();
    }
    return esf.getColumns();
  }

  public Set<Class<?>> getSearchableEntities()
  {
    Map<Class<?>, SearchEntityMetadata> sfm = (Map<Class<?>, SearchEntityMetadata>) getMetadataRepository()
        .getServiceCustomAttributes()
        .get(REPO_ENTITY_SEARCHFIELDS);
    if (sfm == null) {
      return Collections.emptySet();
    }
    return sfm.keySet();
  }

  /**
   * Return only indexed text fields.
   *
   * @param entityClass the entity class
   * @return the searchable text fields for entity
   */
  public Set<String> getSearchableTextFieldsForEntity(Class<?> entityClass)
  {
    Map<String, SearchColumnMetadata> md = getSearchFieldsForEntity(entityClass);
    Set<String> searchFieldSet = new HashSet<>();
    for (SearchColumnMetadata scm : md.values()) {
      if (scm.isIndexed() == false) {
        continue;
      }
      if (scm.getIndexType() != String.class) {
        continue;
      }
      searchFieldSet.add(scm.getName());
    }
    return searchFieldSet;
  }

}
