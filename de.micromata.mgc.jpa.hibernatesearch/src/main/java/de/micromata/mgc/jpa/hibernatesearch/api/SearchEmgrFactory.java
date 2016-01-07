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
public abstract class SearchEmgrFactory<EMGR extends ISearchEmgr<?>>extends EmgrFactory<EMGR>
{
  /**
   * Key to getMetadataRepository().getServiceCustomAttributes().
   */
  public static Class<?> REPO_ENTITY_SEARCHFIELDS = org.hibernate.search.annotations.Field.class;

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
    Map<Class<?>, Map<String, SearchColumnMetadata>> sfm = (Map<Class<?>, Map<String, SearchColumnMetadata>>) getMetadataRepository()
        .getServiceCustomAttributes()
        .get(REPO_ENTITY_SEARCHFIELDS);
    if (sfm == null) {
      return Collections.emptyMap();
    }
    Map<String, SearchColumnMetadata> esf = sfm.get(entityClass);
    if (esf == null) {
      return Collections.emptyMap();
    }
    return esf;
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
