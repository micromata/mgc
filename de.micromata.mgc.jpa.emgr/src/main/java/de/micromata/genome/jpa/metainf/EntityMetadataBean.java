//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
