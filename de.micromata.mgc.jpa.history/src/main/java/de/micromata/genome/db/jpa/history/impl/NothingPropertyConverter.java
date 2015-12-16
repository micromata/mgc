package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;

/**
 * Return empty fields.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NothingPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd)
  {
    return Collections.emptyList();
  }

}
