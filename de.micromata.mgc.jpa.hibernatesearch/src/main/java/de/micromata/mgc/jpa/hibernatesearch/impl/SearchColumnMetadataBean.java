package de.micromata.mgc.jpa.hibernatesearch.impl;

import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;

/**
 * Bean holding SearchColumnMetadata.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SearchColumnMetadataBean implements SearchColumnMetadata, Cloneable
{
  private String name;

  private ColumnMetadata columnMetadata;
  /**
   * Type of the storage.
   */
  private Class<?> indexType;

  private boolean indexed;
  private boolean stored;
  private boolean analyzed;
  private boolean idField;

  public SearchColumnMetadataBean()
  {

  }

  public SearchColumnMetadataBean(String name, ColumnMetadata columnMetadata)
  {
    super();
    this.name = name;
    this.columnMetadata = columnMetadata;
  }

  public SearchColumnMetadataBean createCopy()
  {
    SearchColumnMetadataBean nc = new SearchColumnMetadataBean(name, columnMetadata);
    nc.setAnalyzed(isAnalyzed());
    nc.setIdField(isIdField());
    nc.setIndexed(isIndexed());
    nc.setIndexType(getIndexType());
    nc.setStored(isStored());
    return nc;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public ColumnMetadata getColumnMetadata()
  {
    return columnMetadata;
  }

  public void setColumnMetadata(ColumnMetadata columnMetadata)
  {
    this.columnMetadata = columnMetadata;
  }

  @Override
  public Class<?> getIndexType()
  {
    return indexType;
  }

  public void setIndexType(Class<?> indexType)
  {
    this.indexType = indexType;
  }

  @Override
  public boolean isIndexed()
  {
    return indexed;
  }

  public void setIndexed(boolean indexed)
  {
    this.indexed = indexed;
  }

  @Override
  public boolean isStored()
  {
    return stored;
  }

  public void setStored(boolean stored)
  {
    this.stored = stored;
  }

  @Override
  public boolean isAnalyzed()
  {
    return analyzed;
  }

  public void setAnalyzed(boolean analyzed)
  {
    this.analyzed = analyzed;
  }

  @Override
  public boolean isIdField()
  {
    return idField;
  }

  public void setIdField(boolean idField)
  {
    this.idField = idField;
  }

}
