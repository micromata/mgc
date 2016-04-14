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

import java.util.Map;
import java.util.Set;

/**
 * Provides Information about JPA Mappings for one entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface EntityMetadata extends EmgrDbElement
{

  /**
   * Is an entity with table.
   *
   * @return true, if is table entity
   */
  boolean isTableEntity();

  /**
   * Find column.
   *
   * @param name the name
   * @return the column metadata or null if not found.
   */
  ColumnMetadata findColumn(String name);

  /**
   * Gets the column.
   *
   * @param name the name
   * @return the column
   * @throws JpaMetadataColumnNotFoundException the jpa metadata column not found exception
   */
  ColumnMetadata getColumn(String name) throws JpaMetadataColumnNotFoundException;

  /**
   * Gets the Id column.
   *
   * @return the id column
   * @throws JpaMetadataColumnNotFoundException the jpa metadata column not found exception
   */
  ColumnMetadata getIdColumn() throws JpaMetadataColumnNotFoundException;

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  Map<String, ColumnMetadata> getColumns();

  public Set<EntityMetadata> getReferencedBy();

  public Set<EntityMetadata> getReferencesTo();
}
