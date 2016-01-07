package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.jpa.metainf.ColumnMetadataBean;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchFieldInfoProvider;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;

/**
 * Provides a list of field names.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ListHibernateFieldInfoProvider implements HibernateSearchFieldInfoProvider
{

  @Override
  public Map<String, SearchColumnMetadata> getAdditionallySearchFields(EntityMetadata entm, String params)
  {
    Map<String, SearchColumnMetadata> ret = new HashMap<>();
    String[] fields = StringUtils.split(params, ',');
    for (String field : fields) {
      String fname = StringUtils.trim(field);
      ColumnMetadataBean cm = new ColumnMetadataBean(entm);
      cm.setName(fname);
      cm.setJavaType(String.class);
      SearchColumnMetadataBean mb = new SearchColumnMetadataBean(fname, cm);
      mb.setIndexed(true);
      mb.setIndexType(String.class);
      ret.put(fname, mb);
    }
    return ret;
  }

}
