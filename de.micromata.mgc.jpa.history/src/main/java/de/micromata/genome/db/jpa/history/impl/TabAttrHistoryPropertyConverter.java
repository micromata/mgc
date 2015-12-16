package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
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
  public List<HistProp> convert(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd)
  {
    Map<String, JpaTabAttrBaseDO<?, ?>> tmap = (Map) SimplePropertyConverter.readPropertyValue(entity, pd);
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
    return ct.getEncodeClass().getName();
  }

}
