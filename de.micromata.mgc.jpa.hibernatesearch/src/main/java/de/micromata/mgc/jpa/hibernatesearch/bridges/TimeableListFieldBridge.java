package de.micromata.mgc.jpa.hibernatesearch.bridges;

import java.util.List;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * 
 * TODO RK not testes, may delete
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TimeableListFieldBridge implements FieldBridge
{

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
  {
    if ((value instanceof List) == false) {
      return;
    }
  }

}
