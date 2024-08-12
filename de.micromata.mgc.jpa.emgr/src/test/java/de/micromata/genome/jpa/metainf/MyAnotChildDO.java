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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import de.micromata.genome.jpa.DbRecordDO;

@Entity
public class MyAnotChildDO extends DbRecordDO<Long>
{
  private MyAnotTestDO parent;

  private String propertyName;

  @Column(length = 64)
  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  @Id
  @Column(name = "pk")
  @Override
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "parent", referencedColumnName = "pk")
  public MyAnotTestDO getParent()
  {
    return parent;
  }

  public void setParent(MyAnotTestDO parent)
  {
    this.parent = parent;
  }

}
