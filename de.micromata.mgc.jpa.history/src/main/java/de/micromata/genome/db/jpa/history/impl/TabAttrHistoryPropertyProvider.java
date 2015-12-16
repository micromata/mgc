package de.micromata.genome.db.jpa.history.impl;

import java.util.Map;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyProvider;
import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;

/**
 * Default entry with Attrs.
 * 
 * @deprecated use TabAttrHistoryPropertyConverter instead
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Deprecated
public class TabAttrHistoryPropertyProvider implements HistoryPropertyProvider
{
  @Override
  public void getProperties(HistoryMetaInfo historyMetaInfo, Object entity, Map<String, HistProp> map)
  {
    if ((entity instanceof EntityWithAttributes) == false) {
      return;
    }
    EntityWithAttributes ea = (EntityWithAttributes) entity;
    for (String attrkey : ea.getAttributeKeys()) {
      if (historyMetaInfo.ignoreProperty(attrkey) == true) {
        continue;
      }
      map.put(attrkey, new HistProp(attrkey, String.class.getName(), ea.getStringAttribute(attrkey)));
    }
  }

}
