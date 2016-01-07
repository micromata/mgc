package de.micromata.mgc.jpa.hibernatesearch.api;

import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * Meta info about a column searchs.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface SearchColumnMetadata
{

  /**
   * Name of search index.
   *
   * @return the name
   */
  String getName();

  /**
   * Based on column.
   *
   * @return the column metadata
   */
  ColumnMetadata getColumnMetadata();

  /**
   * Gets the index type.
   *
   * @return the index type
   */
  Class<?> getIndexType();

  /**
   * Checks if is indexed.
   *
   * @return true, if is indexed
   */
  boolean isIndexed();

  /**
   * Checks if is stored.
   *
   * @return true, if is stored
   */
  boolean isStored();

  /**
   * Checks if is analyzed.
   *
   * @return true, if is analyzed
   */
  boolean isAnalyzed();

  boolean isIdField();
}
