package de.micromata.genome.db.jpa.history.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.hibernate.proxy.HibernateProxy;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * Wrapps an entity reference.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DbRecordPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    Object val = pd.getGetter().get(entity);
    DbRecord<?> dbrec = (DbRecord<?>) val;
    Serializable pk = null;
    HistProp hp = new HistProp();
    hp.setName("");
    if (dbrec != null) {
      // to avoid lazy initialization problems, only evaluate the proxy.
      if (dbrec instanceof HibernateProxy) {
        HibernateProxy hibernateProxy = (HibernateProxy) dbrec;
        pk = hibernateProxy.getHibernateLazyInitializer().getIdentifier();
      } else {
        pk = dbrec.getPk();
      }
    }
    if (pk != null) {
      hp.setValue(pk.toString());
    }
    Class<?> clazz = pd.getJavaType();
    if (val != null) {
      clazz = val.getClass();
    }
    hp.setType(clazz.getName());
    return Collections.singletonList(hp);
  }

}
