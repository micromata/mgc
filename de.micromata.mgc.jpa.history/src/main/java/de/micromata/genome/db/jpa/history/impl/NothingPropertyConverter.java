package de.micromata.genome.db.jpa.history.impl;

import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * Return empty fields.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NothingPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    return Collections.emptyList();
  }

}
