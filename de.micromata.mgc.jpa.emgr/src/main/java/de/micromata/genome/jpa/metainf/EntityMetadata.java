package de.micromata.genome.jpa.metainf;

import java.util.Map;

/**
 * The Interface EntityMetadata.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface EntityMetadata extends EmgrDbElement
{

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  Map<String, ColumnMetadata> getColumns();
}
