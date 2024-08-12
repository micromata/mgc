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

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * JPA entity for History.
 *
 * @author roger
 *
 */
@Entity
@Table(name = "TB_BASE_GHISTORY_ATTR", indexes = {
    @Index(name = "IX_BASE_GHISTORY_ATTR_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_BASE_GHISTORY_ATTR_MST_FK", columnList = "MASTER_FK")
})
@SequenceGenerator(name = "SQ_BASE_GHISTORY_ATTR_PK", sequenceName = "SQ_BASE_GHISTORY_ATTR_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WITHDATA", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("0")
public class HistoryAttrDO extends HistoryAttrBaseDO<HistoryMasterDO, Long>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5490342158738541970L;

  /**
   * Instantiates a new history attr do.
   */
  public HistoryAttrDO()
  {

  }

  /**
   * Instantiates a new history attr do.
   *
   * @param parent the parent
   */
  public HistoryAttrDO(HistoryMasterDO parent)
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
  public HistoryAttrDO(HistoryMasterDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @Override
  public JpaTabAttrDataBaseDO<?, Long> createData(String data)
  {
    return new HistoryAttrDataDO(this, data);
  }

  @Override
  @Transient
  public int getMaxDataLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  @Override
  @Id
  @Column(name = "BASE_GHISTORY_ATTR")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_ATTR_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "MASTER_FK", referencedColumnName = "BASE_GHISTORY")
  public HistoryMasterDO getParent()
  {
    return super.getParent();
  }

}
