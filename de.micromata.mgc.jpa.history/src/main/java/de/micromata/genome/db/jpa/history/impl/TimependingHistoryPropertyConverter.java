package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.util.types.Converter;

/**
 * The Class TimependingHistoryPropertyProvider.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TimependingHistoryPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd)
  {
    List<? extends TimeableAttrRow> tlist = (List) SimplePropertyConverter.readPropertyValue(entity, pd);
    TabAttrHistoryPropertyConverter attrConverter = new TabAttrHistoryPropertyConverter();
    List<HistProp> ret = new ArrayList<>();
    for (TimeableAttrRow row : tlist) {
      String key = Converter.isoTimestampFormat.get().format(row.getStartTime());
      HistProp hp = new HistProp();
      hp.setName(key + ".startTime");
      hp.setType(Date.class.getName());
      hp.setValue(key);
      ret.add(hp);
      hp = new HistProp();
      hp.setName(key + ".endTime");
      hp.setType(Date.class.getName());
      if (row.getEndTime() != null) {
        hp.setValue(Converter.isoTimestampFormat.get().format(row.getEndTime()));
      }
      ret.add(hp);
      try {
        PropertyDescriptor attrdesc = PropertyUtils.getPropertyDescriptor(row, "attributes");
        List<HistProp> attrs = attrConverter.convert(historyMetaInfo, row, attrdesc);
        for (HistProp chp : attrs) {
          chp.setName(key + "." + chp.getName());
          ret.add(chp);
        }
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
        throw new IllegalArgumentException("Cannot find attributes in row", ex);
      }
    }
    return ret;
  }

}
