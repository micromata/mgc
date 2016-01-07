package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.Map;

import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Interface HibernateSearchFieldInfoProvider.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface HibernateSearchFieldInfoProvider
{

  /**
   * Gets the additionally search fields.
   *
   * @param entityClass the entity class
   * @return the additionally search fields
   */
  Map<String, SearchColumnMetadata> getAdditionallySearchFields(EntityMetadata entm, String params);
}
