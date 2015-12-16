package de.micromata.genome.db.jpa.history.api;

import java.beans.PropertyDescriptor;
import java.util.List;

import de.micromata.genome.db.jpa.history.impl.HistoryMetaInfo;

/**
 * The Interface HistoryPropertyConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface HistoryPropertyConverter
{

  /**
   * Converts a property to a string for storing an history entry.
   *
   * @param entity the entity
   * @param pd the pd
   * @return the string
   */
  List<HistProp> convert(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd);
}
