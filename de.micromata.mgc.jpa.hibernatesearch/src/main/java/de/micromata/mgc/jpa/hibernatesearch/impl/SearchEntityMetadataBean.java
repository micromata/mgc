package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.util.HashMap;
import java.util.Map;

import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEntityMetadata;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SearchEntityMetadataBean implements SearchEntityMetadata
{
  private EntityMetadata entityMetadata;
  private Map<String, SearchColumnMetadata> columns = new HashMap<>();
  private boolean resolved;

  public SearchEntityMetadataBean(EntityMetadata entityMetadata)
  {
    super();
    this.entityMetadata = entityMetadata;
  }

  @Override
  public String toString()
  {
    return entityMetadata.toString();
  }

  @Override
  public EntityMetadata getEntityMetadata()
  {
    return entityMetadata;
  }

  public void setEntityMetadata(EntityMetadata entityMetadata)
  {
    this.entityMetadata = entityMetadata;
  }

  @Override
  public Map<String, SearchColumnMetadata> getColumns()
  {
    return columns;
  }

  public void setColumns(Map<String, SearchColumnMetadata> columns)
  {
    this.columns = columns;
  }

  public boolean isResolved()
  {
    return resolved;
  }

  public void setResolved(boolean resolved)
  {
    this.resolved = resolved;
  }

}
