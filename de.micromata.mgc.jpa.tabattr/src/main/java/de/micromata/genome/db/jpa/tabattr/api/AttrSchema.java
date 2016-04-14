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

package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;
import java.util.List;

/**
 *
 * A Schema for Attributes.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AttrSchema implements Serializable
{

  /**
   * The columns.
   */
  private List<AttrDescription> columns;

  /**
   * Instantiates a new attr schema.
   */
  public AttrSchema()
  {

  }

  /**
   * Instantiates a new attr schema.
   *
   * @param columns the columns
   */
  public AttrSchema(final List<AttrDescription> columns)
  {
    this.columns = columns;
  }

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  public List<AttrDescription> getColumns()
  {
    return columns;
  }

  /**
   * Sets the columns.
   *
   * @param columns the new columns
   */
  public void setColumns(final List<AttrDescription> columns)
  {
    this.columns = columns;

  }
}
