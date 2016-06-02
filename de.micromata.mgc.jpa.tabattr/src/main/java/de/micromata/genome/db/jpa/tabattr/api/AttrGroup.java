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

public class AttrGroup implements Serializable
{

  /**
   * The columns.
   */
  private List<AttrDescription> columns;

  /**
   * The i18n key of the title.
   */
  private String i18nKey;

  /**
   * Instantiates a new attr group.
   */
  public AttrGroup()
  {

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

  public String getI18nKey()
  {
    return i18nKey;
  }

  public void setI18nKey(String i18nKey)
  {
    this.i18nKey = i18nKey;
  }
}
