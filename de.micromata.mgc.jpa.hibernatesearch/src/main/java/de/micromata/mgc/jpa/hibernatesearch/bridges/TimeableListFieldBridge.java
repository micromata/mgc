package de.micromata.mgc.jpa.hibernatesearch.bridges;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.util.types.Converter;

/**
 * 
 * Renders a Timeable Attributes List.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TimeableListFieldBridge implements FieldBridge
{
  private static Logger LOG = Logger.getLogger(TimeableListFieldBridge.class);

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
  {
    if ((value instanceof List) == false) {
      return;
    }
    List timelist = (List) value;
    for (Object el : timelist) {
      if ((el instanceof TimeableAttrRow) == false) {
        LOG.warn("No TimeableAttrRow, cannot create search index: " + el);
        break;
      }
      TimeableAttrRow<?> row = (TimeableAttrRow<?>) el;
      Date start = row.getStartTime();
      String prefix = Converter.dateToDebugString(start) + ".";
      prefix = StringUtils.replace(StringUtils.replace(prefix, ".", "-"), ":", "_");
      for (String rk : row.getAttributeKeys()) {
        String svalue = row.getStringAttribute(rk);
        if (StringUtils.isEmpty(svalue) == true) {
          continue;
        }
        Field field = new StringField(prefix + rk, svalue, TabAttrFieldBridge.DEFAULT_STORE);
        document.add(field);
      }
    }
  }

}
