package de.micromata.genome.db.jpa.history.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.util.strings.converter.ConvertedStringTypes;

/**
 * Converts a attr map to history entries.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TabAttrHistoryPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    Map<String, JpaTabAttrBaseDO<?, ?>> tmap = (Map) pd.getGetter().get(entity);
    List<HistProp> ret = new ArrayList<>(tmap.size());
    for (Map.Entry<String, JpaTabAttrBaseDO<?, ?>> me : tmap.entrySet()) {
      if (historyMetaInfo.ignoreProperty(me.getKey()) == true) {
        continue;
      }
      HistProp hp = new HistProp();
      hp.setName(me.getKey());
      hp.setType(typeCharToClassName(me.getValue().getType()));
      hp.setValue(me.getValue().getValue());
      ret.add(hp);
    }
    return ret;
  }

  private String typeCharToClassName(char type)
  {
    ConvertedStringTypes ct = ConvertedStringTypes.getValueTypeByShortName(type);
    if (ct.getEncodeClass() == null) {
      return void.class.getName();
    }
    return ct.getEncodeClass().getName();
  }

}
