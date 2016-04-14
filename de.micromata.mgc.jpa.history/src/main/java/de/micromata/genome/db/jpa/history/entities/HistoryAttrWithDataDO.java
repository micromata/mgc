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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * Entity holds Strings longer than fits into one attribute value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Entity
@DiscriminatorValue("1")
public class HistoryAttrWithDataDO extends HistoryAttrDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1965960042100228573L;

  /**
   * Instantiates a new history attr with data do.
   */
  public HistoryAttrWithDataDO()
  {

  }

  /**
   * Instantiates a new history attr with data do.
   *
   * @param parent the parent
   */
  public HistoryAttrWithDataDO(HistoryMasterDO parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new history attr with data do.
   *
   * @param parent the parent
   * @param propertyName the property name
   * @param type the type
   * @param value the value
   */
  public HistoryAttrWithDataDO(HistoryMasterDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = HistoryAttrDataDO.class,
      orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderColumn(name = "datarow")
  @Override
  public List<JpaTabAttrDataBaseDO<?, Long>> getData()
  {
    return super.getData();
  }

}
