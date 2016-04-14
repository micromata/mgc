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

package de.micromata.genome.db.jpa.history.api;

import de.micromata.genome.db.jpa.history.entities.PropertyOpType;

/**
 * The Class DiffEntry.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class DiffEntry
{

  /**
   * The property op type.
   */
  private PropertyOpType propertyOpType;
  /**
   * The property name.
   */
  private String propertyName;
  private HistProp oldProp;

  private HistProp newProp;

  public PropertyOpType getPropertyOpType()
  {
    return propertyOpType;
  }

  public void setPropertyOpType(PropertyOpType propertyOpType)
  {
    this.propertyOpType = propertyOpType;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public String getOldValue()
  {
    return oldProp == null ? null : oldProp.getValue();
  }

  public String getNewValue()
  {
    return newProp == null ? null : newProp.getValue();
  }

  public HistProp getOldProp()
  {
    return oldProp;
  }

  public void setOldProp(HistProp oldProp)
  {
    this.oldProp = oldProp;
  }

  public HistProp getNewProp()
  {
    return newProp;
  }

  public void setNewProp(HistProp newProp)
  {
    this.newProp = newProp;
  }

}
