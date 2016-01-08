package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.Map;

import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * Represents the hibernate search meta data for for an entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface SearchEntityMetadata
{

  /**
   * The JPA metadata .
   *
   * @return the entity metadata
   */
  EntityMetadata getEntityMetadata();

  /**
   * Search columns.
   *
   * @return the columns
   */
  Map<String, SearchColumnMetadata> getColumns();
}
