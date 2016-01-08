package de.micromata.genome.jpa.metainf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Meta data for an entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EntityMetadataBean extends EmgrDbElementBean implements EntityMetadata
{

  /**
   * The columns.
   */
  private Map<String, ColumnMetadata> columns = new HashMap<>();

  private Set<EntityMetadata> referencedBy = new HashSet<>();

  private Set<EntityMetadata> referencesTo = new HashSet<>();

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isTableEntity()
  {
    return getJavaType().getAnnotation(Entity.class) != null &&
        getJavaType().getAnnotation(MappedSuperclass.class) == null;
  }

  @Override
  public ColumnMetadata findColumn(String name)
  {
    return columns.get(name);
  }

  @Override
  public ColumnMetadata getColumn(String name) throws JpaMetadataColumnNotFoundException
  {
    ColumnMetadata ret = findColumn(name);
    if (ret != null) {
      return ret;
    }
    throw new JpaMetadataColumnNotFoundException("Metadata column not found" + getJavaType().getName() + "." + name);
  }

  @Override
  public ColumnMetadata getIdColumn()
  {
    for (ColumnMetadata col : columns.values()) {
      if (col.findAnnoation(Id.class) != null) {
        return col;
      }
    }
    throw new JpaMetadataColumnNotFoundException("Id column not found" + getJavaType().getName());
  }

  @Override
  public Map<String, ColumnMetadata> getColumns()
  {
    return columns;
  }

  /**
   * Sets the columns.
   *
   * @param columns the columns
   */
  public void setColumns(Map<String, ColumnMetadata> columns)
  {
    this.columns = columns;
  }

  @Override
  public Set<EntityMetadata> getReferencedBy()
  {
    return referencedBy;
  }

  @Override
  public Set<EntityMetadata> getReferencesTo()
  {
    return referencesTo;
  }

}
