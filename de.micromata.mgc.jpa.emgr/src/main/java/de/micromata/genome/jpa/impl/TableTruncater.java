package de.micromata.genome.jpa.impl;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Interface TableTruncater.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface TableTruncater
{

  /**
   * Truncate table.
   *
   * @param emgr the emgr
   * @param entity the entity
   * @return the int
   */
  public int truncateTable(IEmgr<?> emgr, EntityMetadata entity);
}
