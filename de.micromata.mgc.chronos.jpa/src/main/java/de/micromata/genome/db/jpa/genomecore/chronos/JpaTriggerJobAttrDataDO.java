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

package de.micromata.genome.db.jpa.genomecore.chronos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * JPA entity for TB_TA_AMOUNT_ATTR_DATA.
 *
 * @author Vitalij Schmidt (v.schmidt@micromata.de)
 */
@Entity
@Table(name = "TB_TA_CHRONOS_JOB_ATTR_DATA")
@SequenceGenerator(name = "SQ_CHRONOS_JOB_ATTR_DATA_PK", sequenceName = "SQ_CHRONOS_JOB_ATTR_DATA_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = { //
        @Index(name = "IX_CHRONOS_JOB_ATTR_DATA_MODAT", columnNames = { "MODIFIEDAT" }), //
        @Index(name = "IX_JOB_ATTR_DATA_PARENT", columnNames = { "PARENT_PK" }),//
    }, appliesTo = "TB_TA_CHRONOS_JOB_ATTR_DATA")
public class JpaTriggerJobAttrDataDO extends JpaTabAttrDataBaseDO<JpaTriggerJobAttrDO, Long>
{

  /**
   *
  */

  private static final long serialVersionUID = -281215744105301889L;

  /**
   * Default constructor.
   */
  public JpaTriggerJobAttrDataDO()
  {
    // Empty on purpose.
  }

  /**
   * Initialize with parent.
   *
   * @param parent the parent.
   */
  public JpaTriggerJobAttrDataDO(JpaTriggerJobAttrDO parent)
  {
    super(parent);
  }

  /**
   * Initialize with parent and value.
   *
   * @param parent the parent
   * @param value the value
   */
  public JpaTriggerJobAttrDataDO(JpaTriggerJobAttrDO parent, String value)
  {
    super(parent, value);
  }

  @Override
  @Id
  @Column(name = "TA_CHRONOS_JOB_ATTR_DATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CHRONOS_JOB_ATTR_DATA_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK", referencedColumnName = "TA_CHRONOS_JOB_ATTR")
  public JpaTriggerJobAttrDO getParent()
  {
    return super.getParent();
  }
}
