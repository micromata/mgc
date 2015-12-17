package de.micromata.genome.db.jpa.history.api;

import java.util.Map;

import de.micromata.genome.db.jpa.history.impl.HistoryMetaInfo;
import de.micromata.genome.jpa.IEmgr;

/**
 * Retrieve for a Entity a flat representation for history.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface HistoryPropertyProvider
{

  /**
   * A Map of properties to make history diff.
   *
   * @param entity the entity
   * @return the properties
   */
  void getProperties(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, Map<String, HistProp> map);
}
