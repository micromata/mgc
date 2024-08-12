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

package de.micromata.genome.db.jpa.history.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;

/**
 * The Class HistoryAttrBaseDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <M> the generic type
 * @param <PK> the generic type
 */
@MappedSuperclass
public abstract class HistoryAttrBaseDO<M extends HistoryMasterBaseDO<?, ?>, PK extends Serializable>
    extends JpaTabAttrBaseDO<M, PK>
{

  /**
   * Name of the type class.
   */

  private String propertyTypeClass;

  /**
   * Instantiates a new history attr do.
   */
  public HistoryAttrBaseDO()
  {

  }

  /**
   * Instantiates a new history attr do.
   *
   * @param parent the parent
   */
  public HistoryAttrBaseDO(M parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new history attr do.
   *
   * @param parent the parent
   * @param propertyName the property name
   * @param type the type
   * @param value the value
   */
  public HistoryAttrBaseDO(M parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  /**
   * Gets the property type class.
   *
   * @return the property type class
   */
  @Column(name = "PROPERTY_TYPE_CLASS", length = 128)
  public String getPropertyTypeClass()
  {
    return propertyTypeClass;
  }

  /**
   * Sets the property type class.
   *
   * @param propertyTypeClass the new property type class
   */
  public void setPropertyTypeClass(String propertyTypeClass)
  {
    this.propertyTypeClass = propertyTypeClass;
  }
}
