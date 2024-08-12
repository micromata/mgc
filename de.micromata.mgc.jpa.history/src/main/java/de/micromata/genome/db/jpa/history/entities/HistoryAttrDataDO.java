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

/**
 * JPA entity for TB_TA_GATTR_DATA.
 *
 * @author roger
 */

@Entity
@Table(name = "TB_BASE_GHISTORY_ATTR_DATA", indexes = {
    @Index(name = "IX_BASE_GHISTORY_A_D_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_BASE_GHISTORY_A_D_PARENT", columnList = "PARENT_PK")
})
@SequenceGenerator(name = "SQ_BASE_GHISTORY_ATTR_DATA_PK", sequenceName = "SQ_BASE_GHISTORY_ATTR_DATA_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HistoryAttrDataDO extends JpaTabAttrDataBaseDO<HistoryAttrDO, Long>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -3845387843789907008L;

  /**
   * Instantiates a new history attr data do.
   */
  public HistoryAttrDataDO()
  {

  }

  /**
   * Instantiates a new history attr data do.
   *
   * @param parent the parent
   */
  public HistoryAttrDataDO(HistoryAttrDO parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new history attr data do.
   *
   * @param parent the parent
   * @param value the value
   */
  public HistoryAttrDataDO(HistoryAttrDO parent, String value)
  {
    super(parent, value);
  }

  @Override
  @Id
  @Column(name = "BASE_GHISTORY_ATTR_DATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_ATTR_DATA_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK", referencedColumnName = "BASE_GHISTORY_ATTR")
  public HistoryAttrDO getParent()
  {
    return super.getParent();
  }
}
