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

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.Transient;
import org.junit.Ignore;

/**
 * JPA entity for TB_TA_GATTR.
 *
 * @author roger
 *
 */
@Ignore
@Entity
@Table(name = "TB_TST_ATTRMASTER_ATTR", indexes = {
    @Index(name = "IX_TST_ATTRMASTER_ATTR_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TST_ATTRMASTER_ATTR_MST_FK", columnList = "MASTER_FK"),
})
@SequenceGenerator(name = "SQ_TST_ATTRMASTER_ATTR_PK", sequenceName = "SQ_TST_ATTRMASTER_ATTR_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WITHDATA", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("0")
public class TestTabAttrDO extends JpaTabAttrBaseDO<TestMasterAttrDO, Long>
{

  private static final long serialVersionUID = -5490342158738541970L;

  public TestTabAttrDO()
  {

  }

  public TestTabAttrDO(TestMasterAttrDO parent)
  {
    super(parent);
  }

  public TestTabAttrDO(TestMasterAttrDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @Override
  public JpaTabAttrDataBaseDO<?, Long> createData(String data)
  {
    return new TestTabAttrDataDO(this, data);
  }

  @Override
  @Transient
  public int getMaxDataLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  @Override
  @Id
  @Column(name = "TST_ATTRMASTER_ATTR")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TST_ATTRMASTER_ATTR_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "MASTER_FK", referencedColumnName = "TST_ATTRMASTER")
  public TestMasterAttrDO getParent()
  {
    return super.getParent();
  }

}
