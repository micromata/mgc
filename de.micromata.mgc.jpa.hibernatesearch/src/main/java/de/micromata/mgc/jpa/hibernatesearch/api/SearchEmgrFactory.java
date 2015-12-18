package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.Collections;
import java.util.Map;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
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
   * Get the columns, which are searchable for the entity.
   * 
   * @param entityClass
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, ColumnMetadata> getSearchableFieldsForEntity(Class<?> entityClass)
  {
    Map<Class<?>, Map<String, ColumnMetadata>> sfm = (Map<Class<?>, Map<String, ColumnMetadata>>) getMetadataRepository()
        .getServiceCustomAttributes()
        .get(REPO_ENTITY_SEARCHFIELDS);
    if (sfm == null) {
      return Collections.emptyMap();
    }
    Map<String, ColumnMetadata> esf = sfm.get(entityClass);
    if (esf == null) {
      return Collections.emptyMap();
    }
    return esf;
  }
}
