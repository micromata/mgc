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

package de.micromata.mgc.jpa.xmldump.entities;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlPersist;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.junit.Ignore;

/**
 * JPA entity for TB_TA_GATTR_DATA
 * 
 * @author roger
 * 
 */
@JpaXmlPersist(noStore = true)
@Ignore
@Entity
@Table(name = "TB_TST_ATTRMASTER_ATTR_DATA", indexes = {
    @Index(name = "IX_TST_AM_ATTR_DATA_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TST_AM_ATTR_DATA_PARENT", columnList = "PARENT_PK")
})
@SequenceGenerator(name = "SQ_TST_AM_ATTR_DATA_PK", sequenceName = "SQ_TST_AM_ATTR_DATA_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestTabAttrDataDO extends JpaTabAttrDataBaseDO<TestTabAttrDO, Long>
{

  private static final long serialVersionUID = -3845387843789907008L;

  public TestTabAttrDataDO()
  {

  }

  public TestTabAttrDataDO(TestTabAttrDO parent)
  {
    super(parent);
  }

  public TestTabAttrDataDO(TestTabAttrDO parent, String value)
  {
    super(parent, value);
  }

  @Override
  @Id
  @Column(name = "TST_ATTRMASTER_ATTR_DATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TST_AM_ATTR_DATA_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK", referencedColumnName = "TST_ATTRMASTER_ATTR")
  public TestTabAttrDO getParent()
  {
    return super.getParent();
  }
}
