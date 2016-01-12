package de.micromata.genome.jpa.impl;

import javax.persistence.Query;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SimpleDeleteTableTruncater implements TableTruncater
{

  @Override
  public int truncateTable(IEmgr<?> emgr, EntityMetadata entity)
  {
    Query query = emgr.getEntityManager().createQuery("delete  from " + entity.getJavaType().getName() + " e");
    int updated = query.executeUpdate();
    return updated;
  }

}
