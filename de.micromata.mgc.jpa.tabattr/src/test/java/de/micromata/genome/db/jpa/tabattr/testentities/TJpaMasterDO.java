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

package de.micromata.genome.db.jpa.tabattr.testentities;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table()
@Entity
public class TJpaMasterDO extends TJpaBaseDO
{
  Map<String, TJpaMasterAttrDO> attributes = new TreeMap<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = TJpaMasterAttrDO.class,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")
  public Map<String, TJpaMasterAttrDO> getAttributes()
  {
    return attributes;
  }

  public void setAttributes(Map<String, TJpaMasterAttrDO> attributes)
  {
    this.attributes = attributes;
  }

  public void putAttribute(String key, String value)
  {
    TJpaMasterAttrDO atr = attributes.get(key);
    if (atr == null) {
      atr = new TJpaMasterAttrDO();
      atr.setPropertyName(key);
      atr.setParent(this);
      attributes.put(key, atr);
    }
    atr.setValue(value);
  }

  public void putNewAttribute(String key, String value)
  {
    TJpaMasterAttrDO atr = new TJpaMasterAttrDO();
    atr.setPropertyName(key);
    atr.setParent(this);
    atr.setValue(value);
    attributes.put(key, atr);
  }
}
