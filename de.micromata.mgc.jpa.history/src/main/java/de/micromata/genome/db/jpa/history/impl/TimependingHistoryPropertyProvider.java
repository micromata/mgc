package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;

/**
 * The Class TimependingHistoryPropertyProvider.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TimependingHistoryPropertyProvider implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(Object entity, PropertyDescriptor pd)
  {
    List<? extends TimeableAttrRow> tlist = (List) SimplePropertyConverter.readPropertyValue(entity, pd);

    List<HistProp> ret = new ArrayList<>();
    for (TimeableAttrRow row : tlist) {

    }
    return ret;
  }

}
