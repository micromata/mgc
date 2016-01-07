package de.micromata.mgc.jpa.hibernatesearch;

import java.util.Map;

import org.junit.Test;

import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * test the meta data features
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSearchMetadataTest extends HibernateSearchTestBase
{
  @Test
  public void testMyEntity()
  {
    try {
      HibernateSearchTestEmgrFactory fac = HibernateSearchTestEmgrFactory.get();
      Map<String, SearchColumnMetadata> searchFields = fac.getSearchFieldsForEntity(MyEntityDO.class);
      System.out.println("Search fields for MyEntityDO: " + searchFields.keySet());
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
