package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.DbRecord;

/**
 * Wrapps an entity reference.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DbRecordPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd)
  {
    Object val = SimplePropertyConverter.readPropertyValue(entity, pd);
    DbRecord dbrec = (DbRecord) val;
    HistProp hp = new HistProp();
    hp.setName("");

    if (dbrec != null) {
      Serializable pk = dbrec.getPk();
      if (pk != null) {
        hp.setValue(pk.toString());
      }
    }
    Class<?> clazz = pd.getPropertyType();
    if (val != null) {
      clazz = val.getClass();
    }
    hp.setType(clazz.getName());
    return Collections.singletonList(hp);
  }

}
