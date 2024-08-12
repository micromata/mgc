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

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;

import org.junit.Ignore;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * Entity holds Strings longer than fits into one attribute value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
@Entity
@DiscriminatorValue("1")
public class TestTabAttrWithDataDO extends TestTabAttrDO
{

  private static final long serialVersionUID = 1965960042100228573L;

  public TestTabAttrWithDataDO()
  {

  }

  public TestTabAttrWithDataDO(TestMasterAttrDO parent)
  {
    super(parent);
  }

  public TestTabAttrWithDataDO(TestMasterAttrDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = TestTabAttrDataDO.class,
      orphanRemoval = true, fetch = FetchType.EAGER)
  @Override
  @OrderColumn(name = "datarow")
  public List<JpaTabAttrDataBaseDO<?, Long>> getData()
  {
    return super.getData();
  }

}
