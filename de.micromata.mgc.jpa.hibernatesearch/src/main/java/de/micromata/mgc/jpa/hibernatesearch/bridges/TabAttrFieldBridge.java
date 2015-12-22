package de.micromata.mgc.jpa.hibernatesearch.bridges;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;

/**
 * Add to Attr
 * 
 * TODO RK not testes, may delete
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TabAttrFieldBridge implements FieldBridge
{
  private static final Logger LOG = Logger.getLogger(TabAttrFieldBridge.class);

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
  {
    if ((value instanceof Map) == false) {
      return;
    }
    Map rawmap = (Map) value;
    if (rawmap.isEmpty() == true) {
      return;
    }

    Set<Map.Entry> mes = rawmap.entrySet();
    for (Map.Entry me : mes) {
      if ((me.getKey() instanceof String) == false || (me.getValue() instanceof JpaTabAttrBaseDO) == false) {
        LOG.error("Bridge to incompatible type: " + value);
      }
    }
  }

}
