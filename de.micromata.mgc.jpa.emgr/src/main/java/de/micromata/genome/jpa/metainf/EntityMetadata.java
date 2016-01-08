package de.micromata.genome.jpa.metainf;

import java.util.Map;

/**
 * Provides Information about JPA Mappings for one entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface EntityMetadata extends EmgrDbElement
{

  /**
   * Is an entity with table.
   *
   * @return true, if is table entity
   */
  boolean isTableEntity();

  /**
   * Find column.
   *
   * @param name the name
   * @return the column metadata or null if not found.
   */
  ColumnMetadata findColumn(String name);

  /**
   * Gets the column.
   *
   * @param name the name
   * @return the column
   * @throws JpaMetadataColumnNotFoundException the jpa metadata column not found exception
   */
  ColumnMetadata getColumn(String name) throws JpaMetadataColumnNotFoundException;

  /**
   * Gets the Id column.
   *
   * @return the id column
   * @throws JpaMetadataColumnNotFoundException the jpa metadata column not found exception
   */
  ColumnMetadata getIdColumn() throws JpaMetadataColumnNotFoundException;

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  Map<String, ColumnMetadata> getColumns();

}
