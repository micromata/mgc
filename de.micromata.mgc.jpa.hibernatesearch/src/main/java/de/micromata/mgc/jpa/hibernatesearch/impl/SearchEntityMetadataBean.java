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

package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.util.HashMap;
import java.util.Map;

import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEntityMetadata;

/**
 * An entity with full text search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SearchEntityMetadataBean implements SearchEntityMetadata
{

  /**
   * The entity metadata.
   */
  private EntityMetadata entityMetadata;

  /**
   * The columns.
   */
  private Map<String, SearchColumnMetadata> columns = new HashMap<>();

  /**
   * The resolved.
   */
  private boolean resolved;

  /**
   * Instantiates a new search entity metadata bean.
   *
   * @param entityMetadata the entity metadata
   */
  public SearchEntityMetadataBean(EntityMetadata entityMetadata)
  {
    super();
    this.entityMetadata = entityMetadata;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String toString()
  {
    return entityMetadata.toString();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public EntityMetadata getEntityMetadata()
  {
    return entityMetadata;
  }

  /**
   * Sets the entity metadata.
   *
   * @param entityMetadata the new entity metadata
   */
  public void setEntityMetadata(EntityMetadata entityMetadata)
  {
    this.entityMetadata = entityMetadata;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Map<String, SearchColumnMetadata> getColumns()
  {
    return columns;
  }

  /**
   * Sets the columns.
   *
   * @param columns the columns
   */
  public void setColumns(Map<String, SearchColumnMetadata> columns)
  {
    this.columns = columns;
  }

  /**
   * Checks if is resolved.
   *
   * @return true, if is resolved
   */
  public boolean isResolved()
  {
    return resolved;
  }

  /**
   * Sets the resolved.
   *
   * @param resolved the new resolved
   */
  public void setResolved(boolean resolved)
  {
    this.resolved = resolved;
  }

}
