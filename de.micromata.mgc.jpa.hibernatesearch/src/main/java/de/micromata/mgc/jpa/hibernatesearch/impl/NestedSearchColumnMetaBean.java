package de.micromata.mgc.jpa.hibernatesearch.impl;

import org.hibernate.search.annotations.IndexedEmbedded;

import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NestedSearchColumnMetaBean extends SearchColumnMetadataBean
{
  private boolean resolved = false;
  private IndexedEmbedded indexEmbedded;

  public NestedSearchColumnMetaBean()
  {
    super();
  }

  public NestedSearchColumnMetaBean(String name, ColumnMetadata columnMetadata, IndexedEmbedded indexEmbedded)
  {
    super(name, columnMetadata);
    this.indexEmbedded = indexEmbedded;
  }

  public IndexedEmbedded getIndexEmbedded()
  {
    return indexEmbedded;
  }

  public void setIndexEmbedded(IndexedEmbedded indexEmbedded)
  {
    this.indexEmbedded = indexEmbedded;
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
