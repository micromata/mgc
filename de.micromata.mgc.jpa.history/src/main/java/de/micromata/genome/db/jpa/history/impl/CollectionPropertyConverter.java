package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.DbRecord;

/**
 * Converts a collection of Dbrecords.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CollectionPropertyConverter implements HistoryPropertyConverter
{
  private static final Logger LOG = Logger.getLogger(CollectionPropertyConverter.class);

  @Override
  public List<HistProp> convert(Object entity, PropertyDescriptor pd)
  {
    Collection<?> col = (Collection<?>) SimplePropertyConverter.readPropertyValue(entity, pd);
    if (col == null) {
      return Collections.emptyList();
    }
    if (col.isEmpty() == true) {
      HistProp hp = new HistProp();
      hp.setName("");
      hp.setType(col.getClass().getName());
      hp.setValue("");
      return Collections.singletonList(hp);
    }
    Map<Long, Class<?>> pkSet = new TreeMap<>();
    int idx = 0;
    for (Object ob : col) {
      if ((ob instanceof DbRecord) == false) {
        LOG.warn("Cannot create collection history on non DbRecord: " + entity.getClass().getName() + "." + pd.getName()
            + "[" + idx + "]" + ob.getClass().getName());
        continue;
      }
      DbRecord<?> rec = (DbRecord<?>) ob;
      Long lp = rec.getPkAsLong();
      if (lp == null) {
        LOG.warn("History; Unsafed PK in history: " + entity.getClass().getName() + "." + pd.getName()
            + "[" + idx + "]" + ob.getClass().getName());
      }
      pkSet.put(lp, ob.getClass());
      ++idx;
    }
    idx = 0;
    List<HistProp> hpret = new ArrayList<>();
    for (Map.Entry<Long, Class<?>> me : pkSet.entrySet()) {
      HistProp hp = new HistProp();
      hp.setName(me.getValue().toString());
      hp.setType(col.getClass().getName() + "<" + me.getValue().getName() + ">");
      hp.setValue(me.getValue().toString());
      hpret.add(hp);
      ++idx;
    }
    return hpret;
  }

}
