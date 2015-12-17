package de.micromata.genome.db.jpa.history.api;

import java.util.List;

import de.micromata.genome.db.jpa.history.impl.HistoryMetaInfo;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;

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
  List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd);
}
